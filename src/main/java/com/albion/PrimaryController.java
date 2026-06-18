package com.albion;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class PrimaryController {

    @FXML
    private VBox messagesBox;
    @FXML
    private TextField inputField;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Label statusLabel;

    private final MarketService marketService = new MarketService();
    private final GeminiService geminiService = new GeminiService();


    private final ChatHistoryService chatHistoryService = new ChatHistoryService();


    @FXML
    public void initialize() {
        adicionarMensagemAssistente("Sou um assistente conversacional para preços de itens de Albion Online, faça alguma pergunta.");
    }

    @FXML
    private void onPerguntar() {
        String pergunta = inputField.getText().trim();
        if (pergunta.isEmpty()) return;

        adicionarMensagemUsuario(pergunta);
        inputField.clear();

        String respostaForaDeEscopo = EscopoUtil.respostaSeForaDeEscopo(pergunta);
        if (respostaForaDeEscopo != null) {
            adicionarMensagemAssistente(respostaForaDeEscopo);
            new Thread(() -> {
                try {
                    chatHistoryService.salvar(pergunta, respostaForaDeEscopo, null);
                } catch (Exception e) {
                    System.err.println("Falha ao salvar histórico: " + e.getMessage());
                }
            }).start();
            return;
        }

        inputField.setDisable(true);
        statusLabel.setText("Processando...");
        statusLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #C9A84C;");

        new Thread(() -> {
            try {
                String contexto = marketService.gerarContexto(pergunta);
                String resposta = geminiService.perguntar(contexto, pergunta);

                try {
                    chatHistoryService.salvar(pergunta, resposta, contexto);
                } catch (Exception e) {
                    System.err.println("Falha ao salvar histórico: " + e.getMessage());
                }

                Platform.runLater(() -> {
                    adicionarMensagemAssistente(resposta);
                    inputField.setDisable(false);
                    inputField.requestFocus();
                    statusLabel.setText("Online");
                    statusLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #3FB950;");
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    adicionarMensagemAssistente("Erro: " + e.getMessage());
                    inputField.setDisable(false);
                    statusLabel.setText("Online");
                    statusLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #3FB950;");
                });
            }
        }).start();
    }

    @FXML
    private void onRelatorio() {
        inputField.setDisable(true);
        statusLabel.setText("Gerando relatório...");
        statusLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #C9A84C;");
        adicionarMensagemUsuario("Gerar relatório de mercado");

        new Thread(() -> {
            try {
                String contexto = marketService.buscarMaisNegociados(15)
                        + "\n" + marketService.buscarPrecoOuro();
                String pergunta = "Gere um relatório resumido do mercado de Albion Online com base nesses dados. " +
                        "Destaque tendências, itens em alta e o comportamento do ouro.";
                String resposta = geminiService.perguntar(contexto, pergunta);

                try {
                    chatHistoryService.salvar(pergunta, resposta, contexto);
                } catch (Exception e) {
                    System.err.println("Falha ao salvar histórico: " + e.getMessage());
                }

                Platform.runLater(() -> {
                    adicionarMensagemAssistente(resposta);
                    inputField.setDisable(false);
                    statusLabel.setText("Online");
                    statusLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #3FB950;");
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    adicionarMensagemAssistente("Erro: " + e.getMessage());
                    inputField.setDisable(false);
                    statusLabel.setText("Online");
                    statusLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #3FB950;");
                });
            }
        }).start();
    }

    private void adicionarMensagemUsuario(String texto) {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER_RIGHT);

        Label label = new Label(texto);
        label.setWrapText(true);
        label.setMaxWidth(420);
        label.setStyle(
                "-fx-background-color: #1F4E8C;" +
                        "-fx-text-fill: #E6EDF3;" +
                        "-fx-padding: 10 14 10 14;" +
                        "-fx-background-radius: 12 12 2 12;" +
                        "-fx-font-size: 13px;"
        );

        container.getChildren().add(label);
        messagesBox.getChildren().add(container);
        scrollToBottom();
    }

    private void adicionarMensagemAssistente(String texto) {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER_LEFT);
        container.setSpacing(10);

        javafx.scene.image.Image img = new javafx.scene.image.Image(
                getClass().getResourceAsStream("/com/albion/assistente.png")
        );
        javafx.scene.image.ImageView icone = new javafx.scene.image.ImageView(img);
        icone.setFitWidth(28);
        icone.setFitHeight(28);
        icone.setPreserveRatio(true);


        Label label = new Label(texto);
        label.setWrapText(true);
        label.setMaxWidth(420);
        label.setStyle(
                "-fx-background-color: #161B22;" +
                        "-fx-text-fill: #E6EDF3;" +
                        "-fx-padding: 10 14 10 14;" +
                        "-fx-background-radius: 12 12 12 2;" +
                        "-fx-font-size: 13px;" +
                        "-fx-border-color: #30363D;" +
                        "-fx-border-radius: 12 12 12 2;"
        );

        container.getChildren().addAll(icone, label);
        messagesBox.getChildren().add(container);
        scrollToBottom();
    }

    private void scrollToBottom() {
        Platform.runLater(() -> scrollPane.setVvalue(1.0));
    }
}