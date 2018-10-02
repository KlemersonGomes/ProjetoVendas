/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.projetosi.dao;

import br.com.projetosi.model.Categoria;
import br.com.projetosi.model.Produto;
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
public class ProdutoDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Produto produto) {
        String sql = "INSERT INTO produtos(nome, preco, quantidade, idCategoria) VALUES(?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setInt(3, produto.getQuantidade());
            stmt.setLong(4, produto.getCategoria().getIdCategoria());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Produto produto) {
        String sql = "UPDATE produtos SET nome=?, preco=?, quantidade=?, idCategoria=? WHERE idProduto=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setInt(3, produto.getQuantidade());
            stmt.setLong(4, produto.getCategoria().getIdCategoria());
            stmt.setLong(5, produto.getIdProduto());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Produto produto) {
        String sql = "DELETE FROM produtos WHERE idCliente=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, produto.getIdProduto());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Produto> listar() {
        String sql = "SELECT * FROM produtos";
        List<Produto> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Produto produto = new Produto();
                Categoria categoria = new Categoria();
                produto.setIdProduto(resultado.getLong("idProduto"));
                produto.setNome(resultado.getString("nome"));
                produto.setPreco(resultado.getDouble("preco"));
                produto.setQuantidade(resultado.getInt("quantidade"));
                categoria.setIdCategoria(resultado.getInt("idCategoria"));

                //Obtendo os dados completos da Categoria associada ao Produto
                CategoriaDAO categoriaDAO = new CategoriaDAO();
                categoriaDAO.setConnection(connection);
                categoria = categoriaDAO.buscar(categoria);

                produto.setCategoria(categoria);
                retorno.add(produto);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public List<Produto> listarPorCategoria(Categoria categoria) {
        String sql = "SELECT * FROM produtos WHERE idCategoria=?";
        List<Produto> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, categoria.getIdCategoria());
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Produto produto = new Produto();
                produto.setIdProduto(resultado.getLong("idProduto"));
                produto.setNome(resultado.getString("nome"));
                produto.setPreco(resultado.getDouble("preco"));
                produto.setQuantidade(resultado.getInt("quantidade"));
                categoria.setIdCategoria(resultado.getInt("idCategoria"));
                produto.setCategoria(categoria);
                retorno.add(produto);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Produto buscar(Produto produto) {
        String sql = "SELECT * FROM produtos WHERE idProduto=?";
        Produto retorno = new Produto();
        Categoria categoria = new Categoria();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, produto.getIdProduto());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno.setIdProduto(resultado.getLong("idProduto"));
                retorno.setNome(resultado.getString("nome"));
                retorno.setPreco(resultado.getDouble("preco"));
                retorno.setQuantidade(resultado.getInt("quantidade"));
                categoria.setIdCategoria(resultado.getInt("idCategoria"));
                retorno.setCategoria(categoria);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

}
