package com.eventsourcing.es;

import com.eventsourcing.es.exceptions.InvalidEventException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
@NoArgsConstructor
public abstract class AggregateRoot {

    protected UUID id;
    protected String type;
    protected long version;
    protected final List<Event> changes = new ArrayList<>();

    public AggregateRoot(final UUID id, final String aggregateType) {
        this.id = id;
        this.type = aggregateType;
    }


    public abstract void when(final Event event);

    public void apply(final Event event) {
        this.validateEvent(event);
        event.setAggregateType(this.type);
        when(event);
        changes.add(event);
        this.version++;
        event.setVersion(this.version);
    }

    public void raiseEvent(final Event event) {
        this.validateEvent(event);
        event.setAggregateType(this.type);
        when(event);
        this.version++;
    }

    public void clearChanges() {
        this.changes.clear();
    }

    public void toSnapshot() {
        this.clearChanges();
    }

    private void validateEvent(final Event event) {
        if (Objects.isNull(event) || !event.getAggregateId().equals(this.id))
            throw new InvalidEventException(event.toString());
    }

    protected Event createEvent(String eventType, byte[] data, byte[] metadata) {
        return Event.builder()
                .aggregateId(this.getId())
                .version(this.getVersion())
                .aggregateType(this.getType())
                .eventType(eventType)
                .data(Objects.isNull(data) ? new byte[]{} : data)
                .metaData(Objects.isNull(metadata) ? new byte[]{} : metadata)
                .timeStamp(LocalDateTime.now())
                .build();
    }


    @Override
    public String toString() {
        return "AggregateRoot{" +
                "id='" + id.toString() + '\'' +
                ", type='" + type + '\'' +
                ", version=" + version +
                ", changes=" + changes.size() +
                '}';
    }
}