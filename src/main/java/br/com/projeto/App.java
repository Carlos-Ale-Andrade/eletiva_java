package br.com.projeto;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    private List<RegistroCSV> registros; // lista de registros lidos

    @Override
    public void start(Stage primaryStage) {

        // Nome do arquivo (deve estar em src/main/resources)
        String nomeArquivo = "/dados_chuvas_vazoes_2010_2020.csv";

        // Cria componentes da interface
        TextField campoBusca = new TextField();
        campoBusca.setPromptText("Digite o código ou parte da estação para filtrar...");

        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);

        Label statusLabel = new Label("Digite algo para filtrar os resultados.");

        try {
            // Lê o arquivo CSV do resources
            InputStream is = getClass().getResourceAsStream(nomeArquivo);

            if (is == null) {
                textArea.setText("Erro: Arquivo não encontrado em 'src/main/resources'.");
            } else {
                try (Reader leitorStream = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                    LeitorComms leitor = new LeitorComms();
                    registros = leitor.lerCsv(leitorStream); // retorna lista de objetos
                    textArea.setText(leitor.formatarRegistros(registros));
                    statusLabel.setText("Total de registros: " + registros.size());
                }
            }
        } catch (Exception e) {
            textArea.setText("Erro ao processar o arquivo CSV:\n" + e.getMessage());
            e.printStackTrace();
        }

        // Quando o usuário digitar algo, filtra os resultados
        campoBusca.textProperty().addListener((obs, oldVal, newVal) -> {
            if (registros == null) return;

            LeitorComms leitor = new LeitorComms();
            List<RegistroCSV> filtrados = leitor.filtrarRegistros(registros, newVal);
            textArea.setText(leitor.formatarRegistros(filtrados));
            statusLabel.setText("Exibindo " + filtrados.size() + " de " + registros.size() + " registros");
        });

        // Layout da interface
        VBox topo = new VBox(8, new Label("Filtro de busca:"), campoBusca, statusLabel);
        topo.setPadding(new Insets(10));

        ScrollPane scroll = new ScrollPane(textArea);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);

        BorderPane root = new BorderPane();
        root.setTop(topo);
        root.setCenter(scroll);

        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Leitor de CSV — Vazões e Chuvas (com filtro)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
