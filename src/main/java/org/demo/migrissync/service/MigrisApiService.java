package org.demo.migrissync.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.demo.migrissync.web.model.Institution;
import org.demo.migrissync.web.model.ServiceDescriptionResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class MigrisApiService {

    private static final String BASE_URL = "https://www.migracija.lt";
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public MigrisApiService() {
        this.httpClient = HttpClients.custom()
                .setUserAgent("Mozilla/5.0")
                .useSystemProperties()  // This will use system proxy settings
                .build();
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    public ServiceDescriptionResponse getServiceDescription() throws IOException {
        HttpGet request = new HttpGet(BASE_URL + "/assets/i18n/en.json");
        String response = EntityUtils.toString(httpClient.execute(request).getEntity());
        return objectMapper.readValue(response, ServiceDescriptionResponse.class);
    }

    public List<Institution> getInstitutions(String serviceKey) throws IOException {
        HttpGet request = new HttpGet(BASE_URL + "/external/tickets/classif/" + serviceKey + "/institutions");
        String response = EntityUtils.toString(httpClient.execute(request).getEntity());
        return Arrays.asList(objectMapper.readValue(response, Institution[].class));
    }

    public List<LocalDateTime> getDateSlots(String serviceKey, String institutionKey) throws IOException {
        HttpGet request = new HttpGet(BASE_URL + "/external/tickets/classif/" + serviceKey + "/" + institutionKey + "/dates");
        String response = EntityUtils.toString(httpClient.execute(request).getEntity());
        return Arrays.asList(objectMapper.readValue(response, LocalDateTime[].class));
    }

    public List<LocalDateTime> getTimeSlots(String serviceKey, String institutionKey, LocalDate date) throws IOException {
        HttpGet request = new HttpGet(BASE_URL +
          "/external/tickets/classif/" + serviceKey + "/" + institutionKey + "/" + date.toString() +
          "/times?t=" + Instant.now().getEpochSecond());
        String response = EntityUtils.toString(httpClient.execute(request).getEntity());
        return Arrays.asList(objectMapper.readValue(response, LocalDateTime[].class));
    }
}
