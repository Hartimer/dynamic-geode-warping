package net.joaopeixoto.geode.client;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.query.FunctionDomainException;
import com.gemstone.gemfire.cache.query.NameResolutionException;
import com.gemstone.gemfire.cache.query.QueryInvocationTargetException;
import com.gemstone.gemfire.cache.query.SelectResults;
import com.gemstone.gemfire.cache.query.TypeMismatchException;
import javax.annotation.Resource;
import net.joaopeixoto.geode.common.model.DistanceResult;
import net.joaopeixoto.geode.common.model.Metric;
import net.joaopeixoto.geode.common.model.MetricKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class TestService {

    private static final Logger log = LoggerFactory.getLogger(TestService.class);
    private static final double MAX_DISTANCE = 1D;

    @Resource(name = "Metric")
    private Region<MetricKey, Metric> region;

    @Autowired
    private DistanceFunction distanceFunction;

    @Autowired
    private RestClient restClient;

    @RequestMapping(value = "/data", method = RequestMethod.POST)
    public void postData(@RequestBody Map<String, String> body) {
        Metric metric = new Metric(Long.parseLong(body.get("t")),
                                   new BigDecimal(body.get("value")));
        log.debug("Saving {}", metric);
        region.put(new MetricKey(metric), metric);

        /**
         * We only trigger pattern recognition if we see a value above a certain threshold.
         */
        if (metric.getValue().compareTo(BigDecimal.TEN) < 1) return;

        try {
            SelectResults<Metric> result = region.query(
                    "SELECT * FROM /Metric.values m WHERE m.getTimestamp() < " +
                            metric.getTimestamp()  + "L " +
                            "ORDER BY m.getTimestamp() DESC LIMIT 12");
            ArrayList<Metric> metrics = new ArrayList<>(result.asList());
            metrics.add(metric);

            // Make sure the metrics are sorted by time
            Collections.sort(metrics);

            DistanceResult resultDistance = null;

            /**
             * Execute the server-side function in parallel! And go through the results
             */
            for (DistanceResult distanceResult : distanceFunction.distance(metrics)) {
                if (resultDistance == null ||
                        (distanceResult != null && resultDistance.compareTo(distanceResult) == -1)) {
                    resultDistance = distanceResult;
                }
            }
            if (resultDistance != null && resultDistance.getDistance() <= MAX_DISTANCE) {
                restClient.sendResultToUi(resultDistance);
            }
        }
        catch (FunctionDomainException | TypeMismatchException | QueryInvocationTargetException | NameResolutionException e) {
            log.error(e.getMessage(), e);
        }
    }
}
