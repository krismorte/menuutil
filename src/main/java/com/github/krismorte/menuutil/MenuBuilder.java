/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.krismorte.menuutil;

import com.github.krismorte.menuutil.annotation.ItemMetodo;
import com.github.krismorte.menuutil.annotation.MenuClasse;
import com.github.krismorte.menuutil.dao.ComponenteMenuDao;
import com.github.krismorte.menuutil.dao.ItemAcessoDao;
import com.github.krismorte.menuutil.dao.ItemDao;
import com.github.krismorte.menuutil.dao.MenuBarraDao;
import com.github.krismorte.menuutil.dao.MenuDao;
import com.github.krismorte.menuutil.interfaces.ComponenteMenu;
import com.github.krismorte.menuutil.util.ReflectionUtil;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManagerFactory;
import javax.swing.JMenu;
import org.reflections.Reflections;

/**
 *
 * @author krisnamourtscf
 */
public class MenuBuilder {

    private boolean isInMemory;
    private MenuBarra inMemoryBar;
    private ReflectionUtil reflexaoUtil = new ReflectionUtil();
    private MenuBarraDao menuBarraDao;
    private ComponenteMenuDao componenteMenuDao;
    private MenuDao menuDao;
    private ItemDao itemDao;
    private ItemAcessoDao itemAcessoDao;
    private EntityManagerFactory entityManagerFactory;
    private List<Class> classes;

    private MenuBuilder(String packgePath, EntityManagerFactory emf) throws Exception {
        isInMemory = false;
        classes = new ArrayList<>();
        encontraClasses(packgePath);
        entityManagerFactory = emf;
        menuBarraDao = new MenuBarraDao(MenuBarra.class, emf);
        menuDao = new MenuDao(Menu.class, emf);
        itemDao = new ItemDao(Item.class, emf);
        itemAcessoDao = new ItemAcessoDao(ItemAcesso.class, emf);
        componenteMenuDao = new ComponenteMenuDao();
    }

    private MenuBuilder(String packgePath) throws Exception {
        isInMemory = true;
        classes = new ArrayList<>();
        encontraClasses(packgePath);
        inMemoryBar = new MenuBarra();
        inMemoryBar.setName("inMemory");
    }

    public static MenuBuilder getInMemoryInstance(String packgePath) throws Exception {
        return new MenuBuilder(packgePath);
    }

    public static MenuBuilder getInstance(String packgePath, EntityManagerFactory emf) throws Exception {
        MenuBuilder menuBuilder = new MenuBuilder(packgePath, emf);
        return menuBuilder;
    }

    public MenuBarra getFullVersion() throws Exception {
        MenuBarra menuBarra = menuBarraDao.getMain();
        montaMenuAdmin(menuBarra);
        return menuBarra;
    }

    public MenuBarra getRestrictVersion(String[] perfis) throws Exception {
        MenuBarra menuBarra = menuBarraDao.getMain();
        montaMenu(menuBarra, perfis);
        return menuBarra;
    }

    public MenuBarra getFullVersion(String menuName) throws Exception {
        MenuBarra menuBarra = menuBarraDao.getOther(menuName);
        montaMenuAdmin(menuBarra);
        return menuBarra;
    }

    public MenuBarra getRestrictVersion(String menuName, String[] perfis) throws Exception {
        MenuBarra menuBarra = menuBarraDao.getOther(menuName);
        montaMenu(menuBarra, perfis);
        return menuBarra;
    }

    private void montaMenuAdmin(MenuBarra menuBarra) throws Exception {
        try {
            for (Menu menu : menuDao.listEnableByMenuBar(menuBarra)) {

                configuraMenu(menu);
                Class classe = getClasse(menu.getNome());

                for (ComponenteMenu componenteMenu : componenteMenuDao.buscaComponentesHabilitados(entityManagerFactory, menu)) {
                    if (componenteMenu instanceof Menu) {
                        Menu subMenu = (Menu) componenteMenu;

                        configuraMenu(subMenu);

                        if (subMenu.getMenuSuperior() != null) {
                            for (Item item : itemDao.listEnableByMenu(subMenu)) {
                                classe = getClasse(subMenu.toString());
                                configuraItem(classe, subMenu, item);
                                subMenu.add(item);
                            }
                        }

                        menu.add(subMenu);
                    } else if (componenteMenu instanceof Item) {
                        if (menu.getMenuSuperior() == null) {
                            Item item = (Item) componenteMenu;
                            configuraItem(classe, menu, item);
                            menu.add(item);
                        }
                    }

                }
                if (menu.getMenuSuperior() == null) {
                    menuBarra.add(menu);
                }

            }

        } catch (Exception e) {
            throw new Exception("MontaMenu: " + e.getMessage(), e);
        }
    }

    private void montaMenu2() throws Exception {

        for (Class classe : classes) {
            MenuClasse menu = (MenuClasse) classe.getAnnotation(MenuClasse.class);
            inMemoryBar.add(new JMenu(menu.name()));
        }
    }

    private void montaMenu(MenuBarra menuBarra, String[] chaves) throws Exception {
        try {
            List<ItemAcesso> acessos = itemAcessoDao.listByKey(chaves);

            for (Menu menu : menuDao.listEnableByMenuBar(menuBarra)) {

                configuraMenu(menu);
                Class classe = getClasse(menu.toString());
                System.out.println("Aqui");
                for (ComponenteMenu componenteMenu : componenteMenuDao.buscaComponentesHabilitados(entityManagerFactory, menu)) {

                    if (componenteMenu instanceof Menu) {
                        Menu subMenu = (Menu) componenteMenu;
                        configuraMenu(subMenu);
                        if (subMenu.getMenuSuperior() != null) {
                            System.out.println("busca item");
                            for (Item item : itemDao.listEnableByMenu(subMenu)) {
                                classe = getClasse(subMenu.toString());
                                configuraItem(classe, subMenu, item, acessos);
                                subMenu.add(item);
                            }
                        }
                        menu.add(subMenu);
                    } else if (componenteMenu instanceof Item) {
                        if (menu.getMenuSuperior() == null) {
                            Item item = (Item) componenteMenu;
                            configuraItem(classe, menu, item, acessos);
                            menu.add(item);
                        }
                    }

                }
                if (menu.getMenuSuperior() == null) {
                    menuBarra.add(menu);
                }

            }
        } catch (Exception e) {
            throw new Exception("MontaMenu: " + e.getMessage(), e);
        }
    }

    private void configuraMenu(Menu menu) {
        menu.setText(menu.getNome());
        if (menu.getCaminhoImagem() != null) {
            menu.setIcon(new javax.swing.ImageIcon(getClass().getResource(menu.getCaminhoImagem())));
        }
    }

    private void configuraItem(Class classe, Menu menu, Item item) throws Exception {
        item.setText(item.getNome());
        if (menu.getId().equals(item.getMenu().getId())) {
            if (classe != null) {
                addMetodoItem(classe, item);
            }
            if (item.getCaminhoImagem() != null) {
                item.setIcon(new javax.swing.ImageIcon(getClass().getResource(item.getCaminhoImagem())));
            }
            menu.add(item);
            if (item.getSeparador()) {
                menu.addSeparator();
            }
        }
    }

    private void configuraItem(Class classe, Menu menu, Item item, List<ItemAcesso> acessos) throws Exception {
        item.setText(item.getNome());
        item.setEnabled(false);
        if (menu.getId().equals(item.getMenu().getId())) {

            for (ItemAcesso acesso : acessos) {
                if (item.getId() == acesso.getItem().getId()) {
                    if (classe != null) {
                        addMetodoItem(classe, item);
                    }
                    item.setEnabled(true);
                    break;
                }
            }

            if (item.getCaminhoImagem() != null) {
                item.setIcon(new javax.swing.ImageIcon(getClass().getResource(item.getCaminhoImagem())));
            }

            menu.add(item);
            if (item.getSeparador()) {
                menu.addSeparator();
            }
        }
    }

    private void addMetodoItem(Class classe, Item i) throws Exception {
        Method method = getMethod(classe, i.getNome());
        i.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    method.invoke(classe.newInstance(), null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void encontraClasses(String packgePath) throws Exception {
        try {

            Reflections reflections = new Reflections(packgePath);

            Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(MenuClasse.class);
            for (Class classe : annotated) {
                classes.add(classe);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("MontaMenu: " + ex.getMessage(), ex);
        }
    }

    private Class getClasse(String menuNome) throws Exception {
        Class classeMenu = null;
        try {
            for (Class classe : classes) {
                MenuClasse menu = (MenuClasse) classe.getAnnotation(MenuClasse.class);
                if (menu.name().toUpperCase().equals(menuNome.toUpperCase())) {
                    classeMenu = classe;
                    break;
                }

            }
            return classeMenu;
        } catch (Exception ex) {
            ex.printStackTrace();

            throw new Exception("MontaMenu: " + ex.getMessage(), ex);
        }
    }

    private Method getMethod(Class classeMenu, String itemNome) throws Exception {
        Method itemMethod = null;
        try {
            for (Method method : classeMenu.getDeclaredMethods()) {
                ItemMetodo item = (ItemMetodo) method.getAnnotation(ItemMetodo.class);
                if (item.name().toUpperCase().equals(itemNome.toUpperCase())) {
                    itemMethod = method;
                    break;
                }
            }
            return itemMethod;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("MontaMenu: " + ex.getMessage(), ex);
        }
    }

}
