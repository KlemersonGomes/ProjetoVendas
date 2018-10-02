/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.projetosi.Utils;

/**
 *
 * @author IANCA
 */
public class DatabaseFactory {

    public static Database getDatabase(String nome) {
        if (nome.equals("postgresql")) {
            return new DatabasePostgres();
        } else if (nome.equals("mysql")) {
            return new DatabaseMySQL();
        }
        return null;
    }
}
