/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.projetosi.controller;

import br.com.projetosi.Utils.Database;
import br.com.projetosi.Utils.DatabaseFactory;
import br.com.projetosi.dao.CategoriaDAO;
import br.com.projetosi.model.Categoria;
import br.com.projetosi.model.Produto;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author IANCA
 */
public class TelaCadastroProdutoController implements Initializable {

    @FXML
    private ComboBox comboBoxProdutoCategoria;
    @FXML
    private TextField textFieldProdutoNome;
    @FXML
    private TextField textFieldProdutoQuantidade;
    @FXML
    private TextField textFieldProdutoPreco;
    @FXML
    private Button buttonConfirmar;
    @FXML
    private Button buttonCancelar;

    private List<Categoria> listCategorias;
    private ObservableList<Categoria> observableListCategorias;

    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Produto produto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        categoriaDAO.setConnection(connection);
        carregarComboBoxCategorias();
    }

    public void carregarComboBoxCategorias() {
        listCategorias = categoriaDAO.listar();
        observableListCategorias = FXCollections.observableArrayList(listCategorias);
        comboBoxProdutoCategoria.setItems(observableListCategorias);
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto p) {
        this.produto = p;
        this.textFieldProdutoNome.setText(p.getNome());
        this.textFieldProdutoQuantidade.setText(String.valueOf(p.getQuantidade()));
        this.textFieldProdutoPreco.setText(String.valueOf(p.getPreco()));
    }

    public boolean isButtonConfirmarClicked() {
        return buttonConfirmarClicked;
    }

    public void setButtonConfirmarClicked(boolean buttonConfirmarClicked) {
        this.buttonConfirmarClicked = buttonConfirmarClicked;
    }

    @FXML
    public void handleButtonConfirmar() {
        if (validarEntradaDeDados()) {
            
            produto.setNome(textFieldProdutoNome.getText());
            produto.setPreco(Double.valueOf(textFieldProdutoPreco.getText()));
            produto.setQuantidade(Integer.valueOf(textFieldProdutoQuantidade.getText()));
            produto.setCategoria((Categoria) comboBoxProdutoCategoria.getSelectionModel().getSelectedItem());
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
        if (comboBoxProdutoCategoria.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Categoria inválido!\n";
        }
        if (textFieldProdutoNome.getText() == null || textFieldProdutoNome.getText().length() == 0) {
            errorMessage += "Nome inválido!\n";
        }
        if (textFieldProdutoPreco.getText() == null || textFieldProdutoPreco.getText().length() == 0) {
            errorMessage += "Preço inválido!\n";
        }
        if (textFieldProdutoQuantidade.getText() == null || textFieldProdutoQuantidade.getText().length() == 0) {
            errorMessage += "Quantidade inválido!\n";
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
