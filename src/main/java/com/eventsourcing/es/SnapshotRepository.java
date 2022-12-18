package com.eventsourcing.es;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SnapshotRepository extends JpaRepository<Snapshot, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO snapshots (aggregate_id, aggregate_type, data, metadata, version, timestamp) VALUES (:aggregate_id, :aggregate_type, :data, :metadata, :version, now()) ON CONFLICT (aggregate_id) DO UPDATE SET data = :data, version = :version, timestamp = now()", nativeQuery = true)
    void insertOrUpdateSnapshot(String aggregate_id, String aggregate_type, byte[] data, byte[] metadata, long version);

    Optional<Snapshot> findByAggregateId(String aggregateId);
}
