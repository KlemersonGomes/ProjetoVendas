/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.projetosi.controller;

import br.com.projetosi.Utils.Database;
import br.com.projetosi.Utils.DatabaseFactory;
import br.com.projetosi.dao.ClienteDAO;
import br.com.projetosi.dao.ProdutoDAO;
import br.com.projetosi.model.Cliente;
import br.com.projetosi.model.ItemVenda;
import br.com.projetosi.model.Produto;
import br.com.projetosi.model.Venda;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author IANCA
 */
public class TelaCadastroVendasController implements Initializable {

    @FXML
    private ComboBox comboBoxVendaCliente;
    @FXML
    private DatePicker datePickerVendaData;
    @FXML
    private CheckBox checkBoxVendaPago;
    @FXML
    private ComboBox comboBoxVendaProduto;
    @FXML
    private TableView<ItemVenda> tableViewVendaItensVenda;
    @FXML
    private TableColumn<ItemVenda, Produto> tableColumnItemVendaProduto;
    @FXML
    private TableColumn<ItemVenda, Integer> tableColumnItemVendaQuantidade;
    @FXML
    private TableColumn<ItemVenda, Double> tableColumnItemVendaValor;
    @FXML
    private TextField textFieldVendaValor;
    @FXML
    private TextField textFieldVendaQuantidade;
    @FXML
    private Button buttonFinalizar;
    @FXML
    private Button buttonCancelar;
    @FXML
    private Button buttonAdicionar;

    private List<Cliente> listClientes;
    private List<Produto> listProdutos;
    private ObservableList<Cliente> observableListClientes;
    private ObservableList<Produto> observableListProdutos;
    private ObservableList<ItemVenda> observableListItensVenda;

    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Venda venda;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clienteDAO.setConnection(connection);
        produtoDAO.setConnection(connection);
        carregarComboBoxClientes();
        carregarComboBoxProdutos();
        tableColumnItemVendaProduto.setCellValueFactory(new PropertyValueFactory<>("produto"));
        tableColumnItemVendaQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        tableColumnItemVendaValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
    }

    public void carregarComboBoxClientes() {
        listClientes = clienteDAO.listar();
        observableListClientes = FXCollections.observableArrayList(listClientes);
        comboBoxVendaCliente.setItems(observableListClientes);
    }

    public void carregarComboBoxProdutos() {
        listProdutos = produtoDAO.listar();
        observableListProdutos = FXCollections.observableArrayList(listProdutos);
        comboBoxVendaProduto.setItems(observableListProdutos);
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Venda getVenda() {
        return this.venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public boolean isButtonConfirmarClicked() {
        return buttonConfirmarClicked;
    }

    @FXML
    public void handleButtonAdicionar() {
        Produto produto;
        ItemVenda itemDeVenda = new ItemVenda();
        if (comboBoxVendaProduto.getSelectionModel().getSelectedItem() != null) {
            produto = (Produto) comboBoxVendaProduto.getSelectionModel().getSelectedItem();
            if (produto.getQuantidade() >= Integer.parseInt(textFieldVendaQuantidade.getText())) {
                itemDeVenda.setProduto((Produto) comboBoxVendaProduto.getSelectionModel().getSelectedItem());
                itemDeVenda.setQuantidade(Integer.parseInt(textFieldVendaQuantidade.getText()));
                itemDeVenda.setValor(itemDeVenda.getProduto().getPreco() * itemDeVenda.getQuantidade());
                venda.getItensVenda().add(itemDeVenda);
                venda.setValor(venda.getValor() + itemDeVenda.getValor());
                observableListItensVenda = FXCollections.observableArrayList(venda.getItensVenda());
                tableViewVendaItensVenda.setItems(observableListItensVenda);
                textFieldVendaValor.setText(String.format("%.2f", venda.getValor()));
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Problemas na escolha do produto!");
                alert.setContentText("Não existe a quantidade de produtos disponíveis no estoque!");
                alert.show();
            }
        }
    }

    @FXML
    public void handleButtonFinalizar() {
        if (validarEntradaDeDados()) {
            venda.setCliente((Cliente) comboBoxVendaCliente.getSelectionModel().getSelectedItem());
            venda.setPago(checkBoxVendaPago.isSelected());
            venda.setData(datePickerVendaData.getValue());
            buttonConfirmarClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    public void handleButtonCancelar() {
        getDialogStage().close();
    }

    
    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        if (comboBoxVendaCliente.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Cliente inválido!\n";
        }
        if (datePickerVendaData.getValue() == null) {
            errorMessage += "Data inválida!\n";
        }
        if (observableListItensVenda == null) {
            errorMessage += "Itens de Venda inválidos!\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campos inválidos, por favor, corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }

}
