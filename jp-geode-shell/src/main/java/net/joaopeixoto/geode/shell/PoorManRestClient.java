package net.joaopeixoto.geode.shell;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class PoorManRestClient {

    private static final String UI_DATA_POINT_URL = "http://localhost:3000/ajax/data-point";
    private static final String GEODE_DATA_POINT_URL = "http://localhost:8080/data";

    public static void sendPointToUi(DataPoint p) {
        String json = "{\"t\":" + p.t + ",\"value\":" + p.value + "}";
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(UI_DATA_POINT_URL);
        httppost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
        try {
            httpclient.execute(httppost);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                httpclient.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void sendPointToGeode(DataPoint p) {
        String json = "{\"t\":" + p.t + ",\"value\":" + p.value + "}";
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(GEODE_DATA_POINT_URL);
        httppost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
        try {
            httpclient.execute(httppost);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                httpclient.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
