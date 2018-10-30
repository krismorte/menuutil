/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.krismorte.menuutil.interfaces;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

/**
 *
 * @author C007329
 */
public abstract class iPopup extends JPopupMenu implements ActionListener {

    private EventoMouse eventoMouse;

    public String recebeNovoValor(String pergunta) {
        String s = JOptionPane.showInputDialog(pergunta);
        return s;
    }

    public void iniciaJMenuItem(JMenuItem item) {
        item.addActionListener(this);
        add(item);
    }

    /**
     * @return the eventoMouse
     */
    public EventoMouse getEventoMouse() {
        if (eventoMouse == null) {
            eventoMouse = new EventoMouse();
        }
        return eventoMouse;
    }

    public class EventoMouse implements MouseListener {

        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                //selecionaLinhaTabela();
                show(e.getComponent(), e.getX(), e.getY());
            }
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }
    }
    
     public abstract void setTabela(JTable tabela);

    
}
