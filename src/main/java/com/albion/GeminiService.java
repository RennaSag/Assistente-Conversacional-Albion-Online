package com.albion;

import com.google.gson.*;
import java.net.URI;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublishers;

public class GeminiService {

    private static final String API_KEY = Config.get("gemini.api.key");
    //private static final String URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent?key=" + API_KEY;
    private static final String URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + API_KEY;

    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public String perguntar(String contexto, String pergunta) throws Exception {
        String prompt = """
                Você é um assistente especializado exclusivamente no mercado de Albion Online.
                Seu único propósito é responder perguntas sobre preços de itens, preço do ouro,
                itens mais negociados e tendências de mercado do jogo Albion Online, com base
                nos dados fornecidos abaixo.
                
                Regras obrigatórias:
                    - Se a pergunta do jogador não tiver relação com preços, itens, ouro ou mercado
                      de Albion Online, não responda o que foi perguntado. Em vez disso, explique de
                      forma breve e simpática que você só pode ajudar com informações de mercado do
                      jogo, e peça que ele faça uma pergunta sobre preços de itens ou do ouro.
                    - Nunca invente preços ou dados que não estejam na seção "Dados do mercado" abaixo.
                    - Se os dados fornecidos estiverem vazios ou não tiverem relação com a pergunta,
                      diga isso ao jogador.
                    - Responda em português, de forma clara e objetiva. Não utilize * nem outro
                      caractere especial nas suas mensagens, deixe elas mais limpas e simples, de
                      fácil compreensão. Use uma linguagem simples e sem termos nem palavras
                      complexas e difíceis de entender. Tente falar apenas o necessário. 
                
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