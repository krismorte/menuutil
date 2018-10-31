/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.krismorte.menuutil;

import com.github.krismorte.menuutil.annotation.ItemMethod;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 *
 * @author krisnamourtscf
 */
public class MenuItem {

    private int order;
    private JMenuItem item;
    private List<MenuItem> itens;

    private MenuItem() {
        itens = new ArrayList<>();
    }

    private MenuItem(int order, JMenuItem item) {
        this.order = order;
        this.item = item;
    }

    public static MenuItem getInstance(Class menuClass) {
        MenuItem menuItem = new MenuItem();
        for (Method method : menuClass.getDeclaredMethods()) {
            ItemMethod item = (ItemMethod) method.getAnnotation(ItemMethod.class);
            if (item.name().contains(":")) {
                String[] twoPart = item.name().split(":");
                JMenu jItemParent = new JMenu(twoPart[0]);
                JMenuItem jItem = new JMenuItem(item.text());
                jItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            method.invoke(menuClass.newInstance(), null);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                jItemParent.add(jItem);
                menuItem.getItens().add(new MenuItem(item.order(), jItemParent));
            } else {
                JMenuItem jItem = new JMenuItem(item.text());
                jItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            method.invoke(menuClass.newInstance(), null);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                menuItem.getItens().add(new MenuItem(item.order(), jItem));
            }
        }
        menuItem.sortList();
        return menuItem;
    }

    public List<MenuItem> getItens() {
        return itens;
    }

    public JMenuItem getItem() {
        return item;
    }
    
    
    private void sortList() {
        Collections.sort(itens, new Comparator<MenuItem>() {
            @Override
            public int compare(MenuItem m2, MenuItem m1) {
                return Integer.compare(m1.order, m2.order);//m1.order().compareTo(m2.order());
            }
        });
    }

}
