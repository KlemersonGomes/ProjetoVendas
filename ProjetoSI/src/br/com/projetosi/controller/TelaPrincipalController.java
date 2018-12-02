/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.projetosi.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author IANCA
 */
public class TelaPrincipalController implements Initializable {

    @FXML
    private MenuItem menuItemCadastroCliente;
    @FXML
    private MenuItem menuItemCadastroProduto;
    @FXML
    private MenuItem menuItemCadastroCategoria;
    @FXML
    private MenuItem menuItemProcessosVendas;
    @FXML
    private MenuItem menuItemRelatorioProdutos;
    @FXML
    private AnchorPane anchorPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    public void handleMenuItemCliente() throws IOException {
        AnchorPane a = (AnchorPane)FXMLLoader.load(getClass().getResource("/br/com/projetosi/view/TelaCliente.fxml"));
        anchorPane.getChildren().setAll(a);
    }
    
    @FXML
    public void handleMenuItemProduto() throws IOException {
        AnchorPane a = (AnchorPane)FXMLLoader.load(getClass().getResource("/br/com/projetosi/view/TelaProduto.fxml"));
        anchorPane.getChildren().setAll(a);
    }
    
    @FXML
    public void handleMenuItemVendas() throws IOException {
        
        AnchorPane a = FXMLLoader.load(getClass().getResource("/br/com/projetosi/view/TelaVendas.fxml"));
        anchorPane.getChildren().setAll(a);
    }
    
    @FXML
    public void handleMenuItemCategoria() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/br/com/projetosi/view/TelaCategoria.fxml"));
        anchorPane.getChildren().setAll(a);
    }
    
    @FXML
    public void handleMenuItemRelatoriosQuantidadeProdutos() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/br/com/projetosi/view/TelaRelatorioQuantidadeProduto.fxml"));
        anchorPane.getChildren().setAll(a);
    }
    

}
