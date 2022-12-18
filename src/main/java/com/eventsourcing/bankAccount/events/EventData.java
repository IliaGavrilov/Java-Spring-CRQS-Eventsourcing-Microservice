package com.eventsourcing.bankAccount.events;


import com.eventsourcing.es.Event;
import com.eventsourcing.es.BaseEvent;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class EventData<T extends Event<T, ?>> extends BaseEvent {

    private BigDecimal amount;

    @Builder
    public EventData(String aggregateId, BigDecimal amount, String email, BigDecimal balance, BigDecimal debit, BigDecimal credit, BigDecimal creditLine, BigDecimal overdraftLimit) {
        super(aggregateId, email, balance, debit, credit, creditLine, overdraftLimit);
        this.amount = amount;
    }
}

