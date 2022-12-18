package com.eventsourcing.es;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public abstract class BaseEvent {
    protected String aggregateId;
    protected String email;
    protected BigDecimal balance;
    protected BigDecimal debit;
    protected BigDecimal credit;
    protected BigDecimal creditLine;
    protected BigDecimal overdraftLimit;

    public BaseEvent(String aggregateId) {
        this.aggregateId = aggregateId;
    }

    public BaseEvent(String aggregateId, String email, BigDecimal balance, BigDecimal debit, BigDecimal credit, BigDecimal creditLine, BigDecimal overdraftLimit) {
        this.aggregateId = aggregateId;
        this.email = email;
        this.balance = balance;
        this.debit = debit;
        this.credit = credit;
        this.creditLine = creditLine;
        this.overdraftLimit = overdraftLimit;
    }
}