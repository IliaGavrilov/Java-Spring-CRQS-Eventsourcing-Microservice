package com.eventsourcing.bankAccount.domain;


import com.eventsourcing.bankAccount.events.*;
import com.eventsourcing.es.AggregateRoot;
import com.eventsourcing.es.Event;
import com.eventsourcing.es.SerializerUtils;
import com.eventsourcing.es.exceptions.InvalidEventTypeException;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BankAccountAggregate extends AggregateRoot {

    public static final String AGGREGATE_TYPE = "BankAccountAggregate";

    public BankAccountAggregate(UUID id) {
        super(id, AGGREGATE_TYPE);
    }

    private String email;
    private BigDecimal amount;
    private BigDecimal balance;
    private BigDecimal debit;
    private BigDecimal credit;
    private BigDecimal creditLine;
    private BigDecimal overdraftLimit;


    @Override
    public void when(Event event) {
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
            default -> throw new InvalidEventTypeException(event.getEventType());
        }
    }

    private void handle(final BankAccountCreatedEvent event) {
        this.email = event.getEmail();
        this.balance = BigDecimal.valueOf(100);
        this.debit = BigDecimal.valueOf(0);
        this.credit = BigDecimal.valueOf(0);
        this.creditLine = BigDecimal.valueOf(300);
        this.overdraftLimit = BigDecimal.valueOf(300);
    }

    private void handle(final EmailChangedEvent event) {
        this.email = event.getNewEmail();
    }

    private void handle(final BalanceDepositedEvent event) {
        this.balance = this.balance.add(event.getAmount());
    }

    private void handle(final BalanceCreditedEvent event) {
        this.credit = this.credit.add(event.getAmount());
        this.balance = this.balance.add(event.getAmount());
    }

    private void handle(final BalanceDebitedEvent event) {
        this.debit = this.debit.add(event.getAmount());
        this.balance = this.balance.subtract(event.getAmount());
    }

    public void createBankAccount(String email) {
        final var data = BankAccountCreatedEvent.builder()
                .aggregateId(id)
                .email(email)
                .amount(BigDecimal.valueOf(0))
                .balance(BigDecimal.valueOf(100))
                .debit(BigDecimal.valueOf(0))
                .credit(BigDecimal.valueOf(0))
                .creditLine(BigDecimal.valueOf(300))
                .overdraftLimit(BigDecimal.valueOf(300))
                .build();

        final byte[] dataBytes = SerializerUtils.serializeToJsonBytes(data);
        final var event = this.createEvent(BankAccountCreatedEvent.BANK_ACCOUNT_CREATED, dataBytes, null);
        this.apply(event);
    }

    public void changeEmail(String email) {
        final var data = EmailChangedEvent.builder().aggregateId(id).newEmail(email).build();
        final byte[] dataBytes = SerializerUtils.serializeToJsonBytes(data);
        final var event = this.createEvent(EmailChangedEvent.EMAIL_CHANGED, dataBytes, null);
        apply(event);
    }

    public <T extends Event<T, ?>> void updateBalance(BigDecimal amount, BankAccountAggregate aggregate, String eventType) {
        final var data = EventData.builder()
                .aggregateId(id)
                .amount(amount)
                .email(aggregate.getEmail())
                .balance(aggregate.getBalance())
                .debit(aggregate.getDebit())
                .credit(aggregate.getCredit())
                .creditLine(aggregate.getCreditLine())
                .overdraftLimit(aggregate.getOverdraftLimit())
                .build();
        final byte[] dataBytes = SerializerUtils.serializeToJsonBytes(data);
        final var event = this.createEvent(eventType, dataBytes, null);
        apply(event);
    }

    @Override
    public String toString() {
        return "BankAccountAggregate{" +
                "email='" + email + '\'' +
                ", balance=" + balance +
                ", debit=" + debit +
                ", credit=" + credit +
                ", creditLine=" + creditLine +
                ", overdraftLimit=" + overdraftLimit +
                ", id='" + id.toString() + '\'' +
                ", type='" + type + '\'' +
                ", version=" + version +
                ", changes=" + changes.size() +
                '}';
    }
}