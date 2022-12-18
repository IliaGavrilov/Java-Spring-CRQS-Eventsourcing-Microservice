package com.eventsourcing.bankAccount.commands;

import java.util.UUID;

public record CreateBankAccountCommand(UUID aggregateID, String email) {
}
