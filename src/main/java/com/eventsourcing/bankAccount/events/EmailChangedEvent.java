package com.eventsourcing.bankAccount.events;

import com.eventsourcing.bankAccount.domain.BankAccountAggregate;
import com.eventsourcing.es.BaseEvent;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
public class EmailChangedEvent extends BaseEvent {
    public static final String EMAIL_CHANGED = "EMAIL_CHANGED";
    public static final String AGGREGATE_TYPE = BankAccountAggregate.AGGREGATE_TYPE;

    private String newEmail;

    @Builder
    public EmailChangedEvent(UUID aggregateId, String newEmail) {
        super(aggregateId);
        this.newEmail = newEmail;
    }
}