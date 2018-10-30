/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.projetosi.dao;

import br.com.projetosi.model.ItemVenda;
import br.com.projetosi.model.Produto;
import br.com.projetosi.model.Venda;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author IANCA
 */
public class ItemVendaDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(ItemVenda itemVenda) {
        String sql = "INSERT INTO itensvenda(quantidade, valor, idProduto, idVenda) VALUES(?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, itemVenda.getQuantidade());
            stmt.setDouble(2, itemVenda.getValor());
            stmt.setLong(3, itemVenda.getProduto().getIdProduto());
            stmt.setLong(4, itemVenda.getVenda().getIdVenda());

            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ItemVendaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(ItemVenda itemVenda) {
        return true;
    }

    public boolean remover(ItemVenda itemVenda) {
        String sql = "DELETE FROM itensVenda WHERE idItemVenda=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, itemVenda.getIdItemVenda());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ItemVendaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<ItemVenda> listar() {
        String sql = "SELECT * FROM itensVenda";
        List<ItemVenda> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                ItemVenda itemVenda = new ItemVenda();
                Produto produto = new Produto();
                Venda venda = new Venda();
                itemVenda.setIdItemVenda(resultado.getLong("idItemVenda"));
                itemVenda.setQuantidade(resultado.getInt("quantidade"));
                itemVenda.setValor(resultado.getDouble("valor"));

                produto.setIdProduto(resultado.getLong("idProduto"));
                venda.setIdVenda(resultado.getLong("idVenda"));

                ProdutoDAO produtoDAO = new ProdutoDAO();
                produtoDAO.setConnection(connection);
                produto = produtoDAO.buscar(produto);

                VendaDAO vendaDAO = new VendaDAO();
                vendaDAO.setConnection(connection);
                venda = vendaDAO.buscar(venda);

                itemVenda.setProduto(produto);
                itemVenda.setVenda(venda);

                retorno.add(itemVenda);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemVendaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public List<ItemVenda> listarPorVenda(Venda venda) {
        String sql = "SELECT * FROM itensVenda WHERE idVenda=?";
        List<ItemVenda> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, venda.getIdVenda());
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                ItemVenda itemVenda = new ItemVenda();
                Produto produto = new Produto();
                Venda v = new Venda();
                itemVenda.setIdItemVenda(resultado.getInt("idItemVenda"));
                itemVenda.setQuantidade(resultado.getInt("quantidade"));
                itemVenda.setValor(resultado.getDouble("valor"));

                produto.setIdProduto(resultado.getLong("idProduto"));
                v.setIdVenda(resultado.getLong("idVenda"));

                ProdutoDAO produtoDAO = new ProdutoDAO();
                produtoDAO.setConnection(connection);
                produto = produtoDAO.buscar(produto);

                itemVenda.setProduto(produto);
                itemVenda.setVenda(v);

                retorno.add(itemVenda);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemVendaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public ItemVenda buscar(ItemVenda itemVenda) {
        String sql = "SELECT * FROM itensVenda WHERE idItemVenda=?";
        ItemVenda retorno = new ItemVenda();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, itemVenda.getIdItemVenda());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                Produto produto = new Produto();
                Venda venda = new Venda();
                itemVenda.setIdItemVenda(resultado.getLong("idItemVenda"));
                itemVenda.setQuantidade(resultado.getInt("quantidade"));
                itemVenda.setValor(resultado.getDouble("valor"));

                produto.setIdProduto(resultado.getLong("idProduto"));
                venda.setIdVenda(resultado.getInt("idVenda"));

                ProdutoDAO produtoDAO = new ProdutoDAO();
                produtoDAO.setConnection(connection);
                produto = produtoDAO.buscar(produto);

                VendaDAO vendaDAO = new VendaDAO();
                vendaDAO.setConnection(connection);
                venda = vendaDAO.buscar(venda);

                itemVenda.setProduto(produto);
                itemVenda.setVenda(venda);

                retorno = itemVenda;
            }
        } catch (SQLException ex) {
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

}
