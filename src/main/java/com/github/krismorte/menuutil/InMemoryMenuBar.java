/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.krismorte.menuutil;

import java.util.List;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import com.github.krismorte.menuutil.annotation.MenuClass;

/**
 *
 * @author krismorte
 */
public class InMemoryMenuBar extends MenuBar {

    private List<Class> classes;

    public InMemoryMenuBar(List<Class> classes) {
        this.classes = classes;
    }
        
    
    @Override
    public MenuBar getFullVersion() throws Exception {
        for (Class classe : classes) {
            //MenuClass annotation = (MenuClass) classe.getAnnotation(MenuClass.class);            
            setupMenu(classe);
            /*for(JMenuItem item:getItens(classe)){
                menu.add(item);
            }*/
        }
        return this;
    }

    @Override
    public MenuBar getRestrictVersion(String[] perfis) throws Exception {
        for (Class classe : classes) {
            MenuClass annotation = (MenuClass) classe.getAnnotation(MenuClass.class);
            this.add(new JMenu(annotation.text()));
        }
        return this;
    }
    
}
