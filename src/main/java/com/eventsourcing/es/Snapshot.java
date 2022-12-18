package com.eventsourcing.es;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "snapshots")
public class Snapshot {

    @Id
    @Column(name = "snapshot_id")
    private UUID id;

    @Column(name="aggregate_id", nullable = false)
    private UUID aggregateId;

    @Column(name="aggregate_type", nullable = false)
    private String aggregateType;

    @Column(name="data", nullable = false)
    private byte[] data;

    @Column(name="metadata", nullable = false)
    private byte[] metaData;

    @Column(name="version", nullable = false)
    private long version;

    @Column(name="timestamp", nullable = false)
    private LocalDateTime timeStamp;

    @Override
    public String toString() {
        return "Snapshot{" +
                "snapshot_id=" + id.toString() +
                ", aggregate_id='" + aggregateId.toString() + '\'' +
                ", aggregate_type='" + aggregateType + '\'' +
                ", data=" + data.length + " bytes" +
                ", version=" + version +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
