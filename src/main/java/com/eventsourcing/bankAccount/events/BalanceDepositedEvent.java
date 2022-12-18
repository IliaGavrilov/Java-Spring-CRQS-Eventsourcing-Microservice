package com.eventsourcing.bankAccount.events;

import com.eventsourcing.bankAccount.domain.BankAccountAggregate;
import com.eventsourcing.es.BaseEvent;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceDepositedEvent extends BaseEvent {
    public static final String BALANCE_DEPOSITED = "BALANCE_DEPOSITED";
    public static final String AGGREGATE_TYPE = BankAccountAggregate.AGGREGATE_TYPE;

    private BigDecimal amount;

    @Builder
    public BalanceDepositedEvent(String aggregateId, BigDecimal amount, String email, BigDecimal balance, BigDecimal debit, BigDecimal credit, BigDecimal creditLine, BigDecimal overdraftLimit) {
        super(aggregateId, email, balance, debit, credit, creditLine, overdraftLimit);
        this.amount = amount;
    }
}