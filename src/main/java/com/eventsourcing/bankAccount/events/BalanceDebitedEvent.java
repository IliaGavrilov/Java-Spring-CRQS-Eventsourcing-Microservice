package com.eventsourcing.bankAccount.events;

import com.eventsourcing.bankAccount.domain.BankAccountAggregate;
import com.eventsourcing.es.BaseEvent;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class BalanceDebitedEvent extends BaseEvent {
    public static final String BALANCE_DEBITED = "BALANCE_DEBITED";
    public static final String AGGREGATE_TYPE = BankAccountAggregate.AGGREGATE_TYPE;

    private BigDecimal amount;

    @Builder
    public BalanceDebitedEvent(UUID aggregateId, BigDecimal amount, String email, BigDecimal balance, BigDecimal debit, BigDecimal credit, BigDecimal creditLine, BigDecimal overdraftLimit) {
        super(aggregateId, email, balance, debit, credit, creditLine, overdraftLimit);
        this.amount = amount;
    }
}
