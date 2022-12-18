package com.eventsourcing.bankAccount.commands;

import java.util.UUID;

public interface BankAccountCommandService {
    UUID handle(CreateBankAccountCommand command);

    void handle(ChangeEmailCommand command);

    void handle(DepositAmountCommand command);

    void handle(CreditAmountCommand command);

    void handle(DebitAmountCommand command);
}
