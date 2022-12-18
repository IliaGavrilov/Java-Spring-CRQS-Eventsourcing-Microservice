package com.eventsourcing.es.exceptions;

import java.util.UUID;

public class AggregateNotFoundException extends RuntimeException {
    public AggregateNotFoundException() {
        super();
    }

    public AggregateNotFoundException(UUID aggregateID) {
        super("aggregate not found id:" + aggregateID);
    }
}
