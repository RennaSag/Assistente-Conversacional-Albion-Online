package com.albion;

import java.sql.*;

public class ChatHistoryService {

    public void salvar(String pergunta, String resposta, String contexto) throws SQLException {
        String sql = """
                INSERT INTO chat_history (pergunta, resposta, contexto)
                VALUES (?, ?, ?)
                """;

        Connection conn = DatabaseConnection.get();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pergunta);
            ps.setString(2, resposta);
            ps.setString(3, contexto);
            ps.executeUpdate();
        }
    }
}