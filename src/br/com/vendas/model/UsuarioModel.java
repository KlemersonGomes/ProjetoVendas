/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.vendas.model;

/**
 *
 * @author IANCA
 */
public class UsuarioModel extends  PessoaModel{
    
    private String senha;
    private boolean master = false;
    public UsuarioModel(String nome, String telefone, String senha, boolean master) {
        super(nome, telefone);
        this.senha = senha;
        this.master = master;
        
        
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isMaster() {
        return master;
    }

    public void setMaster(boolean master) {
        this.master = master;
    }
    
}
