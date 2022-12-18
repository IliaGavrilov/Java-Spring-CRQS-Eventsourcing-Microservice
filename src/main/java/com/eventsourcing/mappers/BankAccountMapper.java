package com.eventsourcing.mappers;

import com.eventsourcing.bankAccount.domain.BankAccountAggregate;
import com.eventsourcing.bankAccount.domain.BankAccountDocument;
import com.eventsourcing.bankAccount.dto.BankAccountResponseDTO;

public final class BankAccountMapper {

    private BankAccountMapper() {
    }


    public static BankAccountResponseDTO bankAccountResponseDTOFromAggregate(BankAccountAggregate bankAccountAggregate) {
        return new BankAccountResponseDTO(
                bankAccountAggregate.getId(),
                bankAccountAggregate.getEmail(),
                bankAccountAggregate.getAmount(),
                bankAccountAggregate.getBalance(),
                bankAccountAggregate.getCredit(),
                bankAccountAggregate.getDebit(),
                bankAccountAggregate.getCreditLine(),
                bankAccountAggregate.getOverdraftLimit()
        );
    }

    public static BankAccountResponseDTO bankAccountResponseDTOFromDocument(BankAccountDocument bankAccountDocument) {
        return new BankAccountResponseDTO(
                bankAccountDocument.getAggregateId(),
                bankAccountDocument.getEmail(),
                null,
                bankAccountDocument.getBalance(),
                bankAccountDocument.getCredit(),
                bankAccountDocument.getDebit(),
                bankAccountDocument.getCreditLine(),
                bankAccountDocument.getOverdraftLimit()
        );
    }

    public static BankAccountDocument bankAccountDocumentFromAggregate(BankAccountAggregate bankAccountAggregate) {
        return BankAccountDocument.builder()
                .aggregateId(bankAccountAggregate.getId())
                .email(bankAccountAggregate.getEmail())
                .balance(bankAccountAggregate.getBalance())
                .credit(bankAccountAggregate.getCredit())
                .debit(bankAccountAggregate.getDebit())
                .creditLine(bankAccountAggregate.getCreditLine())
                .overdraftLimit(bankAccountAggregate.getOverdraftLimit())
                .build();
    }
}