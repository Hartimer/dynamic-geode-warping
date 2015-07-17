package net.joaopeixoto.geode.common.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.gemfire.mapping.Region;



@Region
@Entity
public class Metric implements Serializable, Comparable<Metric> {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    private Long id;
    private Long timestamp;
    private BigDecimal value;

    @PersistenceConstructor
    public Metric(long timestamp, BigDecimal value) {
        this.id = timestamp;
        this.timestamp = timestamp;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public Metric setId(Long id) {
        this.id = id;
        return this;
    }

    public long getT() {
        return timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Metric setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public BigDecimal getValue() {
        return value;
    }

    public Metric setValue(BigDecimal value) {
        this.value = value;
        return this;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Metric metric = (Metric) o;

        return id.equals(metric.id);

    }

    @Override public int hashCode() {
        return id.hashCode();
    }

    @Override public int compareTo(Metric o) {
        return timestamp.compareTo(o.timestamp);
    }

    @Override public String toString() {
        return "Metric{" +
                "timestamp=" + timestamp +
                ", value=" + value +
                '}';
    }
}
