/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.krismorte.menuutil.dao;

import com.github.krismorte.menuutil.Item;
import com.github.krismorte.menuutil.ItemAcesso;
import com.github.krismorte.menuutil.MenuBarra;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author c007329
 */
public class ItemAcessoDao extends GenericJPA<ItemAcesso> {

    public ItemAcessoDao(Class<ItemAcesso> entityClass, EntityManagerFactory emf) {
        super(entityClass, emf);
    }

    @Override
    public boolean save(ItemAcesso entity) {
        super.beginTransaction();
        super.save(entity); //To change body of generated methods, choose Tools | Templates.
        super.commitAndCloseTransaction();
        return true;
    }

    @Override
    public boolean delete(ItemAcesso entity) {
        super.beginTransaction();
        super.delete(entity); //To change body of generated methods, choose Tools | Templates.
        super.commitAndCloseTransaction();
        return true;
    }

    @Override
    public void delete(Long idEntity) {
        super.beginTransaction();
        super.delete(idEntity); //To change body of generated methods, choose Tools | Templates.
        super.commitAndCloseTransaction();
    }
    
    public boolean delete(List<ItemAcesso> entities) {
        super.beginTransaction();
        for (ItemAcesso entity : entities) {
            super.delete(entity); //To change body of generated methods, choose Tools | Templates.    
        }
        super.commitAndCloseTransaction();
        return true;
    }
    
    public boolean deleteByItem(Item item) {        
        super.beginTransaction();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("item", item);
        List<ItemAcesso> itensAcesso = super.findFilterResult("select ia from ItemAcesso ia where ia.item=:item ", parameters);
        for (ItemAcesso entity : itensAcesso) {
            super.delete(entity); //To change body of generated methods, choose Tools | Templates.    
        }
        super.commitAndCloseTransaction();
        return true;
    }

    public List<ItemAcesso> listByItem(Item item) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("item", item);
        super.beginTransaction();
        List<ItemAcesso> lst = super.findFilterResult("select ia from ItemAcesso ia where ia.item=:item ", parameters);
        super.commitAndCloseTransaction();
        return lst;
    }

    public List<ItemAcesso> listByKey(String[] chaves) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("keys", Arrays.asList(chaves));
        super.beginTransaction();
        List<ItemAcesso> lst = super.findFilterResult("select ia from ItemAcesso ia where ia.chave IN :keys ", parameters);
        super.commitAndCloseTransaction();
        return lst;
    }

    public List<ItemAcesso> listAllByMenuBar(MenuBarra menuBarra) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("menuBarra", menuBarra);
        super.beginTransaction();
        List<ItemAcesso> lst = super.findFilterResult("select i from ItemAcesso i where i.item.menu IN ( select m from Menu m where m.menuBarra =:menuBarra) order by i.chave", parameters);
        super.commitAndCloseTransaction();
        return lst;
    }

    private List<ItemAcesso> listAll() {
        super.beginTransaction();
        List<ItemAcesso> lst = super.findFilterResult("select i from ItemAcesso i order by i.chave");
        super.commitAndCloseTransaction();
        return lst;
    }

}
