package com.eventsourcing.es;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
public abstract class BaseEvent {
    protected UUID aggregateId;
    protected String email;
    protected BigDecimal balance;
    protected BigDecimal debit;
    protected BigDecimal credit;
    protected BigDecimal creditLine;
    protected BigDecimal overdraftLimit;

    public BaseEvent(UUID aggregateId) {
        this.aggregateId = aggregateId;
    }

    public BaseEvent(UUID aggregateId, String email, BigDecimal balance, BigDecimal debit, BigDecimal credit, BigDecimal creditLine, BigDecimal overdraftLimit) {
        this.aggregateId = aggregateId;
        this.email = email;
        this.balance = balance;
        this.debit = debit;
        this.credit = credit;
        this.creditLine = creditLine;
        this.overdraftLimit = overdraftLimit;
    }
}