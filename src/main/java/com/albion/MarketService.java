package com.albion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MarketService {


    public String buscarPrecoItem(String itemIdBase, int limite) throws SQLException {
        String sql = """
        SELECT item_id, location, quality,
               AVG(silver_amount::float / NULLIF(item_amount, 0)) AS preco_medio,
               SUM(item_amount) AS volume_total,
               MAX(timestamp) AS ultima_atualizacao
        FROM market_history
        WHERE item_id LIKE ?
          AND timestamp >= (SELECT MAX(timestamp) - INTERVAL '7 days' FROM market_history)
        GROUP BY item_id, location, quality
        ORDER BY item_id, preco_medio ASC
        LIMIT ?
        """;

        Connection conn = DatabaseConnection.get();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + itemIdBase + "%");
        ps.setInt(2, limite);
        ResultSet rs = ps.executeQuery();

        System.out.println("DEBUG SQL: " + ps.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("Dados de mercado para '").append(itemIdBase).append("':\n");

        boolean found = false;
        while (rs.next()) {
            found = true;
            String itemId = rs.getString("item_id");
            int enchant = 0;
            if (itemId.contains("@")) {
                enchant = Integer.parseInt(itemId.split("@")[1]);
            }
            sb.append(String.format(
                    "- %s | Encant: %d | Cidade: %d | Qualidade: %d | Preço médio: %.0f silver | Volume: %d\n",
                    itemId,
                    enchant,
                    rs.getInt("location"),
                    rs.getInt("quality"),
                    rs.getDouble("preco_medio"),
                    rs.getLong("volume_total")
            ));
        }

        System.out.println("DEBUG found: " + found); // retornou alguma linha?
        System.out.println("DEBUG resultado: " + sb.toString());

        if (!found) sb.append("Nenhum dado encontrado nos últimos 7 days.\n");
        rs.close();
        ps.close();
        return sb.toString();
    }


    public String buscarMaisNegociados(int limite) throws SQLException {
        String sql = """
                SELECT item_id,
                       SUM(item_amount) AS volume_total,
                       AVG(silver_amount::float / NULLIF(item_amount, 0)) AS preco_medio
                FROM market_history
                WHERE timestamp >= (SELECT MAX(timestamp) - INTERVAL '3 days' FROM market_history)
                GROUP BY item_id
                ORDER BY volume_total DESC
                LIMIT ?
                """;

        Connection conn = DatabaseConnection.get();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, limite);
        ResultSet rs = ps.executeQuery();

        StringBuilder sb = new StringBuilder();
        sb.append("Itens mais negociados nos últimos 3 dias:\n");

        int i = 1;
        while (rs.next()) {
            sb.append(String.format(
                    "%d. %s | Volume: %d | Preço médio: %.0f silver\n",
                    i++,
                    rs.getString("item_id"),
                    rs.getLong("volume_total"),
                    rs.getDouble("preco_medio")
            ));
        }

        rs.close();
        ps.close();
        return sb.toString();
    }


    public String buscarPrecoOuro() throws SQLException {
        String sql = """
                SELECT timestamp, price
                FROM gold_prices
                ORDER BY timestamp DESC
                LIMIT 24
                """;

        Connection conn = DatabaseConnection.get();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        StringBuilder sb = new StringBuilder();
        sb.append("Preço do ouro nas últimas 24h:\n");

        while (rs.next()) {
            sb.append(String.format(
                    "- %s: %d silver\n",
                    rs.getTimestamp("timestamp"),
                    rs.getLong("price")
            ));
        }

        rs.close();
        st.close();
        return sb.toString();
    }


    public String gerarContexto(String pergunta) throws SQLException {
        String p = pergunta.toLowerCase();

        if (p.contains("ouro") || p.contains("gold")) {
            return buscarPrecoOuro();
        }

        if (p.contains("mais negociado") || p.contains("popular") || p.contains("volume")) {
            return buscarMaisNegociados(10);
        }

        String itemIdBase = ItemCatalogo.buscarIdNaPergunta(pergunta);
        if (itemIdBase != null) {
            // Extrai tier da pergunta (t4, t5, t6 etc)
            String tier = "";
            java.util.regex.Matcher m = java.util.regex.Pattern
                    .compile("t(\\d)").matcher(p);
            if (m.find()) {
                tier = "T" + m.group(1) + "_";
            }
            String itemIdCompleto = tier + itemIdBase;
            System.out.println("DEBUG buscando: " + itemIdCompleto);
            return buscarPrecoItem(itemIdCompleto, 20);
        }

        return buscarMaisNegociados(5);
    }


}