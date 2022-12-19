package com.eventsourcing.bankAccount.controller;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventsourcing.bankAccount.commands.*;
import com.eventsourcing.bankAccount.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/account")
@Slf4j
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountCommandService commandService;

    @PostMapping(path = "/create")
    public String createBankAccount(@Valid @RequestBody CreateBankAccountRequestDTO dto) {
        final UUID id = commandService.handle(new CreateBankAccountCommand(UUID.randomUUID(), dto.email()));
        log.info("Created bank account id: {}", id);
        return id.toString();
    }

    @PostMapping(path = "/deposit/{aggregateId}")
    public void depositAmount(@Valid @RequestBody DepositAmountRequestDTO dto, @PathVariable UUID aggregateId) {
        commandService.handle(new DepositAmountCommand(aggregateId, dto.amount()));
    }

    @PostMapping(path = "/credit/{aggregateId}")
    public void creditAmount(@Valid @RequestBody CreditAmountRequestDTO dto, @PathVariable UUID aggregateId) {
        commandService.handle(new CreditAmountCommand(aggregateId, dto.amount()));
    }

    @PostMapping(path = "/debit/{aggregateId}")
    public void debitAmount(@Valid @RequestBody DebitAmountRequestDTO dto, @PathVariable UUID aggregateId) {
        commandService.handle(new DebitAmountCommand(aggregateId, dto.amount()));
    }

    @PostMapping(path = "/email/{aggregateId}")
    public void changeEmail(@Valid @RequestBody ChangeEmailRequestDTO dto, @PathVariable UUID aggregateId) {
        commandService.handle(new ChangeEmailCommand(aggregateId, dto.newEmail()));
    }
}
