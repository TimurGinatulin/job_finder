package ru.geekbrains.job_finder.headHinterService.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import ru.geekbrains.job_finder.cor_lib.models.HHResponse;
import ru.geekbrains.job_finder.headHinterService.models.SendResumeRequestBody;
import ru.geekbrains.job_finder.headHinterService.models.Vacancy;
import ru.geekbrains.job_finder.headHinterService.models.entity.Filter;
import ru.geekbrains.job_finder.routing_lib.dtos.HHUserSummary;
import ru.geekbrains.job_finder.routing_lib.dtos.ResumeDTO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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

    public List<Vacancy> getFilteredVacancy(Filter filter) {
        List<Vacancy> vacancies = new LinkedList<>();
        StringBuilder requestBuilder = new StringBuilder("https://api.hh.ru/vacancies?");
        if (filter.getText() != null) {
            requestBuilder.append(String.format("text=%s&", filter.getText()));
        }
        if (filter.getSalary() != null) {
            requestBuilder.append(String.format("salary=%s&", filter.getSalary()));
        }
        if (filter.getCurrency() != null) {
            requestBuilder.append(String.format("currency=%s&", filter.getCurrency().getCode()));
        }
        if (filter.getArea() != null) {
            requestBuilder.append(String.format("area=%s&", filter.getArea().getId()));
        }
        if (filter.getSpecialization() != null) {
            requestBuilder.append(String.format("specialization=%s&", filter.getSpecialization().getId()));
        }
        if (filter.getEmployment() != null) {
            filter.getEmployment()
                    .forEach(employment -> requestBuilder.append(String.format("employment=%s&", employment.getId())));
        }
        if (filter.getExperience() != null) {
            filter.getExperience()
                    .forEach(experience -> requestBuilder.append(String.format("experience=%s&", experience.getId())));
        }
        if (filter.getSchedule() != null) {
            filter.getSchedule()
                    .forEach(schedule -> requestBuilder.append(String.format("schedule=%s&", schedule.getId())));
        }

        ResponseEntity<String> response =
                restTemplate.exchange(requestBuilder.toString(), HttpMethod.GET, null, String.class);
        JSONObject jsonVacancy = new JSONObject(response.getBody());
        JSONArray items = jsonVacancy.getJSONArray("items");
        for (Object o : items) {
            JSONObject item = (JSONObject) o;
            vacancies.add(Vacancy.builder()
                    .id(item.getLong("id"))
                    .name(item.getString("name"))
                    .build());
        }
        return vacancies;
    }

    public void sendResumeResponse(String accessToken, Vacancy vacancy, Filter filter) throws JsonProcessingException {
        HttpHeaders headers = headersForHHServices(accessToken);
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        SendResumeRequestBody body = SendResumeRequestBody
                .builder()
                .message(filter.getCoverLetter())
                .resumeId(filter.getSummaryId())
                .vacancyId(vacancy.getId())
                .build();
        HttpEntity<String> request = new HttpEntity<>(objectWriter.writeValueAsString(body), headers);
        restTemplate.patchForObject("https://api.hh.ru/negotiations", request, String.class);
    }

    public List<ResumeDTO> getResumeList(String accessToken) {
        List<ResumeDTO> resumeDTOS = new LinkedList<>();
        String url = "https://api.hh.ru/resumes/mine";
        HttpHeaders headers = headersForHHServices(accessToken);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        JSONObject jsonSummary = new JSONObject(response.getBody());
        JSONArray resumeArray = jsonSummary.getJSONArray("items");
        for (Object resume : resumeArray) {
            JSONObject resumeJson = (JSONObject) resume;
            resumeDTOS.add(ResumeDTO.builder()
                    .id(resumeJson.getString("id"))
                    .title(resumeJson.getString("title"))
                    .build());
        }
        return resumeDTOS;
    }
}
