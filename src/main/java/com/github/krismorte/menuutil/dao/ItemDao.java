/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.krismorte.menuutil.dao;

import com.github.krismorte.menuutil.Item;
import com.github.krismorte.menuutil.Menu;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author c007329
 */
public class ItemDao extends GenericJPA<Item> {

    public ItemDao(Class<Item> entityClass, EntityManagerFactory emf) {
        super(entityClass, emf);
    }

    @Override
    public boolean save(Item entity) {
        super.beginTransaction();
        super.save(entity); //To change body of generated methods, choose Tools | Templates.
        super.commitAndCloseTransaction();
        return true;
    }
    
     @Override
    public boolean update(Item entity) {
        super.beginTransaction();
        super.update(entity); //To change body of generated methods, choose Tools | Templates.
        super.commitAndCloseTransaction();
        return true;
    }

    @Override
    public boolean delete(Item entity) {
        super.beginTransaction();
        super.delete(entity); //To change body of generated methods, choose Tools | Templates.
        super.commitAndCloseTransaction();
        return true;
    }

    public boolean delete(List<Item> entities) {
        super.beginTransaction();
        for (Item entity : entities) {
            super.delete(entity); //To change body of generated methods, choose Tools | Templates.    
        }
        super.commitAndCloseTransaction();
        return true;
    }

    public boolean deleteByMenu(Menu menu) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("menu", menu);
        super.beginTransaction();
        List<Item> itens = super.findFilterResult("select i from Item i where i.menu=:menu order by i.ordem", parameters);
        for (Item entity : itens) {
            super.delete(entity); //To change body of generated methods, choose Tools | Templates.    
        }
        super.commitAndCloseTransaction();
        return true;
    }

    public List<Item> listByMenu(Menu menu) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("menu", menu);
        super.beginTransaction();
        List<Item> lst = super.findFilterResult("select i from Item i where i.menu=:menu order by i.ordem", parameters);
        super.commitAndCloseTransaction();
        return lst;
    }

    public List<Item> listEnableByMenu(Menu menu) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("menu", menu);
        super.beginTransaction();
        List<Item> lst = super.findFilterResult("select i from Item i where i.menu=:menu and i.habilitado=true order by i.ordem", parameters);
        super.commitAndCloseTransaction();
        return lst;
    }

  
    
    private List<Item> listAll() {
        super.beginTransaction();
        List<Item> lst = super.findAll();
        super.commitAndCloseTransaction();
        return lst;
    }

}
