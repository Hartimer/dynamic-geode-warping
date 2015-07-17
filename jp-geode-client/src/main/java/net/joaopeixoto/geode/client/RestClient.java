package net.joaopeixoto.geode.client;

import net.joaopeixoto.geode.common.model.DistanceResult;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;



@Component
public class RestClient {

    private static final String UI_EVENT_POINT_URL = "http://localhost:3000/ajax/ts-event";
    RestTemplate restTemplate = new RestTemplate();

    public void sendResultToUi(DistanceResult result) {
        restTemplate.postForObject(UI_EVENT_POINT_URL, result, Object.class);
    }
}
