package com.eventsourcing.bankAccount.queries;

import com.eventsourcing.bankAccount.dto.BankAccountResponseDTO;
import com.eventsourcing.bankAccount.dto.BankAccountTransactionsResponseDTO;
import org.springframework.data.domain.Page;

public interface BankAccountQueryService {
    BankAccountResponseDTO handle(GetBankAccountByIDQuery query);
    Page<BankAccountResponseDTO> handle(FindAllOrderByBalance query);

    Page<BankAccountResponseDTO> handle(FindAllBelowZeroOrderByBalance query);

    Page<BankAccountTransactionsResponseDTO> handle(String aggregateId, String date, int page, int size);
}
