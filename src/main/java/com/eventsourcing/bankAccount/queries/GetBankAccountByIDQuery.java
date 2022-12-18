package com.eventsourcing.bankAccount.queries;

import java.util.UUID;

public record GetBankAccountByIDQuery(UUID aggregateID) {
}
