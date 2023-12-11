package com.github.parser.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);

    public String call(String location) {

        String res = null;
        try {
            URI uri = URI.create(location);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                res = response.body();
            } else {
                LOGGER.warn("Error fetching {} statusCode:{}", location, response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        return res;
    }

    public boolean isUrl(String url) {
        return url != null && url.startsWith("http");
    }

}
