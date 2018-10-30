/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.krismorte.menuutil.util;

import com.github.krismorte.menuutil.interfaces.ComponenteMenu;
import com.github.krismorte.menuutil.interfaces.iPopup;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author c007329
 */
public class InspectorUtil {
    
    public static Object getObjectFromList(List lista, String nome) {
        Object objeto = null;
        for (Object object : lista) {
            ComponenteMenu objectoComNome = (ComponenteMenu) object;
            if (objectoComNome.getNome().toUpperCase().equals(nome)) {
                objeto = objectoComNome;
                break;
            }
        }
        return objeto;
    }

    public static Boolean existingName(List lista, String nome) {
        Boolean exists = false;
        if(lista.isEmpty()){
            return exists;
        }
        for (Object object : lista) {
            ComponenteMenu objectoComNome = (ComponenteMenu) object;
            if (objectoComNome.getNome().toUpperCase().equals(nome)) {
                exists = true;
                break;
            }
        }
        return exists;
    }

    public static void fulfillJComboBox(JComboBox comboBox, List lista) {
        comboBox.removeAllItems();
        for (Object object : lista) {
            comboBox.addItem(((ComponenteMenu) object).getNome().toUpperCase());
        }
    }

    public static JScrollPane jTableDecorator(JPanel panel, String[] colunas, Object[][] linhas) {
        JTable table = new JTable(new SimpleTableModel(colunas, linhas));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setSize(panel.getSize());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }

    public static JScrollPane jTableDecoratorWithMenuPopup(JPanel panel,iPopup menuPopup, String[] colunas, Object[][] linhas) {
        JTable table = new JTable(new SimpleTableModel(colunas, linhas));
        //iPopup menuPopup = PopupMenu.getFullVersion(tela, table, column, emf);//new PopupMenu(tela, table,column, emf);
        menuPopup.setTabela(table);
        table.addMouseListener(menuPopup.getEventoMouse());
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setSize(panel.getSize());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }

}
