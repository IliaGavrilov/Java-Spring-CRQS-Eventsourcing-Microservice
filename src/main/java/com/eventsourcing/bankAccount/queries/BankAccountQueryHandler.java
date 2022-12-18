package com.eventsourcing.bankAccount.queries;

import com.eventsourcing.bankAccount.domain.BankAccountAggregate;
import com.eventsourcing.bankAccount.domain.BankAccountDocument;
import com.eventsourcing.bankAccount.dto.BankAccountResponseDTO;
import com.eventsourcing.bankAccount.dto.BankAccountTransactionsResponseDTO;
import com.eventsourcing.bankAccount.repository.BankAccountMongoRepository;
import com.eventsourcing.es.Event;
import com.eventsourcing.es.EventRepository;
import com.eventsourcing.es.EventStoreDB;
import com.eventsourcing.es.SerializerUtils;
import com.eventsourcing.es.exceptions.AggregateNotFoundException;
import com.eventsourcing.mappers.BankAccountMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class BankAccountQueryHandler implements BankAccountQueryService {

    private final EventStoreDB eventStoreDB;
    private final BankAccountMongoRepository mongoRepository;
    private final EventRepository eventRepository;

    @Override
    public BankAccountResponseDTO handle(GetBankAccountByIDQuery query) {
        Optional<BankAccountDocument> optionalDocument = mongoRepository.findByAggregateId(query.aggregateID());
        if (optionalDocument.isPresent()) {
            return BankAccountMapper.bankAccountResponseDTOFromDocument(optionalDocument.get());
        }

        final var aggregate = eventStoreDB.load(query.aggregateID(), BankAccountAggregate.class);
        final var savedDocument = mongoRepository.save(BankAccountMapper.bankAccountDocumentFromAggregate(aggregate));
        log.info("(GetBankAccountByIDQuery) savedDocument: {}", savedDocument);

        final var bankAccountResponseDTO = BankAccountMapper.bankAccountResponseDTOFromAggregate(aggregate);
        log.info("(GetBankAccountByIDQuery) response: {}", bankAccountResponseDTO);
        return bankAccountResponseDTO;
    }

    @Override
    public Page<BankAccountResponseDTO> handle(FindAllOrderByBalance query) {
        return mongoRepository.findAll(PageRequest.of(query.page(), query.size(), Sort.by("balance")))
                .map(BankAccountMapper::bankAccountResponseDTOFromDocument);
    }

    @Override
    public Page<BankAccountResponseDTO> handle(FindAllBelowZeroOrderByBalance query) {
        PageRequest pageRequest = PageRequest.of(query.page(), query.size(), Sort.by("balance"));
        return mongoRepository.findByBalanceLessThan(new BigDecimal("0"), pageRequest)
                .map(BankAccountMapper::bankAccountResponseDTOFromDocument);
    }

    @Override
    public Page<BankAccountTransactionsResponseDTO> handle(String aggregateId, String date, int page, int size) {
        try {
            final var pageable = PageRequest.of(page, size);
            final var result = eventRepository.findAllByAggregateIdAndDate(aggregateId, date, pageable);
            log.info("Get account transactions result: {}", result);
            List<BankAccountTransactionsResponseDTO> transactions = new ArrayList<>();
            for (Event event : result.getContent()) {
                BankAccountResponseDTO aggregate = SerializerUtils.deserializeFromJsonBytes(event.getData(), BankAccountResponseDTO.class);
                BankAccountTransactionsResponseDTO transaction = new BankAccountTransactionsResponseDTO(
                        event.getId(), event.getVersion(), event.getEventType(), aggregate, event.getTimeStamp()
                );
                transactions.add(transaction);
            }
            return new PageImpl<>(transactions, pageable, result.getTotalElements());
        } catch (Exception e) {
            log.error("Error getting transactions for aggregateId {}", aggregateId, e);
            throw new AggregateNotFoundException(aggregateId);
        }
    }
}
