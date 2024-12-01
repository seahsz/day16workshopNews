package vttp.ssf.day16workshop.services;

import java.io.StringReader;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonReader;

@Service
public class IndexService {

    public static final String GET_COUNTRY_NAME_AND_CCA2_URL = "https://restcountries.com/v3.1/all?fields=name,cca2";

    public SortedMap<String, String> getCountryNameAndCca2() {

        // Configure the request
        RequestEntity<Void> req = RequestEntity
                .get(GET_COUNTRY_NAME_AND_CCA2_URL)
                .accept(MediaType.APPLICATION_JSON)
                .build();

        // Create REST template
        RestTemplate template = new RestTemplate();

        // Get the response
        ResponseEntity<String> response;

        try {
            response = template.exchange(req, String.class);

            // Extract the payload
            String payload = response.getBody();
            JsonReader reader = Json.createReader(new StringReader(payload));
            JsonArray result = reader.readArray();

            Map<String, String> countryNameCca2Map = result.stream()
                    .map(obj -> {return obj.asJsonObject();})
                    .collect(Collectors.toMap(
                        obj -> obj.getJsonObject("name").getString("common"),
                        obj -> obj.getString("cca2")));

            SortedMap<String, String> sortedCountryNameCca2Map = new TreeMap<>(countryNameCca2Map);

            return sortedCountryNameCca2Map;

        } catch (Exception ex) {
            ex.printStackTrace();
            return new TreeMap<>(Map.of());
        }

    }
    
}
