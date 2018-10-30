/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.krismorte.menuutil.dao;

import com.github.krismorte.menuutil.Menu;
import com.github.krismorte.menuutil.MenuBarra;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author c007329
 */
public class MenuDao extends GenericJPA<Menu> {

    public MenuDao(Class<Menu> entityClass, EntityManagerFactory emf) {
        super(entityClass, emf);
    }

    @Override
    public boolean save(Menu entity) {
        super.beginTransaction();
        super.save(entity); //To change body of generated methods, choose Tools | Templates.
        super.commitAndCloseTransaction();
        return true;
    }

    @Override
    public boolean delete(Menu entity) {
        super.beginTransaction();
        super.delete(entity); //To change body of generated methods, choose Tools | Templates.
        super.commitAndCloseTransaction();
        return true;
    }

    public boolean deleteByMenuBarra(MenuBarra menuBarra) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("menuBarra", menuBarra);
        super.beginTransaction();        
        List<Menu> entities = super.findFilterResult("select m from Menu m where m.menuBarra=:menuBarra order by m.ordem", parameters);
        for (Menu entity : entities) {
            super.delete(entity); //To change body of generated methods, choose Tools | Templates.    
        }
        super.commitAndCloseTransaction();
        return true;
    }

    @Override
    public boolean update(Menu entity) {
        super.beginTransaction();
        super.update(entity);
        super.commitAndCloseTransaction();
        return true;
    }

    public List<Menu> listByMenuBar(MenuBarra menuBarra) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("menuBarra", menuBarra);
        super.beginTransaction();
        List<Menu> lst = super.findFilterResult("select m from Menu m where m.menuBarra=:menuBarra order by m.ordem", parameters);
        super.commitAndCloseTransaction();
        return lst;
    }

    public List<Menu> listEnableByMenuBar(MenuBarra menuBarra) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("menuBarra", menuBarra);
        super.beginTransaction();
        List<Menu> lst = super.findFilterResult("select m from Menu m where m.menuBarra=:menuBarra and m.habilitado=true order by m.ordem", parameters);
        super.commitAndCloseTransaction();
        return lst;
    }

    private List<Menu> listByMenu(Menu menu) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("menu", menu);
        super.beginTransaction();
        List<Menu> lst = super.findFilterResult("select m from Menu m where m.menuSuperior=:menu order by m.ordem", parameters);
        super.commitAndCloseTransaction();
        return lst;
    }

    public List<Menu> listEnableByMenu(Menu menu) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("menu", menu);
        super.beginTransaction();
        List<Menu> lst = super.findFilterResult("select m from Menu m where m.menuSuperior=:menu and m.habilitado=true order by m.ordem", parameters);
        super.commitAndCloseTransaction();
        return lst;
    }

    private List<Menu> listAll() {
        super.beginTransaction();
        List<Menu> lst = super.findAll();
        super.commitAndCloseTransaction();
        return lst;
    }

}
