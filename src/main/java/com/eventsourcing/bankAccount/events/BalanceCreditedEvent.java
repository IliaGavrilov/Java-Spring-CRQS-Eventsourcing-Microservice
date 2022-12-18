package com.eventsourcing.bankAccount.events;

import com.eventsourcing.bankAccount.domain.BankAccountAggregate;
import com.eventsourcing.es.BaseEvent;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceCreditedEvent extends BaseEvent {
    public static final String BALANCE_CREDITED = "BALANCE_CREDITED";
    public static final String AGGREGATE_TYPE = BankAccountAggregate.AGGREGATE_TYPE;

    private BigDecimal amount;

    @Builder
    public BalanceCreditedEvent(String aggregateId, BigDecimal amount, String email, BigDecimal balance, BigDecimal debit, BigDecimal credit, BigDecimal creditLine, BigDecimal overdraftLimit) {
        super(aggregateId, email, balance, debit, credit, creditLine, overdraftLimit);
        this.amount = amount;
    }
}
