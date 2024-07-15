package org.linx.appck.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Callback;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.scene.Node;
import org.linx.appck.webUtils.Session;
import org.linx.appck.model.Client;
import org.linx.appck.model.Product;
import org.linx.appck.service.ClientService;
import org.linx.appck.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

public class MainPageController {
    @FXML
    private ListView<Product> productListView;
    @FXML
    private TextField searchTextField;
    @FXML
    private Button searchButton;
    @FXML
    private Label totalValueLabel;
    @FXML
    private Label totalItemsLabel;
    @FXML
    private Label clientNameLabel;
    @FXML
    private Button logoutButton;

    private ProductService productService;
    private ClientService clientService;
    private List<Product> products;
    private Client client;

    @FXML
    public void initialize() {
        productService = new ProductService();
        clientService = new ClientService();
        try {
            // Buscando e atualizando lista de produtos
            products = productService.fetchProducts();
            updateProductList(products);
            updateTotalValues(products);

            // Configurando o botão de pesquisa
            searchButton.setOnAction(event -> searchProducts());

            // Buscando informações do cliente por email
            String email = Session.getEmail();
            String jwtToken = Session.getJwtToken();
            client = clientService.findByEmail(email, jwtToken);
            System.out.println("Cliente encontrado: " + client.getName());

            // Atualizando a label com o nome do cliente
            clientNameLabel.setText(client.getName());

            // Configurando o botão de logout
            logoutButton.setOnAction(event -> logout(event));

        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();  // Mostrar stack trace no console
        }

        // Configurando a ListView para usar a ListCell personalizada
        productListView.setCellFactory(new Callback<ListView<Product>, ListCell<Product>>() {
            @Override
            public ListCell<Product> call(ListView<Product> listView) {
                return new ProductListCell();
            }
        });
    }

    private void searchProducts() {
        String query = searchTextField.getText().toLowerCase();
        List<Product> filteredProducts = products.stream()
                .filter(product -> product.getNome().toLowerCase().contains(query))
                .collect(Collectors.toList());
        updateProductList(filteredProducts);
    }

    private void updateProductList(List<Product> products) {
        ObservableList<Product> observableProducts = FXCollections.observableArrayList(products);
        productListView.setItems(observableProducts);
    }

    private void updateTotalValues(List<Product> products) {
        double totalValue = products.stream().mapToDouble(Product::getValorTotal).sum();
        int totalItems = products.stream().mapToInt(Product::getQuantidade).sum();

        totalValueLabel.setText(String.format("R$ %.2f", totalValue));
        totalItemsLabel.setText(String.valueOf(totalItems));
    }

    private void logout(javafx.event.ActionEvent event) {
        // Limpar a sessão
        Session.clear();

        try {
            // Redirecionar para a tela de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Classe interna para formatar a ListCell
    // Classe interna para formatar a ListCell
    // Classe interna para formatar a ListCell
    // Classe interna para formatar a ListCell
    // Classe interna para formatar a ListCell
    private static class ProductListCell extends ListCell<Product> {
        private GridPane gridPane;
        private Label nameLabel;
        private Label priceLabel;
        private Label quantityLabel;
        private Button addButton;
        private Button subtractButton;
        private Button deleteButton;

        public ProductListCell() {
            super();
            nameLabel = new Label();
            priceLabel = new Label();
            quantityLabel = new Label();
            addButton = new Button("+");
            subtractButton = new Button("-");
            deleteButton = new Button("x");

            // Configurando o layout dos botões
            HBox buttons = new HBox(addButton, subtractButton, deleteButton);
            buttons.setSpacing(10);

            // Configurando o layout das colunas
            gridPane = new GridPane();
            gridPane.add(nameLabel, 0, 0);
            gridPane.add(priceLabel, 1, 0);
            gridPane.add(quantityLabel, 2, 0);
            gridPane.add(buttons, 3, 0);

            ColumnConstraints column1 = new ColumnConstraints();
            column1.setPercentWidth(40); // Nome ocupa 40%
            ColumnConstraints column2 = new ColumnConstraints();
            column2.setPercentWidth(20); // Valor ocupa 20%
            ColumnConstraints column3 = new ColumnConstraints();
            column3.setPercentWidth(20); // Quantidade ocupa 20%
            ColumnConstraints column4 = new ColumnConstraints();
            column4.setPercentWidth(20); // Botões ocupam 20%
            gridPane.getColumnConstraints().addAll(column1, column2, column3, column4);
        }

        @Override
        protected void updateItem(Product product, boolean empty) {
            super.updateItem(product, empty);
            if (empty || product == null) {
                setText(null);
                setGraphic(null);
            } else {
                nameLabel.setText(product.getNome());
                priceLabel.setText(String.format("R$ %.2f", product.getPreco()));
                quantityLabel.setText(String.valueOf(product.getQuantidade()));
                setGraphic(gridPane);

                // Adicionar ações para os botões se necessário
                addButton.setOnAction(event -> {
                    // Lógica para adicionar
                });
                subtractButton.setOnAction(event -> {
                    // Lógica para subtrair
                });
                deleteButton.setOnAction(event -> {
                    // Lógica para deletar
                });
            }
        }
    }
}