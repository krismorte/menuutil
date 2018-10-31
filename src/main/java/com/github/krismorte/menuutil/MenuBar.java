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
import java.util.List;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import com.github.krismorte.menuutil.annotation.MenuClass;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JMenu;

/**
 *
 * @author krismorte
 */
public abstract class MenuBar extends JMenuBar {

    public abstract MenuBar getFullVersion() throws Exception;

    public abstract MenuBar getRestrictVersion(String[] perfis) throws Exception;

    public void setupMenu(Class classeMenu) throws Exception {
        try {
            MenuClass annotation = (MenuClass) classeMenu.getAnnotation(MenuClass.class);
            JMenu menu = new JMenu(annotation.text());

            MenuItem menuItem = MenuItem.getInstance(classeMenu);
            for(MenuItem item : menuItem.getItens()){
                menu.add(item.getItem());
            }
            
           /*List<ItemMethod> itens= extractOrederedList(classeMenu);
            for (ItemMethod item : itens) {
                 if (item.name().contains(":")) {
                    String[] twoPart = item.name().split(":");
                    JMenu jItemParent = new JMenu(twoPart[0]);
                    JMenuItem jItem = new JMenuItem(item.text());
                    jItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                method.invoke(classeMenu.newInstance(), null);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    jItemParent.add(jItem);
                    menu.add(jItemParent);
                } else {
                    JMenuItem jItem = new JMenuItem(item.text());
                    jItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                method.invoke(classeMenu.newInstance(), null);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    menu.add(jItem);
                }
            }
            
            for (Method method : classeMenu.getDeclaredMethods()) {
                ItemMethod item = (ItemMethod) method.getAnnotation(ItemMethod.class);
                if (item.name().contains(":")) {
                    String[] twoPart = item.name().split(":");
                    JMenu jItemParent = new JMenu(twoPart[0]);
                    JMenuItem jItem = new JMenuItem(item.text());
                    jItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                method.invoke(classeMenu.newInstance(), null);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    jItemParent.add(jItem);
                    menu.add(jItemParent);
                } else {
                    JMenuItem jItem = new JMenuItem(item.text());
                    jItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                method.invoke(classeMenu.newInstance(), null);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    menu.add(jItem);
                }

            }*/
            this.add(menu);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("MontaMenu: " + ex.getMessage(), ex);
        }
    }

    /*private List<JMenuItem> extractOrederedList(Class menuClass) {
        List<JMenuItem> list = new ArrayList<>();
        for (Method method : menuClass.getDeclaredMethods()) {
            ItemMethod item = (ItemMethod) method.getAnnotation(ItemMethod.class);
            list.add(MenuItem.getInstance(method, menuClass, item));
        }
        Collections.sort(listItem, new Comparator<ItemMethod>() {
            @Override
            public int compare(ItemMethod m2, ItemMethod m1) {
                return Integer.compare(m1.order(), m2.order());//m1.order().compareTo(m2.order());
            }
        });
        return list;
    }*/

    /*public List<JMenuItem> getItens(Class classeMenu) throws Exception {
        List<JMenuItem> list = new ArrayList<>();
        try {
            for (Method method : classeMenu.getDeclaredMethods()) {
                ItemMethod item = (ItemMethod) method.getAnnotation(ItemMethod.class);
                System.out.println(item.parent()+", "+item.text());
                if (!item.parent().equals("")) {
                    JMenu jItemParent = list
                            .stream()
                            .filter(l -> l.getText().equals(item.parent()))
                            .findFirst()
                            .orElse(new JMenu(item.parent()));
                    JMenuItem jItem = new JMenuItem(item.text());
                    jItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                method.invoke(classeMenu.newInstance(), null);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    jItemParent.add(jItem);
                    list.add(jItemParent);
                } else {
                    JMenuItem jItem = new JMenuItem(item.text());
                    jItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                method.invoke(classeMenu.newInstance(), null);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    list.add(jItem);
                }

            }
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("MontaMenu: " + ex.getMessage(), ex);
        }
    }*/
}
