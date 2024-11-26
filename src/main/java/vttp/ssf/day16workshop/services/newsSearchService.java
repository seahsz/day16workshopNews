package vttp.ssf.day16workshop.services;

import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.ssf.day16workshop.models.News;
import vttp.ssf.day16workshop.models.SearchParam;

@Service
public class NewsSearchService {

    public static final String GET_NEWS_URL = "https://newsapi.org/v2/top-headlines";

    public static final String API_KEY = "67bd827e5c4446799100adfee3418363";

    public static final String DEFAULT_IMG_URL = "https://giphy.com/gifs/theoffice-Rng7sDG4dkmyWev2qx";

    public List<News> getNews(SearchParam params) {

        // Construct the Search URL
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(GET_NEWS_URL)
                .queryParam("apiKey", API_KEY);

        if (params.getCountry() != null && !params.getCountry().isEmpty()) {
            builder.queryParam("country", params.getCountry());
        }

        if (params.getCategory() != null && !params.getCategory().isEmpty()) {
            builder.queryParam("category", params.getCategory());
        }

        if (params.getQuery() != null && !params.getQuery().isEmpty()) {
            builder.queryParam("q", params.getQuery());
        }

        String searchUrl = builder.toUriString();

        System.out.println(">>>>>>>>>>>>> search URL is: " +searchUrl);

        // Configure the request
        RequestEntity<Void> request = RequestEntity
                .get(searchUrl)
                .accept(MediaType.APPLICATION_JSON)
                .build();

        // Create REST Template
        RestTemplate template = new RestTemplate();

        // Get the response
        ResponseEntity<String> response;

        try {
            response = template.exchange(request, String.class);

            String payload = response.getBody();
            JsonReader reader = Json.createReader(new StringReader(payload));
            JsonObject result = reader.readObject();
            JsonArray articles = result.getJsonArray("articles");

            System.out.println(">>> Starting stream to populate List<News>");

            // Instantiate List to contain news
            List<News> newsList = articles.stream()
                    .map(obj -> {
                        return obj.asJsonObject();
                    })
                    .map(obj -> {
                        News n = new News();
                        n.setTitle(obj.getString("title"));

                        if (obj.containsKey("content") && obj.getString("content") != null) {
                            n.setContent(obj.getString("content"));
                            System.out.println(">Content");
                        }

                        if (obj.containsKey("urlToImage") && obj.getString("urlToImage") != null)
                            n.setImgUrl(obj.getString("urlToImage"));
                        else
                            n.setImgUrl(DEFAULT_IMG_URL);

                        if (obj.containsKey("url") && obj.getString("url") != null) {
                            n.setUrl(obj.getString("url"));
                            System.out.println(">>>>>>>>> URL");

                        }
                        return n;
                    })
                    .collect(Collectors.toList());

            System.out.println("News List size: " + newsList.size());

            return newsList;

        } catch (Exception ex) {
            ex.printStackTrace();
            return List.of();
        }

    }

}
