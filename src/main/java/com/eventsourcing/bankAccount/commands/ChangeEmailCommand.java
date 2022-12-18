package com.eventsourcing.bankAccount.commands;

import java.util.UUID;

public record ChangeEmailCommand(UUID aggregateID, String newEmail) {
}
