/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.vendas.model.dao;

import java.util.List;

/**
 *
 * @author IANCA
 */
public class DaoGeneric<T> {

    public void save(T t) {

    }

    public T getObject(Long id) {
        T t = null;
        return t;
    }

    public List list() {
        List<T> t = null;
        return t;
    }

    public void remove(T t) {

    }

    public void update(T t) {

    }
}
