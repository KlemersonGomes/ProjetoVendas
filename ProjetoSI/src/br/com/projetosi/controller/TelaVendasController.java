package br.com.projetosi.controller;

import br.com.projetosi.Utils.Database;
import br.com.projetosi.Utils.DatabaseFactory;
import br.com.projetosi.dao.ItemVendaDAO;
import br.com.projetosi.dao.ProdutoDAO;
import br.com.projetosi.dao.VendaDAO;
import br.com.projetosi.model.ItemVenda;
import br.com.projetosi.model.Produto;
import br.com.projetosi.model.Venda;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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

public class TelaVendasController implements Initializable {

    @FXML
    private TableView<Venda> tableViewVendas;
    @FXML
    private TableColumn<Venda, Integer> tableColumnVendaCodigo;
    @FXML
    private TableColumn<Venda, LocalDate> tableColumnVendaData;
    @FXML
    private TableColumn<Venda, Venda> tableColumnVendaCliente;
    @FXML
    private Button buttonInserir;
    @FXML
    private Button buttonAlterar;
    @FXML
    private Button buttonRemover;
    @FXML
    private Label labelVendaId;
    @FXML
    private Label labelVendaData;
    @FXML
    private Label labelVendaValor;
    @FXML
    private Label labelVendaPago;
    @FXML
    private Label labelVendaCliente;

    private List<Venda> listVendas;

    private ObservableList<Venda> observableListVendas;

    private final Database database = DatabaseFactory.getDatabase("postgresql");

    private final Connection connection = database.conectar();

    private final VendaDAO vendaDAO = new VendaDAO();

    private final ItemVendaDAO itemVendaDAO = new ItemVendaDAO();

    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vendaDAO.setConnection(connection);
        carregarTableViewVendas();

        selecionarItemTableViewVendas(null);

        tableViewVendas.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewVendas(newValue));

    }

    public void carregarTableViewVendas() {
        tableColumnVendaCodigo.setCellValueFactory(new PropertyValueFactory<>("idVenda"));
        tableColumnVendaData.setCellValueFactory(new PropertyValueFactory<>("data"));
        tableColumnVendaCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));

        listVendas = vendaDAO.listar();

        observableListVendas = FXCollections.observableArrayList(listVendas);
        tableViewVendas.setItems(observableListVendas);
    }

    public void selecionarItemTableViewVendas(Venda venda) {
        if (venda != null) {
            labelVendaId.setText(String.valueOf(venda.getIdVenda()));
            labelVendaData.setText(String.valueOf(venda.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            labelVendaValor.setText(String.format("%.2f", venda.getValor()));
            labelVendaPago.setText(String.valueOf(venda.getPago()));
            labelVendaCliente.setText(venda.getCliente().toString());
        } else {
            labelVendaId.setText("");
            labelVendaData.setText("");
            labelVendaValor.setText("");
            labelVendaPago.setText("");
            labelVendaCliente.setText("");
        }
    }

    @FXML
    public void handleButtonInserir() throws IOException, SQLException {
        Venda venda = new Venda();
        List<ItemVenda> listItensVenda = new ArrayList<>();
        venda.setItensVenda(listItensVenda);
        boolean buttonConfirmarClicked = showTelaCadastroVendas(venda);
        if (buttonConfirmarClicked) {
            try {
                connection.setAutoCommit(false);
                vendaDAO.setConnection(connection);
                vendaDAO.inserir(venda);
                itemVendaDAO.setConnection(connection);
                produtoDAO.setConnection(connection);
                for (ItemVenda listItemVenda : venda.getItensVenda()) {
                    Produto produto = listItemVenda.getProduto();
                    listItemVenda.setVenda(vendaDAO.buscarUltimaVenda());
                    itemVendaDAO.inserir(listItemVenda);
                    produto.setQuantidade(produto.getQuantidade() - listItemVenda.getQuantidade());
                    produtoDAO.alterar(produto);
                }
                connection.commit();
                carregarTableViewVendas();
            } catch (SQLException ex) {
                try {
                    connection.rollback();
                } catch (SQLException ex1) {
                    Logger.getLogger(TelaVendasController.class.getName()).log(Level.SEVERE, null, ex1);
                }
                Logger.getLogger(TelaVendasController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
     public boolean showTelaCadastroVendas(Venda venda) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TelaCadastroVendasController.class.getResource("/br/com/projetosi/view/TelaCadastroVendas.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Registro de Vendas");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        
        TelaCadastroVendasController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setVenda(venda);
        
        dialogStage.showAndWait();
        return controller.isButtonConfirmarClicked();
    }
    
     
    @FXML
    public void handleButtonRemover() throws IOException, SQLException {
        Venda venda = tableViewVendas.getSelectionModel().getSelectedItem();
        if (venda != null) {
            connection.setAutoCommit(false);
            vendaDAO.setConnection(connection);
            itemVendaDAO.setConnection(connection);
            produtoDAO.setConnection(connection);
            for (ItemVenda listItemVenda : venda.getItensVenda()) {
                Produto produto = listItemVenda.getProduto();
                produto.setQuantidade(produto.getQuantidade() + listItemVenda.getQuantidade());
                produtoDAO.alterar(produto);
                itemVendaDAO.remover(listItemVenda);
            }
            vendaDAO.remover(venda);
            connection.commit();
            carregarTableViewVendas();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha uma venda na Tabela!");
            alert.show();
        }
    }
}
