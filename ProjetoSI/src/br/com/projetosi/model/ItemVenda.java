/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.projetosi.model;

import java.io.Serializable;

/**
 *
 * @author IANCA
 */
public class ItemVenda implements Serializable {

    private long idItemVenda;
    private int quantidade;
    private double valor;
    private Produto produto;
    private Venda venda;

    public ItemVenda() {
    }

    public long getIdItemVenda() {
        return idItemVenda;
    }

    public void setIdItemVenda(long idItemVenda) {
        this.idItemVenda = idItemVenda;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

}
