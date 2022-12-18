package com.eventsourcing.es;


import com.eventsourcing.es.exceptions.AggregateNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Repository
@RequiredArgsConstructor
@Slf4j
public class EventStore implements EventStoreDB {

    public static final int SNAPSHOT_FREQUENCY = 3;

    private final EventRepository eventRepository;
    private final SnapshotRepository snapshotRepository;
    private final EventBus eventBus;


    @Override
    @Transactional
    public <T extends AggregateRoot> void save(T aggregate) {
        final List<Event> aggregateEvents = new ArrayList<>(aggregate.getChanges());
        if (aggregate.getVersion() > 1) {
            this.handleConcurrency(aggregate.getId());
        }
        this.saveEvents(aggregate.getChanges());
        if (aggregate.getVersion() % SNAPSHOT_FREQUENCY == 0) {
            this.saveSnapshot(aggregate);
        }
        eventBus.publish(aggregateEvents);
        log.info("(save) saved aggregate: {}", aggregate);
    }

    @Override
    @Transactional(readOnly = true)
    public <T extends AggregateRoot> T load(UUID aggregateId, Class<T> aggregateType) {
        final Optional<Snapshot> snapshot = this.loadSnapshot(aggregateId);
        final var aggregate = this.getSnapshotFromClass(snapshot, aggregateId, aggregateType);
        final List<Event> events = this.loadEvents(aggregateId, aggregate.getVersion());
        events.forEach(event -> {
            aggregate.raiseEvent(event);
            log.info("raise event version: {}", event.getVersion());
        });
        if (aggregate.getVersion() == 0) throw new AggregateNotFoundException(aggregateId);
        log.info("(load) loaded aggregate: {}", aggregate);
        return aggregate;
    }

    @Override
    public void saveEvents(List<Event> events) {
        if (events.isEmpty()) return;
        final List<Event> changes = new ArrayList<>(events);
        if (changes.size() > 1) {
            this.eventsBatchInsert(changes);
            return;
        }
        final Event event = changes.get(0);
        eventRepository.saveEntity(event);
    }

    private void eventsBatchInsert(List<Event> events) {
        eventRepository.saveAllEntities(events);
    }

    @Override
    public List<Event> loadEvents(UUID aggregateId, long version) {
        return eventRepository.findByAggregateIdAndVersionGreaterThanEqualOrderByVersionAsc(aggregateId, version);
    }

    private <T extends AggregateRoot> void saveSnapshot(T aggregate) {
        aggregate.toSnapshot();
        final var snapshot = EventSourcingUtils.snapshotFromAggregate(aggregate);
        snapshotRepository.insertOrUpdateSnapshot(
                snapshot.getAggregateId(),
                snapshot.getAggregateType(),
                snapshot.getData(),
                snapshot.getMetaData(),
                snapshot.getVersion()
        );
        log.info("(saveSnapshot) updateResult: {}");
    }


    private void handleConcurrency(UUID aggregateId) {
        Optional<UUID> aggregateIdRec = eventRepository.findAllByAggregateId(aggregateId)
                    .stream()
                    .max(Comparator.comparingLong(Event::getVersion))
                    .map(Event::getAggregateId);
        if (aggregateIdRec.isPresent()) {
            log.info("(handleConcurrency) aggregateID for lock: {}", aggregateIdRec);
        } else {
            log.info("(handleConcurrency) EmptyResultDataAccessException: {}", aggregateId);
        }
        log.info("(handleConcurrency) aggregateID for lock: {}", aggregateIdRec.get());
    }

    private Optional<Snapshot> loadSnapshot(UUID aggregateId) {
        return snapshotRepository.findByAggregateId(aggregateId);
    }

    private <T extends AggregateRoot> T getAggregate(final UUID aggregateId, final Class<T> aggregateType) {
        try {
            return aggregateType.getConstructor(UUID.class).newInstance(aggregateId);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private <T extends AggregateRoot> T getSnapshotFromClass(Optional<Snapshot> snapshot, UUID aggregateId, Class<T> aggregateType) {
        if (snapshot.isEmpty()) {
            final var defaultSnapshot = EventSourcingUtils.snapshotFromAggregate(getAggregate(aggregateId, aggregateType));
            return EventSourcingUtils.aggregateFromSnapshot(defaultSnapshot, aggregateType);
        }
        return EventSourcingUtils.aggregateFromSnapshot(snapshot.get(), aggregateType);
    }


    @Override
    public Boolean exists(UUID aggregateId) {
        Optional<UUID> aggregateIdRec = eventRepository.findAggregateId(aggregateId);
        if (aggregateIdRec.isPresent()) {
            log.info("aggregate exists id: {}", aggregateIdRec.get());
            return true;
        } else {
            return false;
        }
    }
}
