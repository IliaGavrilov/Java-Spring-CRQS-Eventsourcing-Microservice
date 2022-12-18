package com.eventsourcing.bankAccount.exceptions;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String aggregateID) {
        super(aggregateID);
    }
}