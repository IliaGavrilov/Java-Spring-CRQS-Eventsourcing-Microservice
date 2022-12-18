package com.eventsourcing.bankAccount.commands;

import java.math.BigDecimal;

public record CreditAmountCommand(String aggregateID, BigDecimal amount) {
}
