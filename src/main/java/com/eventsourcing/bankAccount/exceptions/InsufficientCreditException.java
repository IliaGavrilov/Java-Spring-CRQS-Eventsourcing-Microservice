package com.eventsourcing.bankAccount.exceptions;

public class InsufficientCreditException extends RuntimeException {
    public InsufficientCreditException(String aggregateID) {
        super(aggregateID);
    }
}