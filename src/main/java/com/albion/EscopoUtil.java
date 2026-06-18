package com.albion;

import java.util.Set;

public class EscopoUtil {

    private static final Set<String> SAUDACOES = Set.of(
            "oi", "ola", "bom dia", "boa tarde", "boa noite",
            "hey", "hello", "eae", "e ai", "salve", "tudo bem",
            "como vai", "oii", "oie", "fala", "blz", "beleza"
    );

    private static final String MENSAGEM_FORA_DE_CONTEXTO =
            "Eu só consigo te ajudar com informações de mercado do Albion Online, " +
                    "como preço de itens, preço do ouro ou itens mais negociados. " +
                    "Pode me perguntar algo assim, por exemplo: \"Qual o preço da espada larga T5?\"";

    public static String respostaSeForaDeEscopo(String pergunta) {
        String p = normalizar(pergunta);

        if (p.isBlank() || p.length() < 3) {
            return MENSAGEM_FORA_DE_CONTEXTO;
        }

        if (SAUDACOES.contains(p)) {
            return MENSAGEM_FORA_DE_CONTEXTO;
        }

        return null; // segue o fluxo normal, vai pro banco + Gemini
    }

    private static String normalizar(String texto) {
        return texto.trim().toLowerCase().replaceAll("[!?.,]+$", "");
    }
}