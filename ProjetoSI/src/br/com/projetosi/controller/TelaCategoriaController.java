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
public class TelaCategoriaController implements Initializable {

    @FXML
    private TableView<Categoria> tableViewCategoria;
    @FXML
    private TableColumn<Categoria, String> tableColumnCategoriaID;
    @FXML
    private TableColumn<Categoria, String> tableColumnCategoriaNome;
    @FXML
    private Button buttonNovo;
    @FXML
    private Button buttonDeletar;
    @FXML
    private Button buttonAlterar;
    @FXML
    private Label labelCategoriaID;
    @FXML
    private Label labelCategoriaNome;

    private List<Categoria> listCategorias;

    private ObservableList<Categoria> observableListCategorias;

    private final Database database = DatabaseFactory.getDatabase("postgresql");

    private final Connection connection = database.conectar();

    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        categoriaDAO.setConnection(connection);
        carregarTableViewCategoria();

        tableViewCategoria.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewCategoria(newValue));
    }

    public void carregarTableViewCategoria() {
        this.tableColumnCategoriaID.setCellValueFactory(new PropertyValueFactory<>("idCategoria"));
        this.tableColumnCategoriaNome.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        listCategorias = categoriaDAO.listar();

        observableListCategorias = FXCollections.observableArrayList(listCategorias);
        tableViewCategoria.setItems(observableListCategorias);

    }

    public void selecionarItemTableViewCategoria(Categoria categoria) {
        if (categoria != null) {
            labelCategoriaID.setText(String.valueOf(categoria.getIdCategoria()));
            labelCategoriaNome.setText(categoria.getDescricao());
        } else {
            labelCategoriaID.setText("");
            labelCategoriaNome.setText("");
        }

    }

    @FXML
    public void handleButtonNovo() throws IOException {
        Categoria categoria = new Categoria();
        boolean buttonConfirmarClicked = showTelaCadastroCategoria(categoria);
        if (buttonConfirmarClicked) {
            categoriaDAO.inserir(categoria);
            carregarTableViewCategoria();
        }
    }

    public boolean showTelaCadastroCategoria(Categoria categoria) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TelaCadastroCategoriaController.class.getResource("/br/com/projetosi/view/TelaCadastroCategoria.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Categoria");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        TelaCadastroCategoriaController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setCategoria(categoria);

        dialogStage.showAndWait();

        return controller.isButtonConfirmarClicked();

    }
}
