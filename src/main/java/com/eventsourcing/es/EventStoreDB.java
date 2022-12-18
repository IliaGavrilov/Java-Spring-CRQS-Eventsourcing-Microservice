package com.eventsourcing.es;


import java.util.List;
import java.util.UUID;

public interface EventStoreDB {

    void saveEvents(final List<Event> events);

    List<Event> loadEvents(final UUID aggregateId, long version);

    <T extends AggregateRoot> void save(final T aggregate);

    <T extends AggregateRoot> T load(final UUID aggregateId, final Class<T> aggregateType);

    Boolean exists(final UUID aggregateId);
}
