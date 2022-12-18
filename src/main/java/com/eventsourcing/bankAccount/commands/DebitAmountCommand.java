package com.eventsourcing.bankAccount.commands;

import java.math.BigDecimal;

public record DebitAmountCommand(String aggregateID, BigDecimal amount) {
}
