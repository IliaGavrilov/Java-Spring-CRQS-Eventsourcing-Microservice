package com.eventsourcing.bankAccount.events;

import com.eventsourcing.bankAccount.domain.BankAccountAggregate;
import com.eventsourcing.es.BaseEvent;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BankAccountCreatedEvent extends BaseEvent {
    public static final String BANK_ACCOUNT_CREATED = "BANK_ACCOUNT_CREATED";
    public static final String AGGREGATE_TYPE = BankAccountAggregate.AGGREGATE_TYPE;

    private BigDecimal amount;
    private String email;

//    @Builder
//    public BankAccountCreatedEvent(String aggregateId, String email) {
//        super(aggregateId);
//        this.email = email;
//    }

    @Builder
    public BankAccountCreatedEvent(String aggregateId, BigDecimal amount, String email, BigDecimal balance, BigDecimal debit, BigDecimal credit, BigDecimal creditLine, BigDecimal overdraftLimit) {
        super(aggregateId, email, balance, debit, credit, creditLine, overdraftLimit);
        this.email = email;
        this.amount = amount;
    }


}