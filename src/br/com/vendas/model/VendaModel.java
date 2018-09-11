/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.vendas.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author IANCA
 */
public class VendaModel {

    private long id;
    private List<ProdutoModel> produtos;
    private UsuarioModel usuario;
    private double valorTotal;
    private double desconto;
    private double quantidade;
    private Date data;
    private double valorRecebido;

    public VendaModel(List<ProdutoModel> produtos, double quantidade, double valorRecebido) {
        this.produtos = produtos;
        this.quantidade = quantidade;
        this.valorRecebido = valorRecebido;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<ProdutoModel> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ProdutoModel> produtos) {
        this.produtos = produtos;
    }

    public UsuarioModel getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioModel usuario) {
        this.usuario = usuario;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public double getValorRecebido() {
        return valorRecebido;
    }

    public void setValorRecebido(double valorRecebido) {
        this.valorRecebido = valorRecebido;
    }

}
