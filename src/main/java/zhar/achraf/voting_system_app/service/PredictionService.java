package zhar.achraf.voting_system_app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PredictionService {


    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public PredictionService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Predict the winning option for a poll using OpenAI ChatGPT.
     *
     * @param pollTitle Poll question or title.
     * @param options   List of poll options.
     * @return Predicted winning option.
     * @throws Exception If prediction API request fails.
     */
    public String predictWinningOption(String pollTitle, List<String> options) throws Exception {

        StringBuilder prompt = new StringBuilder("You are an expert in predicting outcomes.\n");
        prompt.append("Based on the following polling question and options, predict which option is most likely to win:\n\n");
        prompt.append("Question: ").append(pollTitle).append("\n");
        prompt.append("Options:\n");
        for (int i = 0; i < options.size(); i++) {
            prompt.append(i + 1).append(". ").append(options.get(i)).append("\n");
        }
        prompt.append("\nProvide only the winning option exactly as it is, without any additional informations, characters, symbols, prefixes, or suffixes.");

        // Build the request body for OpenAI API
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o-mini");
        requestBody.put("messages", List.of(
                Map.of("role", "system", "content", "You are a helpful assistant that is specialized in predicting outcomes based on your knowledge."),
                Map.of("role", "user", "content", prompt.toString())
        ));
        requestBody.put("temperature", 0.5);


        var headers = new org.springframework.http.HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");


        var requestEntity = new org.springframework.http.HttpEntity<>(requestBody, headers);
        String url = "https://api.openai.com/v1/chat/completions";

        try {
            var response = restTemplate.postForObject(url, requestEntity, Map.class);

            assert response != null;
            Map choice = (Map) ((List) response.get("choices")).get(0);
            Map message = (Map) choice.get("message");
            return (String) message.get("content");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Prediction failed. Please check your API configuration.");
        }
    }
}