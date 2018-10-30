/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.krismorte.menuutil.util;

import com.github.krismorte.menuutil.Item;
import com.github.krismorte.menuutil.ItemAcesso;
import com.github.krismorte.menuutil.Menu;
import com.github.krismorte.menuutil.MenuBarra;
import com.github.krismorte.menuutil.dao.ItemAcessoDao;
import com.github.krismorte.menuutil.dao.ItemDao;
import com.github.krismorte.menuutil.dao.MenuBarraDao;
import com.github.krismorte.menuutil.dao.MenuDao;
import com.github.krismorte.menuutil.interfaces.iAtualizaGUI;
import com.github.krismorte.menuutil.interfaces.iPopup;
import java.awt.event.ActionEvent;
import javax.persistence.EntityManagerFactory;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author C007329
 */
public class PopupMenu extends iPopup {

    //private JMenuItem updOrdem = new JMenuItem("atualizar ordem");
    /*private JMenuItem subirOrdem = new JMenuItem("acima");
    private JMenuItem baixarOrdem = new JMenuItem("abaixo");
    private JMenuItem updTexto = new JMenuItem("atualizar nome");
    private JMenuItem delRemover = new JMenuItem("remover");*/
    private EntityManagerFactory entityManagerFactory;

    private iAtualizaGUI tela;
    private JTable tabela;
    private int colunaItem = 2;

    private PopupMenu(iAtualizaGUI tela, JTable tabela, int colunaItem, EntityManagerFactory emf) {
        this.tela = tela;
        this.tabela = tabela;
        this.colunaItem = colunaItem;
        //iniciaJMenuItem(updTexto);
        //iniciaJMenuItem(delRemover);
        entityManagerFactory = emf;
    }

    public static PopupMenu getFullVersion(iAtualizaGUI tela, JTable tabela, int colunaItem, EntityManagerFactory emf) {
        PopupMenu popupMenu = new PopupMenu(tela, tabela, colunaItem, emf);
        popupMenu.iniciaJMenuItem(new JMenuItem(LabelUtil.JMENU_UP));
        popupMenu.iniciaJMenuItem(new JMenuItem(LabelUtil.JMENU_DOWN));
        popupMenu.iniciaJMenuItem(new JMenuItem(LabelUtil.JMENU_NAME));
        popupMenu.iniciaJMenuItem(new JMenuItem(LabelUtil.JMENU_ENABLE));
        popupMenu.iniciaJMenuItem(new JMenuItem(LabelUtil.JMENU_REMOVE));
        return popupMenu;
    }

    public static PopupMenu getSimpleVersion(iAtualizaGUI tela, JTable tabela, int colunaItem, EntityManagerFactory emf) {
        PopupMenu popupMenu = new PopupMenu(tela, tabela, colunaItem, emf);
        popupMenu.iniciaJMenuItem(new JMenuItem(LabelUtil.JMENU_NAME));
        popupMenu.iniciaJMenuItem(new JMenuItem(LabelUtil.JMENU_ENABLE));
        popupMenu.iniciaJMenuItem(new JMenuItem(LabelUtil.JMENU_REMOVE));
        return popupMenu;
    }

    public static PopupMenu getJustRemove(iAtualizaGUI tela, JTable tabela, int colunaItem, EntityManagerFactory emf) {
        PopupMenu popupMenu = new PopupMenu(tela, tabela, colunaItem, emf);
        popupMenu.iniciaJMenuItem(new JMenuItem(LabelUtil.JMENU_REMOVE));
        return popupMenu;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getActionCommand().equals(LabelUtil.JMENU_UP)) {
                alterarOrdem(tabela.getSelectedRow(), true);
                tela.atualizarTela();
            } else if (e.getActionCommand().equals(LabelUtil.JMENU_DOWN)) {
                alterarOrdem(tabela.getSelectedRow(), false);
                tela.atualizarTela();
            } else if (e.getActionCommand().equals(LabelUtil.JMENU_NAME)) {
                String texto = recebeNovoValor(LabelUtil.JMENU_ALTER_TEXT);
                alterarTexto(tabela.getSelectedRow(), texto);
                tela.atualizarTela();
            } else if (e.getActionCommand().equals(LabelUtil.JMENU_ENABLE)) {
                habilitar(tabela.getSelectedRow());
                tela.atualizarTela();
            } else if (e.getActionCommand().equals(LabelUtil.JMENU_REMOVE)) {
                remover(tabela.getSelectedRow());
                tela.atualizarTela();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "PopupMenu: " + ex.getMessage());
        }
    }

    private void alteraOrdemMenuAcima(Menu menu) {
        MenuDao menuDao = new MenuDao(Menu.class, entityManagerFactory);
        menu.setOrdem(menu.getOrdem() - 1);
        menuDao.update(menu);
    }

    private void alteraOrdemMenuAbaixo(Menu menu) {
        MenuDao menuDao = new MenuDao(Menu.class, entityManagerFactory);
        menu.setOrdem(menu.getOrdem() + 1);
        menuDao.update(menu);
    }

    private void alteraOrdemItemAcima(Item item) {
        ItemDao itemDao = new ItemDao(Item.class, entityManagerFactory);
        item.setOrdem(item.getOrdem() - 1);
        itemDao.update(item);
    }

    private void alteraOrdemItemAbaixo(Item item) {
        ItemDao itemDao = new ItemDao(Item.class, entityManagerFactory);
        item.setOrdem(item.getOrdem() + 1);
        itemDao.update(item);
    }

    private void alterarOrdem(int linhaTab, boolean acima) {
        if (getObjectFromJTable(linhaTab) instanceof Menu) {
            Menu menu = (Menu) getObjectFromJTable(linhaTab);
            if (acima) {
                alteraOrdemMenuAcima(menu);
                if ((linhaTab - 1) >= 0) {
                    if (getObjectFromJTable(linhaTab - 1) instanceof Menu) {
                        Menu menu2 = (Menu) getObjectFromJTable(linhaTab - 1);
                        alteraOrdemMenuAbaixo(menu2);
                    } else if (getObjectFromJTable(linhaTab - 1) instanceof Item) {
                        Item item = (Item) getObjectFromJTable(linhaTab - 1);
                        alteraOrdemItemAbaixo(item);
                    }
                }
            } else {
                alteraOrdemMenuAbaixo(menu);
                if ((linhaTab + 1) <= tabela.getRowCount()) {
                    if (getObjectFromJTable(linhaTab + 1) instanceof Menu) {
                        Menu menu2 = (Menu) getObjectFromJTable(linhaTab + 1);
                        alteraOrdemMenuAcima(menu2);
                    } else if (getObjectFromJTable(linhaTab + 1) instanceof Item) {
                        Item item = (Item) getObjectFromJTable(linhaTab + 1);
                        alteraOrdemItemAcima(item);
                    }

                }
            }
        } else if (getObjectFromJTable(linhaTab) instanceof Item) {
            Item item = (Item) getObjectFromJTable(linhaTab);

            if (acima) {
                alteraOrdemItemAcima(item);
                if ((linhaTab - 1) >= 0) {
                    if (getObjectFromJTable(linhaTab - 1) instanceof Menu) {
                        Menu menu2 = (Menu) getObjectFromJTable(linhaTab - 1);
                        alteraOrdemMenuAbaixo(menu2);
                    } else if (getObjectFromJTable(linhaTab - 1) instanceof Item) {
                        Item item2 = (Item) getObjectFromJTable(linhaTab - 1);
                        alteraOrdemItemAbaixo(item2);
                    }
                }
            } else {
                alteraOrdemItemAbaixo(item);
                if (tabela.getRowCount() <= (linhaTab + 1)) {
                    if (getObjectFromJTable(linhaTab + 1) instanceof Menu) {
                        Menu menu2 = (Menu) getObjectFromJTable(linhaTab + 1);
                        alteraOrdemMenuAcima(menu2);
                    } else if (getObjectFromJTable(linhaTab + 1) instanceof Item) {
                        Item item2 = (Item) getObjectFromJTable(linhaTab + 1);
                        alteraOrdemItemAcima(item2);
                    }
                }
            }
        }
    }

    private void alterarTexto(int linhaTab, String texto) {
        if (getObjectFromJTable(linhaTab) instanceof MenuBarra) {
            MenuBarra menuBarra = (MenuBarra) getObjectFromJTable(linhaTab);
            menuBarra.setNome(texto);
            MenuBarraDao menuBarraDao = new MenuBarraDao(MenuBarra.class, entityManagerFactory);
            menuBarraDao.update(menuBarra);
        } else if (getObjectFromJTable(linhaTab) instanceof Menu) {
            Menu menu = (Menu) getObjectFromJTable(linhaTab);
            MenuDao menuDao = new MenuDao(Menu.class, entityManagerFactory);
            menu.setNome(texto);
            menuDao.update(menu);
        } else if (getObjectFromJTable(linhaTab) instanceof Item) {
            Item item = (Item) getObjectFromJTable(linhaTab);
            ItemDao itemDao = new ItemDao(Item.class, entityManagerFactory);
            item.setNome(texto);
            itemDao.update(item);
        }
    }

    private void habilitar(int linhaTab) {
        if (getObjectFromJTable(linhaTab) instanceof Menu) {
            Menu menu = (Menu) getObjectFromJTable(linhaTab);
            MenuDao menuDao = new MenuDao(Menu.class, entityManagerFactory);

            if (menu.getHabilitado()) {
                menu.setHabilitado(Boolean.FALSE);
            } else {
                menu.setHabilitado(Boolean.TRUE);
            }

            menuDao.update(menu);
        } else if (getObjectFromJTable(linhaTab) instanceof Item) {
            Item item = (Item) getObjectFromJTable(linhaTab);
            ItemDao itemDao = new ItemDao(Item.class, entityManagerFactory);
            if (item.getHabilitado()) {
                item.setHabilitado(Boolean.FALSE);
            } else {
                item.setHabilitado(Boolean.TRUE);
            }
            itemDao.update(item);
        }
    }

    private void remover(int linhaTab) {
        if (getObjectFromJTable(linhaTab) instanceof MenuBarra) {
            MenuBarra menuBarra = (MenuBarra) getObjectFromJTable(linhaTab);
            MenuBarraDao menuBarraDao = new MenuBarraDao(MenuBarra.class, entityManagerFactory);
            MenuDao menuDao = new MenuDao(Menu.class, entityManagerFactory);

            for (Menu menu : menuDao.listByMenuBar(menuBarra)) {
                deleteItemPorMenu(menu);
            }
            menuDao.deleteByMenuBarra(menuBarra);
            menuBarraDao.delete(menuBarra);
        } else if (getObjectFromJTable(linhaTab) instanceof Menu) {
            Menu menu = (Menu) getObjectFromJTable(linhaTab);
            MenuDao menuDao = new MenuDao(Menu.class, entityManagerFactory);

            deleteItemPorMenu(menu);
            menuDao.delete(menu);
        } else if (getObjectFromJTable(linhaTab) instanceof Item) {
            ItemDao itemDao = new ItemDao(Item.class, entityManagerFactory);
            Item item = (Item) getObjectFromJTable(linhaTab);
            deleteItemAcesso(item);
            itemDao.delete(item);
            //deleteItemPorMenu(item.getMenu());
        } else if (getObjectFromJTable(linhaTab) instanceof ItemAcesso) {
            ItemAcesso itemAcesso = (ItemAcesso) getObjectFromJTable(linhaTab);
            ItemAcessoDao itemAcessoDao = new ItemAcessoDao(ItemAcesso.class, entityManagerFactory);
            itemAcessoDao.delete(itemAcesso.getId());
        }
    }

    private void deleteItemPorMenu(Menu menu) {
        ItemDao itemDao = new ItemDao(Item.class, entityManagerFactory);
        ItemAcessoDao itemAcessoDao = new ItemAcessoDao(ItemAcesso.class, entityManagerFactory);
        for (Item item : itemDao.listByMenu(menu)) {
            itemAcessoDao.deleteByItem(item);
        }
        itemDao.deleteByMenu(menu);
    }

    private void deleteItemAcesso(Item item) {
        ItemAcessoDao itemAcessoDao = new ItemAcessoDao(ItemAcesso.class, entityManagerFactory);
        itemAcessoDao.deleteByItem(item);
    }

    private Object getObjectFromJTable(int linha) {
        return tabela.getValueAt(linha, colunaItem);
    }

    /**
     * @param tabela the tabela to set
     */
    @Override
    public void setTabela(JTable tabela) {
        this.tabela = tabela;
    }

}
