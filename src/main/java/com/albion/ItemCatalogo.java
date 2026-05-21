package com.albion;

import java.util.*;

public class ItemCatalogo {

    // Mapa: palavra-chave (lowercase) -> prefixo do item_id no banco
    private static final Map<String, String> KEYWORDS = new LinkedHashMap<>();

    static {
        // Elmos
        KEYWORDS.put("elmo de soldado", "HEAD_PLATE_SET1");
        KEYWORDS.put("elmo de cavaleiro", "HEAD_PLATE_SET2");
        KEYWORDS.put("elmo de guardiao", "HEAD_PLATE_SET3");
        KEYWORDS.put("elmo de guardião", "HEAD_PLATE_SET3");
        KEYWORDS.put("capote de erudito", "HEAD_CLOTH_SET1");
        KEYWORDS.put("capote de clerigo", "HEAD_CLOTH_SET2");
        KEYWORDS.put("capote de mago", "HEAD_CLOTH_SET3");
        KEYWORDS.put("capuz de mercenario", "HEAD_LEATHER_SET1");
        KEYWORDS.put("capuz de cacador", "HEAD_LEATHER_SET2");
        KEYWORDS.put("capuz de assassino", "HEAD_LEATHER_SET3");

        // Armaduras
        KEYWORDS.put("armadura de soldado", "ARMOR_PLATE_SET1");
        KEYWORDS.put("armadura de cavaleiro", "ARMOR_PLATE_SET2");
        KEYWORDS.put("armadura de guardiao", "ARMOR_PLATE_SET3");
        KEYWORDS.put("robe de erudito", "ARMOR_CLOTH_SET1");
        KEYWORDS.put("robe de clerigo", "ARMOR_CLOTH_SET2");
        KEYWORDS.put("robe de mago", "ARMOR_CLOTH_SET3");
        KEYWORDS.put("casaco de mercenario", "ARMOR_LEATHER_SET1");
        KEYWORDS.put("casaco de cacador", "ARMOR_LEATHER_SET2");
        KEYWORDS.put("casaco de assassino", "ARMOR_LEATHER_SET3");

        // Botas
        KEYWORDS.put("botas de soldado", "SHOES_PLATE_SET1");
        KEYWORDS.put("botas de cavaleiro", "SHOES_PLATE_SET2");
        KEYWORDS.put("botas de guardiao", "SHOES_PLATE_SET3");
        KEYWORDS.put("sandalias de erudito", "SHOES_CLOTH_SET1");
        KEYWORDS.put("sandalias de mago", "SHOES_CLOTH_SET3");
        KEYWORDS.put("sapatos de mercenario", "SHOES_LEATHER_SET1");
        KEYWORDS.put("sapatos de cacador", "SHOES_LEATHER_SET2");
        KEYWORDS.put("sapatos de assassino", "SHOES_LEATHER_SET3");

        // Espadas
        KEYWORDS.put("espada larga", "MAIN_SWORD");
        KEYWORDS.put("espadas duplas", "2H_DUALSWORD");
        KEYWORDS.put("montante", "2H_CLAYMORE");
        KEYWORDS.put("cria-reis", "2H_KINGMAKER");
        KEYWORDS.put("kingmaker", "2H_KINGMAKER");

        // Machados
        KEYWORDS.put("machado de guerra", "MAIN_AXE");
        KEYWORDS.put("machadao", "2H_AXE");
        KEYWORDS.put("machadão", "2H_AXE");
        KEYWORDS.put("alabarda", "2H_HALBERD");

        // Maças
        KEYWORDS.put("maca", "MAIN_MACE");
        KEYWORDS.put("maça", "MAIN_MACE");
        KEYWORDS.put("maca pesada", "2H_MACE");
        KEYWORDS.put("maça pesada", "2H_MACE");
        KEYWORDS.put("mangual", "2H_FLAIL");

        // Martelos
        KEYWORDS.put("martelo", "MAIN_HAMMER");
        KEYWORDS.put("martelo de batalha", "2H_POLEHAMMER");

        // Lanças
        KEYWORDS.put("lanca", "MAIN_SPEAR");
        KEYWORDS.put("lança", "MAIN_SPEAR");
        KEYWORDS.put("pique", "2H_SPEAR");

        // Adagas
        KEYWORDS.put("adaga", "MAIN_DAGGER");
        KEYWORDS.put("par de adagas", "2H_DAGGER");
        KEYWORDS.put("garras", "2H_CLAWS_HELL");

        // Bordões
        KEYWORDS.put("bordao", "2H_QUARTERSTAFF");
        KEYWORDS.put("bordão", "2H_QUARTERSTAFF");

        // Cajados
        KEYWORDS.put("cajado arcano", "MAIN_ARCANESTAFF");
        KEYWORDS.put("cajado de fogo", "MAIN_FIRESTAFF");
        KEYWORDS.put("cajado de gelo", "MAIN_FROSTSTAFF");
        KEYWORDS.put("cajado amaldicoado", "MAIN_CURSEDSTAFF");
        KEYWORDS.put("cajado amaldiçoado", "MAIN_CURSEDSTAFF");
        KEYWORDS.put("cajado sagrado", "MAIN_HOLYSTAFF");
        KEYWORDS.put("cajado da natureza", "MAIN_NATURESTAFF");

        // Arcos e bestas
        KEYWORDS.put("arco", "2H_BOW");
        KEYWORDS.put("arco de guerra", "2H_WARBOW");
        KEYWORDS.put("arco longo", "2H_LONGBOW");
        KEYWORDS.put("besta", "2H_CROSSBOW");
        KEYWORDS.put("besta pesada", "2H_CROSSBOWLARGE");

        // Escudos
        KEYWORDS.put("escudo", "OFF_SHIELD");
        KEYWORDS.put("tomo", "OFF_BOOK");
        KEYWORDS.put("tocha", "OFF_TORCH");
        KEYWORDS.put("bolsa", "BAG");
        KEYWORDS.put("capa", "CAPE");

        // Recursos
        KEYWORDS.put("minerio de cobre", "T2_ORE");
        KEYWORDS.put("minerio de estanho", "T3_ORE");
        KEYWORDS.put("minerio de ferro", "T4_ORE");
        KEYWORDS.put("minerio de titanio", "T5_ORE");
        KEYWORDS.put("minerio de runita", "T6_ORE");
        KEYWORDS.put("minerio de meteorito", "T7_ORE");
        KEYWORDS.put("minerio de adamante", "T8_ORE");
        KEYWORDS.put("tronco", "WOOD");
        KEYWORDS.put("tabua", "PLANKS");
        KEYWORDS.put("tábua", "PLANKS");
        KEYWORDS.put("fibra", "FIBER");
        KEYWORDS.put("tecido", "CLOTH");
        KEYWORDS.put("pelego", "HIDE");
        KEYWORDS.put("couro", "LEATHER");
        KEYWORDS.put("pedra", "ROCK");
        KEYWORDS.put("bloco", "STONEBLOCK");
        KEYWORDS.put("barra", "METALBAR");

        // Comidas
        KEYWORDS.put("peixe grelhado", "MEAL_GRILLEDFISH");
        KEYWORDS.put("sopa", "MEAL_SOUP");
        KEYWORDS.put("torta", "MEAL_PIE");
        KEYWORDS.put("omelete", "MEAL_OMELETTE");
        KEYWORDS.put("sanduiche", "MEAL_SANDWICH");
        KEYWORDS.put("salada", "MEAL_SALAD");

        // Pocoes
        KEYWORDS.put("pocao de cura", "POTION_HEAL");
        KEYWORDS.put("poção de cura", "POTION_HEAL");
        KEYWORDS.put("pocao de energia", "POTION_ENERGY");
        KEYWORDS.put("poção de energia", "POTION_ENERGY");
    }

    /**
     * Tenta encontrar um item_id a partir da pergunta do usuário.
     * Primeiro tenta frases longas, depois curtas (para evitar falsos positivos).
     * Retorna null se não encontrar nada.
     */
    public static String buscarIdNaPergunta(String pergunta) {
        String p = normalizar(pergunta);

        // Ordena por tamanho decrescente para priorizar frases mais específicas
        List<String> chaves = new ArrayList<>(KEYWORDS.keySet());
        chaves.sort((a, b) -> b.length() - a.length());

        for (String chave : chaves) {
            if (p.contains(chave)) {
                return KEYWORDS.get(chave);
            }
        }
        return null;
    }

    private static String normalizar(String texto) {
        return texto.toLowerCase()
                .replace("á", "a").replace("à", "a").replace("ã", "a").replace("â", "a")
                .replace("é", "e").replace("ê", "e")
                .replace("í", "i")
                .replace("ó", "o").replace("ô", "o").replace("õ", "o")
                .replace("ú", "u")
                .replace("ç", "c");
    }
}