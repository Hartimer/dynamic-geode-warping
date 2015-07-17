/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.joaopeixoto.geode.server.functions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.fastdtw.dtw.DTW;
import com.fastdtw.dtw.TimeWarpInfo;
import com.fastdtw.timeseries.TimeSeries;
import com.fastdtw.timeseries.TimeSeriesBase;
import com.fastdtw.timeseries.TimeSeriesItem;
import com.fastdtw.timeseries.TimeSeriesPoint;
import com.fastdtw.util.Distances;
import com.gemstone.gemfire.cache.Region;
import javax.annotation.Resource;
import net.joaopeixoto.geode.common.model.DistanceResult;
import net.joaopeixoto.geode.common.model.Metric;
import net.joaopeixoto.geode.common.model.MetricKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.gemfire.function.annotation.GemfireFunction;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;



@Component
public class PatternFunctions {
    private static final Logger log = LoggerFactory.getLogger(PatternFunctions.class);
    private static final double THRESHOLD = 1D;

    @Resource(name = "Metric")
    private Region<MetricKey, Metric> metricRegion;

    /**
     * Replace here with your favorite algorithm
     */
    @GemfireFunction
    public DistanceResult distance(List<Metric> metrics) {
        Assert.notEmpty(metrics);
        Metric first = metrics.get(0);

        LinkedList<Metric> currentWindow = new LinkedList<>();
        LinkedList<Metric> matchedWindow = new LinkedList<>();
        double minDistance = THRESHOLD;
        int matchCount = 0;

        log.debug("Checking for patterns comparing with {}", metrics);

        List<Metric> localValues = new ArrayList<>(metricRegion.values());

        /**
         * Ensure the local values are ordered. {@link Region#values()} does not guarantee it.
         */
        Collections.sort(localValues);
        for (Metric metric : localValues) {

            // Ignore overlapping points or noise
            if (metric.getTimestamp() >= first.getTimestamp() ||
                    metric.getValue().compareTo(BigDecimal.TEN) < 1) {
                if (!currentWindow.isEmpty()) {
                    currentWindow.pop();
                }
                continue;
            }

            currentWindow.add(metric);
            if (currentWindow.size() > 13) {
                currentWindow.pop();
            }

            /**
             * We only compare windows the same size (for now)
             */
            if (currentWindow.size() == 13) {

                TimeWarpInfo compare =
                        DTW.compare(metricToSeries(currentWindow), metricToSeries(metrics),
                                    Distances.EUCLIDEAN_DISTANCE);
                if(compare.getDistance() <= 1D) {
                    matchCount++;
                    matchedWindow = new LinkedList<>(currentWindow);
                }
            }
        }

        if (matchCount > 0) {
            return new DistanceResult(matchedWindow, minDistance, matchCount);
        }
        return null;
    }

    private TimeSeries metricToSeries(List<Metric> metrics) {
        List<TimeSeriesItem> items = new ArrayList<>();
        for (Metric metric : metrics) {
            items.add(new TimeSeriesItem(metric.getTimestamp(), new TimeSeriesPoint(
                    new double[] { metric.getValue().doubleValue() })));
        }

        return new TimeSeriesBase(items);
    }
}
