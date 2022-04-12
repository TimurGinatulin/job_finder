package ru.geekbrains.job_finder.headHinterService.repositories;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import ru.geekbrains.job_finder.cor_lib.models.HHResponse;
import ru.geekbrains.job_finder.routing_lib.dtos.HHUserSummary;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Repository
public class HeadHunterApiRepository {
    private final String clientSecret;
    private final String clientID;
    private final RestTemplate restTemplate;

    public HeadHunterApiRepository(RestTemplate restTemplate) throws IOException {
        this.restTemplate = restTemplate;
        this.clientID = "V3TFUAKUROVOQRNDLDVHRRMM5NF54OL53R50109P96HN55AD1D07F3Q7SBIMO2IP";
        byte[] storageBytes = Files.readAllBytes(Path.of("/home/tim/hh.key"));
        StringBuilder stringBuilder = new StringBuilder();
        for (byte aByte : storageBytes) {
            stringBuilder.append((char) aByte);
        }
        String[] storageElement = stringBuilder.toString().split(String.valueOf((char) 10));
        String targetField;
        targetField = Arrays.stream(storageElement)
                .filter(element -> element.contains("clientSecret="))
                .findFirst()
                .orElse(null);
        if (targetField == null)
            throw new IOException("Key don't contain client secret");
        this.clientSecret = targetField.substring(13);
    }

    public HHResponse getHHTokens(String code) {
        String url = "https://hh.ru/oauth/token?grant_type=authorization_code&client_id=" + clientID
                + "&client_secret=" + clientSecret + "&code=" + code;
        return restTemplate.postForObject(url, null, HHResponse.class);
    }

    public HHUserSummary getHHUserPropertiesByCode(String code) {
        HHResponse hhTokens = getHHTokens(code);
        String url = "https://api.hh.ru/me";
        HttpHeaders headers = headersForHHServices(hhTokens.getAccess_token());
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        JSONObject jsonSummary = new JSONObject(response.getBody());
        return HHUserSummary
                .builder()
                .id(jsonSummary.getLong("id"))
                .firstName(jsonSummary.get("first_name").toString())
                .middleName(jsonSummary.get("middle_name").toString())
                .lastName(jsonSummary.getString("last_name"))
                .email(jsonSummary.get("email").toString())
                .accessToken(hhTokens.getAccess_token())
                .refreshToken(hhTokens.getRefresh_token())
                .build();
    }

    private HttpHeaders headersForHHServices(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/json");
        headers.add("Connection", "keep-alive");
        headers.add("Accept", "*/*");
        return headers;
    }

    public static void main(String[] args) throws IOException {
        HeadHunterApiRepository repository = new HeadHunterApiRepository(new RestTemplate());
        HHUserSummary wwww = repository.getHHUserPropertiesByCode("Q7URPVUTD4KVP6THM86DFK6T2PGVFB68N4A713TH7B2SETM67A9GEE6QTTN4LMD9");
        System.out.println(wwww);
    }
}
