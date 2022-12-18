package com.eventsourcing.es;


import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "events")
public class Event {

    @Id
    @Column(name="event_id", nullable = false)
    private UUID id;

    @Column(name="aggregate_id", nullable = false)
    private String aggregateId;

    @Column(name="event_type", nullable = false)
    private String eventType;

    @Column(name="aggregate_type", nullable = false)
    private String aggregateType;

    @Column(name="version", nullable = false)
    private long version;

    @Column(name="data", nullable = false)
    private byte[] data;

    @Column(name="metadata", nullable = false)
    private byte[] metaData;

    @Column(name="timestamp", nullable = false)
    private LocalDateTime timeStamp;

    public Event(String eventType, String aggregateType) {
        this.id = UUID.randomUUID();
        this.eventType = eventType;
        this.aggregateType = aggregateType;
        this.timeStamp = LocalDateTime.now();
    }


    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", aggregateId='" + aggregateId + '\'' +
                ", eventType='" + eventType + '\'' +
                ", aggregateType='" + aggregateType + '\'' +
                ", version=" + version + '\'' +
                ", timeStamp=" + timeStamp + '\'' +
                ", data=" + new String(data) + '\'' +
                '}';
    }
}
