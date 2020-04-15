package tools;//package utilities;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class Request {

    public static String get(String url) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);

        try (CloseableHttpResponse response = httpClient.execute(request)) {

            // Get HttpResponse Status
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);

            httpClient.close();
            return  result;
        }
        catch (Exception e) {
            System.out.println(e.toString());
            httpClient.close();
        }
        return "Not Found";
    }
}
