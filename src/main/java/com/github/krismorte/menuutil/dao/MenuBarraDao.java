/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.krismorte.menuutil.dao;

import com.github.krismorte.menuutil.MenuBarra;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author c007329
 */
public class MenuBarraDao extends GenericJPA<MenuBarra> {

    public MenuBarraDao(Class<MenuBarra> entityClass, EntityManagerFactory emf) {
        super(entityClass, emf);
    }

    @Override
    public boolean save(MenuBarra entity) {
        super.beginTransaction();
        super.save(entity); //To change body of generated methods, choose Tools | Templates.
        super.commitAndCloseTransaction();
        return true;
    }

    @Override
    public boolean update(MenuBarra entity) {
        super.beginTransaction();
        super.update(entity); //To change body of generated methods, choose Tools | Templates.
        super.commitAndCloseTransaction();
        return true;
    }

    @Override
    public boolean delete(MenuBarra entity) {
        super.beginTransaction();
        super.delete(entity); //To change body of generated methods, choose Tools | Templates.
        super.commitAndCloseTransaction();
        return true;
    }

    public MenuBarra getMain() {
        super.beginTransaction();
        MenuBarra lst = super.findOneResult("select m from MenuBarra m where m.principal = true");
        super.commitAndCloseTransaction();
        return lst;
    }

    public MenuBarra getOther(String menuName) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("menuName", menuName);
        super.beginTransaction();
        MenuBarra lst = super.findOneResult("select m from MenuBarra m where m.nome = :menuName");
        super.commitAndCloseTransaction();
        return lst;
    }

    /*Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("linha", linha);*/
    public List<MenuBarra> listAll() {
        super.beginTransaction();
        List<MenuBarra> lst = super.findFilterResult("from MenuBarra");
        super.commitAndCloseTransaction();
        return lst;
    }

}
