package com.eventsourcing.bankAccount.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreditAmountRequestDTO(@Min(value = 0, message = "minimal amount is 0") @NotNull BigDecimal amount) {
}
