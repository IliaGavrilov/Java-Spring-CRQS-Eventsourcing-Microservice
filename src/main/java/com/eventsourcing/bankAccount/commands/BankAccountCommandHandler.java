package com.eventsourcing.bankAccount.commands;


import com.eventsourcing.bankAccount.domain.BankAccountAggregate;
import com.eventsourcing.bankAccount.exceptions.InsufficientCreditException;
import com.eventsourcing.bankAccount.exceptions.InsufficientFundsException;
import com.eventsourcing.bankAccount.repository.BankAccountMongoRepository;
import com.eventsourcing.es.EventStoreDB;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Slf4j
@Service
public class BankAccountCommandHandler implements BankAccountCommandService {

    private final EventStoreDB eventStoreDB;
    private final BankAccountMongoRepository mongoRepository;

    public String handle(CreateBankAccountCommand command) {
        final var aggregate = new BankAccountAggregate(command.aggregateID());
        aggregate.createBankAccount(command.email());
        eventStoreDB.save(aggregate);

        log.info("(CreateBankAccountCommand) aggregate: {}", aggregate);
        return aggregate.getId();
    }

    @Override
    public void handle(ChangeEmailCommand command) {
        final var aggregate = eventStoreDB.load(command.aggregateID(), BankAccountAggregate.class);
        aggregate.changeEmail(command.newEmail());
        eventStoreDB.save(aggregate);
        log.info("(ChangeEmailCommand) aggregate: {}", aggregate);
    }

    @Override
    public void handle(DepositAmountCommand command) {
        final var aggregate = eventStoreDB.load(command.aggregateID(), BankAccountAggregate.class);
        aggregate.depositBalance(command.amount(), aggregate);
        eventStoreDB.save(aggregate);
        log.info("(DepositAmountCommand) aggregate: {}", aggregate);
    }

    @Override
    public void handle(CreditAmountCommand command) {
        checkPendingCreditExceedsOverdraftLimit(command);
        final var aggregate = eventStoreDB.load(command.aggregateID(), BankAccountAggregate.class);
        aggregate.creditBalance(command.amount(), aggregate);
        eventStoreDB.save(aggregate);
        log.info("(CreditAmountCommand) aggregate: {}", aggregate);
    }

    public void handle(DebitAmountCommand command) {
        checkPendingDebitExceedsOverdraftLimit(command);
        final var aggregate = eventStoreDB.load(command.aggregateID(), BankAccountAggregate.class);
        aggregate.debitBalance(command.amount(), aggregate);
        eventStoreDB.save(aggregate);
        log.info("(DebitAmountCommand) aggregate: {}", aggregate);
    }

    private void checkPendingCreditExceedsOverdraftLimit(CreditAmountCommand command) {
        mongoRepository.findByAggregateId(command.aggregateID())
                .ifPresent(document -> {
                    if (command.amount().add(document.getCredit()).compareTo(document.getCreditLine()) > 0) {
                        throw new InsufficientCreditException("Insufficient credit line to make credit payment of: " + command.amount());
                    }
                });

    }

    private void checkPendingDebitExceedsOverdraftLimit(DebitAmountCommand command) {
        mongoRepository.findByAggregateId(command.aggregateID())
                .ifPresent(document -> {
                    if (document.getBalance().compareTo(BigDecimal.ZERO) < 0
                            && document.getBalance().abs().add(command.amount()).compareTo(document.getOverdraftLimit()) > 0) {
                        throw new InsufficientFundsException("Insufficient funds to make debit payment of: " + command.amount());
                    }
                });
    }

}
