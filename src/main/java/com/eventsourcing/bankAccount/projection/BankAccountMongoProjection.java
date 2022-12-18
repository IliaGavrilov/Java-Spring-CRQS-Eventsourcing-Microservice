package com.eventsourcing.bankAccount.projection;


import com.eventsourcing.bankAccount.domain.BankAccountAggregate;
import com.eventsourcing.bankAccount.domain.BankAccountDocument;
import com.eventsourcing.bankAccount.events.*;
import com.eventsourcing.bankAccount.exceptions.BankAccountDocumentNotFoundException;
import com.eventsourcing.bankAccount.repository.BankAccountMongoRepository;
import com.eventsourcing.es.*;
import com.eventsourcing.mappers.BankAccountMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BankAccountMongoProjection implements Projection {

    private final BankAccountMongoRepository mongoRepository;
    private final EventStoreDB eventStoreDB;

    @KafkaListener(topics = {"${microservice.kafka.topics.bank-account-event-store}"},
            groupId = "${microservice.kafka.groupId}",
            concurrency = "${microservice.kafka.default-concurrency}")
    public void bankAccountMongoProjectionListener(@Payload byte[] data, ConsumerRecordMetadata meta, Acknowledgment ack) {
        log.info("(BankAccountMongoProjection) topic: {}, offset: {}, partition: {}, timestamp: {}, data: {}", meta.topic(), meta.offset(), meta.partition(), meta.timestamp(), new String(data));

        try {
            final Event[] events = SerializerUtils.deserializeEventsFromJsonBytes(data);
            this.processEvents(Arrays.stream(events).toList());
            ack.acknowledge();
            log.info("ack events: {}", Arrays.toString(events));
        } catch (Exception ex) {
            ack.nack(100);
            log.error("(BankAccountMongoProjection) topic: {}, offset: {}, partition: {}, timestamp: {}", meta.topic(), meta.offset(), meta.partition(), meta.timestamp(), ex);
        }
    }

    private void processEvents(List<Event> events) {
        if (events.isEmpty()) return;

        try {
            events.forEach(this::when);
        } catch (Exception ex) {
            mongoRepository.deleteByAggregateId(events.get(0).getAggregateId());
            final var aggregate = eventStoreDB.load(events.get(0).getAggregateId(), BankAccountAggregate.class);
            final var document = BankAccountMapper.bankAccountDocumentFromAggregate(aggregate);
            final var result = mongoRepository.save(document);
            log.info("(processEvents) saved document: {}", result);
        }
    }

    @Override
    public void when(Event event) {
        final var aggregateId = event.getAggregateId();
        log.info("(when) >>>>> aggregateId: {}", aggregateId);

        switch (event.getEventType()) {
            case BankAccountCreatedEvent.BANK_ACCOUNT_CREATED ->
                    handle(SerializerUtils.deserializeFromJsonBytes(event.getData(), BankAccountCreatedEvent.class));
            case EmailChangedEvent.EMAIL_CHANGED ->
                    handle(SerializerUtils.deserializeFromJsonBytes(event.getData(), EmailChangedEvent.class));
            case BalanceDepositedEvent.BALANCE_DEPOSITED ->
                    handle(SerializerUtils.deserializeFromJsonBytes(event.getData(), BalanceDepositedEvent.class));
            case BalanceCreditedEvent.BALANCE_CREDITED ->
                    handle(SerializerUtils.deserializeFromJsonBytes(event.getData(), BalanceCreditedEvent.class));
            case BalanceDebitedEvent.BALANCE_DEBITED ->
                    handle(SerializerUtils.deserializeFromJsonBytes(event.getData(), BalanceDebitedEvent.class));
            default -> log.error("unknown event type: {}", event.getEventType());
        }
    }

    private void handle(BankAccountCreatedEvent event) {
        log.info("(when) BankAccountCreatedEvent: {}, aggregateID: {}", event, event.getAggregateId());

        final var document = BankAccountDocument.builder()
                .aggregateId(event.getAggregateId())
                .email(event.getEmail())
                .balance(BigDecimal.valueOf(100))
                .credit(BigDecimal.valueOf(0))
                .debit(BigDecimal.valueOf(0))
                .creditLine(BigDecimal.valueOf(300))
                .overdraftLimit(BigDecimal.valueOf(300))
                .build();

        final var insert = mongoRepository.insert(document);
        log.info("(BankAccountCreatedEvent) insert: {}", insert);
    }

    private void handle(EmailChangedEvent event) {
        log.info("(when) EmailChangedEvent: {}, aggregateID: {}", event, event.getAggregateId());
        final var documentOptional = mongoRepository.findByAggregateId(event.getAggregateId());
        if (documentOptional.isEmpty())
            throw new BankAccountDocumentNotFoundException(event.getAggregateId());
        final var document = documentOptional.get();
        document.setEmail(event.getNewEmail());
        mongoRepository.save(document);
    }

    private void handle(BalanceDepositedEvent event) {
        log.info("(when) BalanceDepositedEvent: {}, aggregateID: {}", event, event.getAggregateId());
        final var documentOptional = mongoRepository.findByAggregateId(event.getAggregateId());
        if (documentOptional.isEmpty())
            throw new BankAccountDocumentNotFoundException(event.getAggregateId());
        final var document = documentOptional.get();
        final var newBalance = document.getBalance().add(event.getAmount());
        document.setBalance(newBalance);
        mongoRepository.save(document);
    }

    private void handle(BalanceCreditedEvent event) {
        log.info("(when) BalanceCreditedEvent: {}, aggregateID: {}", event, event.getAggregateId());
        final var documentOptional = mongoRepository.findByAggregateId(event.getAggregateId());
        if (documentOptional.isEmpty())
            throw new BankAccountDocumentNotFoundException(event.getAggregateId());
        final var document = documentOptional.get();
        final var newCredit = document.getCredit().add(event.getAmount());
        document.setCredit(newCredit);
        final var newBalance = document.getBalance().add(event.getAmount());
        document.setBalance(newBalance);
        mongoRepository.save(document);
    }

    private void handle(BalanceDebitedEvent event) {
        log.info("(when) BalanceDebitedEvent: {}, aggregateID: {}", event, event.getAggregateId());
        final var documentOptional = mongoRepository.findByAggregateId(event.getAggregateId());
        if (documentOptional.isEmpty())
            throw new BankAccountDocumentNotFoundException(event.getAggregateId());
        final var document = documentOptional.get();
        final var newDebit = document.getDebit().add(event.getAmount());
        document.setDebit(newDebit);
        final var newBalance = document.getBalance().subtract(event.getAmount());
        document.setBalance(newBalance);
        mongoRepository.save(document);
    }
}
