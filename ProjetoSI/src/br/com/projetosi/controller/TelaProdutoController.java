/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.projetosi.controller;

import br.com.projetosi.Utils.Database;
import br.com.projetosi.Utils.DatabaseFactory;
import br.com.projetosi.dao.ItemVendaDAO;
import br.com.projetosi.dao.ProdutoDAO;
import br.com.projetosi.model.ItemVenda;
import br.com.projetosi.model.Produto;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author IANCA
 */
public class TelaProdutoController implements Initializable {

    @FXML
    private TableView<Produto> tableViewProduto;
    @FXML
    private TableColumn<Produto, String> tableColumnProdutoNome;
    @FXML
    private TableColumn<Produto, String> tableColumnProdutoQuantidade;
    @FXML
    private Button buttonNovoProduto;
    @FXML
    private Button buttonDeletar;
    @FXML
    private Button buttonAlterar;
    @FXML
    private Label labelProdutoID;
    @FXML
    private Label labelProdutoNome;
    @FXML
    private Label labelProdutoCategoria;
    @FXML
    private Label labelProdutoQuantidade;
    @FXML
    private Label labelProdutoPreco;

    private List<Produto> listProdutos;

    private ObservableList<Produto> observableListProdutos;

    private final Database database = DatabaseFactory.getDatabase("postgresql");

    private final Connection connection = database.conectar();

    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    private final ItemVendaDAO itemVendaDAO = new ItemVendaDAO();
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        produtoDAO.setConnection(connection);
        carregarTableViewProduto();

        tableViewProduto.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewProdutos(newValue));
    }
    
    public void carregarTableViewProduto() {
        this.tableColumnProdutoNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        this.tableColumnProdutoQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        listProdutos = produtoDAO.listar();

        observableListProdutos = FXCollections.observableArrayList(listProdutos);
        tableViewProduto.setItems(observableListProdutos);

    }

    public void selecionarItemTableViewProdutos(Produto produto) {
        if (produto != null) {
            labelProdutoID.setText(String.valueOf(produto.getIdProduto()));
            labelProdutoNome.setText(produto.getNome());
            labelProdutoQuantidade.setText(String.valueOf(produto.getQuantidade()));
            labelProdutoCategoria.setText(produto.getCategoria().toString());
            labelProdutoPreco.setText(String.format("%.2f", produto.getPreco()));
        } else {
            labelProdutoID.setText("");
            labelProdutoNome.setText("");
            labelProdutoCategoria.setText("");
            labelProdutoQuantidade.setText("");
            labelProdutoPreco.setText("");
        }

    }
    

    @FXML
    public void handleButtonDeletar() throws IOException {
        Produto produto = tableViewProduto.getSelectionModel().getSelectedItem();
        if (produto != null) {
            ItemVenda i = itemVendaDAO.buscarPorProduto(produto);
            itemVendaDAO.remover(i);
            produtoDAO.remover(produto);
            carregarTableViewProduto();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um produto na Tabela!");
            alert.show();
        }
    }

    @FXML
    public void handleButtonNovoProduto() throws IOException {
        Produto produto = new Produto();
        boolean buttonConfirmarClicked = showTelaCadastroProduto(produto);
        if (buttonConfirmarClicked) {
            produtoDAO.inserir(produto);
            carregarTableViewProduto();
        }
    }

     public boolean showTelaCadastroProduto(Produto produto) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TelaCadastroClienteController.class.getResource("/br/com/projetosi/view/TelaCadastroProduto.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Produtos");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        TelaCadastroProdutoController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setProduto(produto);

        dialogStage.showAndWait();

        return controller.isButtonConfirmarClicked();

    }
}
