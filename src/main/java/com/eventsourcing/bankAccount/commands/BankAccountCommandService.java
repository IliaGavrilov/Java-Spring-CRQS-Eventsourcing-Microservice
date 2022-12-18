package com.eventsourcing.bankAccount.commands;

public interface BankAccountCommandService {
    String handle(CreateBankAccountCommand command);

    void handle(ChangeEmailCommand command);

    void handle(DepositAmountCommand command);

    void handle(CreditAmountCommand command);

    void handle(DebitAmountCommand command);
}
