package com.eventsourcing.bankAccount.controller;


import com.eventsourcing.bankAccount.dto.BankAccountResponseDTO;
import com.eventsourcing.bankAccount.dto.BankAccountTransactionsResponseDTO;
import com.eventsourcing.bankAccount.queries.BankAccountQueryService;
import com.eventsourcing.bankAccount.queries.FindAllBelowZeroOrderByBalance;
import com.eventsourcing.bankAccount.queries.FindAllOrderByBalance;
import com.eventsourcing.bankAccount.queries.GetBankAccountByIDQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/account/info")
@Slf4j
@RequiredArgsConstructor
public class BankAccountInfoController {

    private final BankAccountQueryService queryService;

    @GetMapping("{aggregateId}")
    public BankAccountResponseDTO getBankAccount(@PathVariable String aggregateId) {
        final var result = queryService.handle(new GetBankAccountByIDQuery(aggregateId));
        log.info("Get bank account result: {}", result);
        return result;
    }

    @GetMapping("/balance")
    public Page<BankAccountResponseDTO> getAllOrderByBalance(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                             @RequestParam(name = "size", defaultValue = "10") Integer size) {

        final var result = queryService.handle(new FindAllOrderByBalance(page, size));
        log.info("Get all by balance page: {}, size: {}, result: {}", page, size, result.getContent());
        return result;
    }

    @GetMapping("/balance-in-red")
    public Page<BankAccountResponseDTO> getAllWithBalanceLowerZeroOrderByBalance(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                                 @RequestParam(name = "size", defaultValue = "10") Integer size) {

        final var result = queryService.handle(new FindAllBelowZeroOrderByBalance(page, size));
        log.info("Get all by balance below zero page: {}, size: {}, result: {}", page, size, result.getContent());
        return result;
    }

    @GetMapping("/transactions/{aggregateId}")
    public Page<BankAccountTransactionsResponseDTO> getTransactions(@PathVariable String aggregateId, @RequestParam String date,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "20") int size) {
        final var result = queryService.handle(aggregateId, date, page, size);
        log.info("Get account transactions page: {}, size: {}, result: {}",  page, size, result.getContent());
        return result;
    }
}
