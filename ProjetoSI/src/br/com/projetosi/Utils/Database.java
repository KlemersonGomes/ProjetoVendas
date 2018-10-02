/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.projetosi.Utils;

import java.sql.Connection;

/**
 *
 * @author IANCA
 */
public interface Database {

    public Connection conectar();

    public void desconectar(Connection conn);
}
