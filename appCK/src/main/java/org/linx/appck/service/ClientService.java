package org.linx.appck.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.linx.appck.enums.UrlEnum;
import org.linx.appck.model.Client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ClientService {

    public Client findByEmail(String email, String jwtToken) throws Exception {
        String urlString = UrlEnum.FIND_CLIENT_BY_EMAIL_URL.getUrl() + "?email=" + URLEncoder.encode(email, StandardCharsets.UTF_8.toString());
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + jwtToken);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            ObjectMapper mapper = new ObjectMapper();
            Client client = mapper.readValue(in, Client.class);
            in.close();
            return client;
        } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
            throw new RuntimeException("Client not found: " + email);
        } else {
            throw new RuntimeException("Failed to fetch client by email: " + responseCode);
        }
    }
}
