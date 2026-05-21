package com.albion;

import com.google.gson.*;
import java.net.URI;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublishers;

public class GeminiService {

    private static final String API_KEY = Config.get("gemini.api.key");
    private static final String URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + API_KEY;

    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public String perguntar(String contexto, String pergunta) throws Exception {
        String prompt = """
            Você é um assistente especializado no mercado de Albion Online.
            Responda em português, de forma clara e objetiva.
            
            Dados do mercado recuperados do banco:
            %s
            
            Pergunta do jogador: %s
            """.formatted(contexto, pergunta);

        JsonObject content = new JsonObject();
        JsonArray parts = new JsonArray();
        JsonObject part = new JsonObject();
        part.addProperty("text", prompt);
        parts.add(part);
        content.add("parts", parts);

        JsonArray contents = new JsonArray();
        contents.add(content);

        JsonObject body = new JsonObject();
        body.add("contents", contents);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(gson.toJson(body)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Resposta Gemini: " + response.body());

        JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

        if (json.has("error")) {
            String msg = json.getAsJsonObject("error").get("message").getAsString();
            throw new RuntimeException("Erro da API Gemini: " + msg);
        }

        return json.getAsJsonArray("candidates")
                .get(0).getAsJsonObject()
                .getAsJsonObject("content")
                .getAsJsonArray("parts")
                .get(0).getAsJsonObject()
                .get("text").getAsString();
    }
}