package com.eventsourcing.bankAccount.commands;

import java.math.BigDecimal;
import java.util.UUID;

public record DepositAmountCommand(UUID aggregateID, BigDecimal amount) {
}
