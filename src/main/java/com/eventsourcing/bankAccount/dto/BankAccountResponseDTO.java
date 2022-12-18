package com.eventsourcing.bankAccount.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BankAccountResponseDTO(
        UUID aggregateId,
        String email,
        BigDecimal amount,
        BigDecimal balance,
        BigDecimal credit,
        BigDecimal debit,
        BigDecimal creditLine,
        BigDecimal overdraftLimit
) {
}