package vttp.ssf.day16workshop.services;

import java.io.StringReader;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.data.news.api.key}")
    public String API_KEY;

    public static final String DEFAULT_IMG_URL = "https://www.icegif.com/wp-content/uploads/2023/01/icegif-162.gif";

    private Logger logger = Logger.getLogger(NewsSearchService.class.getName());

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

            logger.info(">>> Starting stream to populate List<News>");

            logger.info("Number of articles retrieved: " + articles.size());

            /*
             * DEBUGGING NOTES: even if value is null, "value" == null -> FALSE
             * Need to either use JsonObject.isNull("key") or JsonObject.get("key") !=
             * JsonValue.NULL
             */

            // Instantiate List to contain news
            List<News> newsList = articles.stream()
                    .map(obj -> obj.asJsonObject()) // Convert to JsonObject
                    .map(obj -> {
                        News n = new News();

                        // Handle title
                        n.setTitle(!obj.isNull("title") ? obj.getString("title") : "");

                        // Handle content
                        n.setContent(!obj.isNull("content") ? obj.getString("content") : "");

                        // Handle image URL
                        n.setImgUrl(!obj.isNull("urlToImage") ? obj.getString("urlToImage") : DEFAULT_IMG_URL);

                        // Handle URL
                        n.setUrl(!obj.isNull("url") ? obj.getString("url") : "");

                        return n;
                    })
                    .collect(Collectors.toList());

            logger.info("News List size: " + newsList.size());

            return newsList;

        } catch (Exception ex) {
            ex.printStackTrace();
            return List.of();
        }

    }

}

// CODE FOR DEBUGGING ERROR WHEN CONVERTING JSON VALUE TO NEWS_JAVA_OBJECT

// List<News> newsList = new ArrayList<>();

// for (int i = 0; i < articles.size(); i++) {
// JsonObject article = articles.getJsonObject(i);

// logger.info("article %d: title value: %s\n".formatted(i,
// article.get("title")));
// logger.info("article %d: title value type: %s\n".formatted(i,
// article.get("title").getValueType()));
// logger.info("article %d: title value equals null: %s\n".formatted(i,
// article.get("title") == null));

// logger.info("article %d: content value: %s\n".formatted(i,
// article.get("content")));
// logger.info("article %d: content value type: %s\n".formatted(i,
// article.get("content").getValueType()));
// logger.info("article %d: content value equals null: %s\n".formatted(i,
// article.get("content") == null));

// logger.info("article %d: url value: %s\n".formatted(i, article.get("url")));
// logger.info("article %d: url value type: %s\n".formatted(i,
// article.get("url").getValueType()));
// logger.info("article %d: url value equals null: %s\n".formatted(i,
// article.get("url") == null));

// logger.info("article %d: imgUrl value: %s\n".formatted(i,
// article.get("urlToImage")));
// logger.info("article %d: imgUrl value type: %s\n".formatted(i,
// article.get("urlToImage").getValueType()));
// logger.info("article %d: imgUrl value equals null: %s\n".formatted(i,
// article.get("urlToImage") == null));

// logger.info("=====================");

// }
