package net.joaopeixoto.geode.client;

import java.util.Collection;
import java.util.List;

import net.joaopeixoto.geode.common.model.DistanceResult;
import net.joaopeixoto.geode.common.model.Metric;
import org.springframework.data.gemfire.function.annotation.OnRegion;



@OnRegion(region = "Metric")
public interface DistanceFunction {
    Collection<DistanceResult> distance(List<Metric> metrics);
}
