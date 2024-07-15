package org.linx.appck.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.stage.Stage;

import org.linx.appck.enums.UrlEnum;
import org.linx.appck.webUtils.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class LoginController {

    @FXML
    private Button button;
    @FXML
    private Label wrong_login;
    @FXML
    private PasswordField password;
    @FXML
    private TextField login;

    @FXML
    void loginButtonClicked(ActionEvent event) {
        String username = login.getText();
        String pass = password.getText();

        // URL e credenciais para autenticação básica
        String urlString = UrlEnum.AUTHENTICATE_URL.getUrl();
        String authString = username + ":" + pass;
        String authHeader = "Basic " + Base64.getEncoder().encodeToString(authString.getBytes());

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", authHeader);
            connection.setDoOutput(true);

            // Verificação do código de resposta
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Leitura da resposta
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String jwtToken = in.readLine(); // Assuming the response body contains the JWT token
                in.close();

                // Salvando email e token na sessão
                Session.setEmail(username);
                Session.setJwtToken(jwtToken);

                // Ação após autenticação bem-sucedida
                wrong_login.setText("Autenticado com sucesso!");

                // Carregar a página principal
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainPageView.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(loader.load()));
                stage.show();

            } else {
                // Ação quando a autenticação falha
                wrong_login.setText("Login ou senha incorretos.");
            }
        } catch (IOException e) {
            e.printStackTrace();  // Mostrar stack trace no console
            wrong_login.setText("Erro de conexão.");
        }
    }
}
