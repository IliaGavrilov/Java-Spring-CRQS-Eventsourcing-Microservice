package com.eventsourcing.es;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {

    default void saveEntity(Event event) {
        if (event.getId() == null) {
            event.setId(UUID.randomUUID());
        }
        save(event);
    }

    default void saveAllEntities(List<Event> events) {
        events.forEach(event -> {
            if (event.getId() == null) {
                event.setId(UUID.randomUUID());
            }
        });
        saveAll(events);
    }

    List<Event> findByAggregateIdAndVersionGreaterThanEqualOrderByVersionAsc(UUID aggregateId, long version);

    @Query("SELECT e FROM Event e WHERE e.aggregateId = :aggregateId")
    List<Event> findAllByAggregateId(@Param("aggregateId") UUID aggregateId);

    @Query("SELECT e.aggregateId FROM Event e WHERE e.aggregateId = :aggregateId")
    Optional<UUID> findAggregateId(@Param("aggregateId") UUID aggregateId);

    @Query(value = "SELECT * FROM public.events WHERE aggregate_id = :aggregateId AND date_trunc('day', timestamp) >= to_date(:date, 'YYYY-MM-DD')",
        countQuery = "SELECT count(*) FROM public.events WHERE aggregate_id = :aggregateId AND date_trunc('day', timestamp) >= to_date(:date, 'YYYY-MM-DD')",
        nativeQuery = true)
    Page<Event> findAllByAggregateIdAndDate(@Param("aggregateId") UUID aggregateId, @Param("date") String date, Pageable pageable);

}

