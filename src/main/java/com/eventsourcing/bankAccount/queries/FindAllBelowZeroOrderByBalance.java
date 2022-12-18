package com.eventsourcing.bankAccount.queries;

public record FindAllBelowZeroOrderByBalance(int page, int size)  {
}