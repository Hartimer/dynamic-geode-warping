package net.joaopeixoto.geode.common.model;

import java.io.Serializable;
import java.util.List;



public class DistanceResult implements Comparable<DistanceResult>, Serializable {

    private static final long serialVersionUID = 1L;

    private List<Metric> metrics;
    private double distance;
    private int count;

    public DistanceResult(List<Metric> metrics, double distance, int count) {
        this.metrics = metrics;
        this.distance = distance;
        this.count = count;
    }

    public List<Metric> getMetrics() {
        return metrics;
    }

    public double getDistance() {
        return distance;
    }

    public int getCount() {
        return count;
    }

    @Override public int compareTo(DistanceResult o) {
        return new Double(distance).compareTo(o.distance);
    }

    @Override public String toString() {
        return "DistanceResult{" +
                "distance=" + distance +
                ", count=" + count +
                ", metrics=" + metrics +
                '}';
    }
}
