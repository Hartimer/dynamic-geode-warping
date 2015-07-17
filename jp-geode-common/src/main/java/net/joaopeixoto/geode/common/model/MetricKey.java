package net.joaopeixoto.geode.common.model;

import java.io.Serializable;

import com.gemstone.gemfire.cache.EntryOperation;
import com.gemstone.gemfire.cache.PartitionResolver;



public class MetricKey implements PartitionResolver, Serializable {

    private Metric metric;

    public MetricKey(Metric metric) {
        this.metric = metric;
    }

    @Override public String toString() {
        return String.valueOf(metric.getTimestamp());
    }

    @Override public Object getRoutingObject(EntryOperation entryOperation) {
        return (long) (metric.getTimestamp() / 20000);
    }

    @Override public String getName() {
        return getClass().getName();
    }

    @Override public void close() {
    }
}
