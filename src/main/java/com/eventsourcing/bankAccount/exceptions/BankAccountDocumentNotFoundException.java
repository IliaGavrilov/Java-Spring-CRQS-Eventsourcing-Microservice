package com.eventsourcing.bankAccount.exceptions;

import java.util.UUID;

public class BankAccountDocumentNotFoundException extends RuntimeException {
    public BankAccountDocumentNotFoundException() {
    }

    public BankAccountDocumentNotFoundException(UUID id) {
        super("bank account document not found id:" + id);
    }
}
