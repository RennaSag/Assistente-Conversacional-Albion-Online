package com.albion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {

        // teste de conexão com o banco
        try {
            Connection conn = DatabaseConnection.get();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM market_history");
            if (rs.next()) {
                System.out.println("Conexão OK, registros em market_history: " + rs.getLong(1));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.err.println("Erro na conexão: " + e.getMessage());
        }

        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setTitle("Albion Market Assistant");
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}