/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.projetosi.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author IANCA
 */
public class Venda implements Serializable{

    private long idVenda;
    private LocalDate data;
    private double valor;
    private boolean pago;
    private List<ItemVenda> itensVenda;
    private Cliente cliente;

    public Venda() {
    }

    public Venda(long idvenda, LocalDate data, double valor, boolean pago) {
        this.idVenda = idvenda;
        this.data = data;
        this.valor = valor;
        this.pago = pago;
    }

    public long getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(long idVenda) {
        this.idVenda = idVenda;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public boolean getPago() {
        return pago;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
    }

    public List<ItemVenda> getItensVenda() {
        return itensVenda;
    }

    public void setItensVenda(List<ItemVenda> itensVenda) {
        this.itensVenda = itensVenda;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    

}
