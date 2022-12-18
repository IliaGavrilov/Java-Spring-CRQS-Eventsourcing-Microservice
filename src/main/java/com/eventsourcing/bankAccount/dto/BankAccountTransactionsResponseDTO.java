package com.eventsourcing.bankAccount.dto;


import java.time.LocalDateTime;
import java.util.UUID;

public record BankAccountTransactionsResponseDTO(
        UUID eventId,
        long version,
        String eventType,
        BankAccountResponseDTO data,
        LocalDateTime timeStamp
) {
}