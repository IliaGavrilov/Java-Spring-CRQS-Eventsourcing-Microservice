package com.eventsourcing.bankAccount.queries;

import com.eventsourcing.bankAccount.dto.BankAccountResponseDTO;
import com.eventsourcing.bankAccount.dto.BankAccountTransactionsResponseDTO;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface BankAccountQueryService {
    BankAccountResponseDTO handle(GetBankAccountByIDQuery query);
    Page<BankAccountResponseDTO> handle(FindAllOrderByBalance query);

    Page<BankAccountResponseDTO> handle(FindAllBelowZeroOrderByBalance query);

    Page<BankAccountTransactionsResponseDTO> handle(UUID aggregateId, String date, int page, int size);
}
