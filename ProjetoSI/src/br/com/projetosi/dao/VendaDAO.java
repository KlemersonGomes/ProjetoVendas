/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.projetosi.dao;

import br.com.projetosi.model.Cliente;
import br.com.projetosi.model.ItemVenda;
import br.com.projetosi.model.Venda;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author IANCA
 */
public class VendaDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Venda venda) {
        String sql = "INSERT INTO vendas(data, valor, pago, idCliente) VALUES(?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(venda.getData()));
            stmt.setDouble(2, venda.getValor());
            stmt.setBoolean(3, venda.getPago());
            stmt.setLong(4, venda.getCliente().getIdCliente());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Venda venda) {
        String sql = "UPDATE clientes SET data=?, valor=?, pago=?, idCliente=? WHERE idVenda=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(venda.getData()));
            stmt.setDouble(2, venda.getValor());
            stmt.setBoolean(3, venda.getPago());
            stmt.setLong(4, venda.getCliente().getIdCliente());
            stmt.setLong(5, venda.getIdVenda());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Venda venda) {
        String sql = "DELETE FROM vendas WHERE idVenda=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, venda.getIdVenda());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Venda> listar() {
        String sql = "SELECT * FROM vendas";
        List<Venda> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Venda venda = new Venda();
                Cliente cliente = new Cliente();
                List<ItemVenda> itensVenda = new ArrayList();

                venda.setIdVenda(resultado.getInt("idVenda"));
                venda.setData(resultado.getDate("data").toLocalDate());
                venda.setValor(resultado.getDouble("valor"));
                venda.setPago(resultado.getBoolean("pago"));
                cliente.setIdCliente(resultado.getInt("idCliente"));

                ClienteDAO clienteDAO = new ClienteDAO();
                clienteDAO.setConnection(connection);
                cliente = clienteDAO.buscar(cliente);

                ItemVendaDAO itemVendaDAO = new ItemVendaDAO();
                itemVendaDAO.setConnection(connection);
                itensVenda = itemVendaDAO.listarPorVenda(venda);

                venda.setCliente(cliente);
                venda.setItensVenda(itensVenda);
                retorno.add(venda);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Venda buscar(Venda venda) {
        String sql = "SELECT * FROM vendas WHERE idVenda=?";
        Venda retorno = new Venda();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, venda.getIdVenda());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                Cliente cliente = new Cliente();
                venda.setIdVenda(resultado.getLong("idVenda"));
                venda.setData(resultado.getDate("data").toLocalDate());
                venda.setValor(resultado.getDouble("valor"));
                venda.setPago(resultado.getBoolean("pago"));
                cliente.setIdCliente(resultado.getLong("idCliente"));
                venda.setCliente(cliente);
                retorno = venda;
            }
        } catch (SQLException ex) {
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Venda buscarUltimaVenda() {
        String sql = "SELECT max(idVenda) FROM vendas";
        Venda retorno = new Venda();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();

            if (resultado.next()) {
                retorno.setIdVenda(resultado.getLong("max"));
                return retorno;
            }
        } catch (SQLException ex) {
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Map<Integer, ArrayList> listarQuantidadeVendasPorMes() {
        String sql = "select count(idVenda), extract(year from data) as ano, extract(month from data) as mes from vendas group by ano, mes order by ano, mes";
        Map<Integer, ArrayList> retorno = new HashMap();

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();

            while (resultado.next()) {
                ArrayList linha = new ArrayList();
                if (!retorno.containsKey(resultado.getInt("ano"))) {
                    linha.add(resultado.getInt("mes"));
                    linha.add(resultado.getInt("count"));
                    retorno.put(resultado.getInt("ano"), linha);
                } else {
                    ArrayList linhaNova = retorno.get(resultado.getInt("ano"));
                    linhaNova.add(resultado.getInt("mes"));
                    linhaNova.add(resultado.getInt("count"));
                }
            }
            return retorno;
        } catch (SQLException ex) {
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

}
