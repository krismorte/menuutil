/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.krismorte.menuutil.view;

import com.github.krismorte.menuutil.Item;
import com.github.krismorte.menuutil.ItemAcesso;
import com.github.krismorte.menuutil.Menu;
import com.github.krismorte.menuutil.MenuBarra;
import com.github.krismorte.menuutil.dao.ComponenteMenuDao;
import com.github.krismorte.menuutil.dao.ItemAcessoDao;
import com.github.krismorte.menuutil.dao.ItemDao;
import com.github.krismorte.menuutil.dao.MenuBarraDao;
import com.github.krismorte.menuutil.dao.MenuDao;
import com.github.krismorte.menuutil.interfaces.ComponenteMenu;
import com.github.krismorte.menuutil.interfaces.iAtualizaGUI;
import com.github.krismorte.menuutil.util.InspectorUtil;
import com.github.krismorte.menuutil.util.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author krisnamourtscf
 */
public class ControleMenu extends javax.swing.JFrame implements iAtualizaGUI {

    private String[] colunasMenu = {"ID", "Ordem", "Nome", "Imagem", "habilitado"};
    private Object[][] linhasMenu;
    private String[] colunasItem = {"Menu", "ID", "Tipo", "Ordem", "Nome", "Imagem", "Separador", "habilitado"};
    private Object[][] linhasItem;
    private String[] colunasItemAcesso = {"ID", "Perfil", "Menu", "Item"};
    private Object[][] linhasItemAcesso;
    private MenuBarraDao menuBarraDao;
    private MenuDao menuDao;
    private ComponenteMenuDao componenteMenuDao;
    private ItemDao itemDao;
    private ItemAcessoDao itemAcessoDao;
    private EntityManagerFactory entityManagerFactory;
    private List<MenuBarra> listaBarras;
    private List<Menu> listaMenus;
    private List<ComponenteMenu> listaComponenteMenus;
    private List<Item> listaItens;
    private List<ItemAcesso> listaItensAcesso;
    private MenuBarra menuBarraAtual;
    private Menu menuAtual;
    private Item itemAtual;
    private String[] perfis;

    /**
     * Creates new form ControleMenu
     */
    public ControleMenu(EntityManagerFactory emf, String[] perfis) {
        initComponents();
        menuBarraDao = new MenuBarraDao(MenuBarra.class, emf);
        menuDao = new MenuDao(Menu.class, emf);
        itemDao = new ItemDao(Item.class, emf);
        itemAcessoDao = new ItemAcessoDao(ItemAcesso.class, emf);
        componenteMenuDao = new ComponenteMenuDao();
        entityManagerFactory = emf;
        this.perfis = perfis;
        preecheBox();
        adicionaEventos();
    }

    private void preecheBox() {
        listaBarras = menuBarraDao.listAll();

        InspectorUtil.fulfillJComboBox(boxBarra, listaBarras);

        selecionaMenuBarra();

        if (listaMenus != null) {
            InspectorUtil.fulfillJComboBox(boxMenu, listaMenus);

            selecionaMenu();

            InspectorUtil.fulfillJComboBox(boxItem, listaItens);

            //selecionaItem();
            todosOsAcessos();
        }

        boxPerfil.removeAllItems();
        boxPerfil.addItem("");
        for (String perfil : perfis) {
            boxPerfil.addItem(perfil);
        }
    }

    private void adicionaEventos() {
        boxBarra.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (boxBarra.getSelectedItem() != null) {
                        selecionaMenuBarra();
                        InspectorUtil.fulfillJComboBox(boxMenu, listaMenus);
                        selecionaMenu();
                    }
                }
            }
        });

        boxMenu.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (boxMenu.getSelectedItem() != null) {
                        selecionaMenu();
                        InspectorUtil.fulfillJComboBox(boxItem, listaItens);
                        //selecionaItem();
                        todosOsAcessos();
                    }
                }
            }
        });

        boxItem.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (boxItem.getSelectedItem() != null) {
                        //selecionaItem();
                        todosOsAcessos();
                    }
                }
            }
        });
        chkAllItems.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (chkAllItems.isSelected()) {
                    boxItem.setEnabled(false);
                } else {
                    boxItem.setEnabled(true);
                }
            }
        });
    }

    private void selecionaMenuBarra() {
        if (boxBarra.getSelectedItem() != null) {
            String nomeBarra = boxBarra.getSelectedItem().toString();
            menuBarraAtual = (MenuBarra) InspectorUtil.getObjectFromList(listaBarras, nomeBarra);
            listaMenus = menuDao.listByMenuBar(menuBarraAtual);
            if (menuBarraAtual.getPrincipal()) {
                chkPrincipal.setSelected(true);
            } else {
                chkPrincipal.setSelected(false);
            }
            preencheTabelaMenu();
        }
    }

    private void selecionaMenu() {
        if (boxMenu.getSelectedItem() != null) {
            String nomeMenu = boxMenu.getSelectedItem().toString();
            menuAtual = (Menu) InspectorUtil.getObjectFromList(listaMenus, nomeMenu);
            listaItens = itemDao.listByMenu(menuAtual);
            listaComponenteMenus = componenteMenuDao.buscaComponentes(entityManagerFactory, menuAtual);
            preencheTabelaItem();
        } else {
            listaItens = new ArrayList<>();
            listaComponenteMenus = new ArrayList<>();
            preencheTabelaItem();
        }
    }

    private void todosOsAcessos() {
        if (boxItem.getSelectedItem() != null) {
            String nomeItem = boxItem.getSelectedItem().toString();
            itemAtual = (Item) InspectorUtil.getObjectFromList(listaItens, nomeItem);
            listaItensAcesso = itemAcessoDao.listAllByMenuBar(menuBarraAtual);
            preencheTabelaItemAcesso();
        } else {
            listaItensAcesso = new ArrayList<>();
            preencheTabelaItemAcesso();
        }
    }

    public void preencheLinhasMenu(List<Menu> list) {
        linhasMenu = new Object[list.size()][colunasMenu.length];
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                for (int z = 0; z < colunasMenu.length; z++) {
                    Object[][] obj = {{list.get(i).getId(), list.get(i).getOrdem(), list.get(i), list.get(i).getCaminhoImagem(), list.get(i).getHabilitado()}};
                    linhasMenu[i][z] = obj[0][z];//Rows are dynamics
                }
            }
        }
    }

    public void preencheLinhasItem(List<ComponenteMenu> list) {
        linhasItem = new Object[list.size()][colunasItem.length];
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                for (int z = 0; z < colunasItem.length; z++) {
                    Object[][] obj = {{list.get(i).getMenu().getNome(), list.get(i).getId(), list.get(i).getTipo(), list.get(i).getOrdem(), list.get(i), list.get(i).getCaminhoImagem(), list.get(i).getSeparador(), list.get(i).getHabilitado()}};
                    linhasItem[i][z] = obj[0][z];//Rows are dynamics
                }
            }
        }
    }

    public void preencheLinhasItemAcesso(List<ItemAcesso> list) {
        linhasItemAcesso = new Object[list.size()][colunasItemAcesso.length];
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                for (int z = 0; z < colunasItemAcesso.length; z++) {
                    Object[][] obj = {{list.get(i).getId(), list.get(i), list.get(i).getItem().getMenu().toString(), list.get(i).getItem().getNome()}};
                    linhasItemAcesso[i][z] = obj[0][z];//Rows are dynamics
                }
            }
        }
    }

    private void preencheTabelaMenu() {
        preencheLinhasMenu(listaMenus);

        panelMenu.removeAll();
        //panelMenu.add(InspectorUtil.jTableDecorator(panelMenu, colunasMenu, linhasMenu));
        panelMenu.add(InspectorUtil.jTableDecoratorWithMenuPopup(panelMenu, PopupMenu.getFullVersion(this, null, 2, entityManagerFactory), colunasMenu, linhasMenu));

        panelMenu.validate();
    }

    private void preencheTabelaItem() {
        preencheLinhasItem(listaComponenteMenus);

        panelItem.removeAll();
        //panelItem.add(InspectorUtil.jTableDecorator(panelItem, colunasItem, linhasItem));
        panelItem.add(InspectorUtil.jTableDecoratorWithMenuPopup(panelItem, PopupMenu.getFullVersion(this, null, 4, entityManagerFactory), colunasItem, linhasItem));
        panelItem.validate();
    }

    private void preencheTabelaItemAcesso() {
        preencheLinhasItemAcesso(listaItensAcesso);
        panelItemAcesso.removeAll();
        //panelItemAcesso.add(InspectorUtil.jTableDecorator(panelItemAcesso, colunasItemAcesso, linhasItemAcesso));
        panelItemAcesso.add(InspectorUtil.jTableDecoratorWithMenuPopup(panelItemAcesso, PopupMenu.getJustRemove(this, null, 1, entityManagerFactory), colunasItemAcesso, linhasItemAcesso));
        panelItemAcesso.validate();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        boxBarra = new javax.swing.JComboBox<>();
        btnAdicionarBarra = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        panelMenu = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtNomeMenu = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtOrdemMenu = new javax.swing.JTextField();
        btnAddMenu = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        txtImagemMenu = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        panelItem = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtNomeItem = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtOrdemItem = new javax.swing.JTextField();
        btnAddItem = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        boxMenu = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        txtImagemItem = new javax.swing.JTextField();
        chkSeparador = new javax.swing.JCheckBox();
        btnSubMenu = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        panelItemAcesso = new javax.swing.JPanel();
        btnAddAcesso = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        boxItem = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        boxPerfil = new javax.swing.JComboBox<>();
        chkAllItems = new javax.swing.JCheckBox();
        chkPrincipal = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Menu Barra:");

        btnAdicionarBarra.setText("+");
        btnAdicionarBarra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarBarraActionPerformed(evt);
            }
        });

        panelMenu.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout panelMenuLayout = new javax.swing.GroupLayout(panelMenu);
        panelMenu.setLayout(panelMenuLayout);
        panelMenuLayout.setHorizontalGroup(
            panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 658, Short.MAX_VALUE)
        );
        panelMenuLayout.setVerticalGroup(
            panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 342, Short.MAX_VALUE)
        );

        jLabel2.setText("Nome:");

        jLabel3.setText("Ordem:");

        txtOrdemMenu.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        btnAddMenu.setText("+");
        btnAddMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddMenuActionPerformed(evt);
            }
        });

        jLabel8.setText("Caminho Imagem:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNomeMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtOrdemMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAddMenu)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtImagemMenu)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNomeMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtOrdemMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddMenu))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtImagemMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Menu", jPanel1);

        panelItem.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout panelItemLayout = new javax.swing.GroupLayout(panelItem);
        panelItem.setLayout(panelItemLayout);
        panelItemLayout.setHorizontalGroup(
            panelItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 658, Short.MAX_VALUE)
        );
        panelItemLayout.setVerticalGroup(
            panelItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 335, Short.MAX_VALUE)
        );

        jLabel4.setText("Nome:");

        jLabel5.setText("Ordem:");

        txtOrdemItem.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        btnAddItem.setText("+");
        btnAddItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddItemActionPerformed(evt);
            }
        });

        jLabel6.setText("Menu:");

        jLabel7.setText("Caminho Imagem:");

        chkSeparador.setText("adicionar separador");

        btnSubMenu.setText("add sub-menu");
        btnSubMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubMenuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(boxMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNomeItem, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtImagemItem)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtOrdemItem, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAddItem)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(chkSeparador)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnSubMenu)))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtNomeItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(txtOrdemItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddItem)
                    .addComponent(jLabel6)
                    .addComponent(boxMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtImagemItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkSeparador)
                    .addComponent(btnSubMenu))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Item", jPanel3);

        panelItemAcesso.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout panelItemAcessoLayout = new javax.swing.GroupLayout(panelItemAcesso);
        panelItemAcesso.setLayout(panelItemAcessoLayout);
        panelItemAcessoLayout.setHorizontalGroup(
            panelItemAcessoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 658, Short.MAX_VALUE)
        );
        panelItemAcessoLayout.setVerticalGroup(
            panelItemAcessoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 335, Short.MAX_VALUE)
        );

        btnAddAcesso.setText("+");
        btnAddAcesso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddAcessoActionPerformed(evt);
            }
        });

        jLabel11.setText("Item:");

        jLabel12.setText("Perfil:");

        boxPerfil.setEditable(true);

        chkAllItems.setText("all items");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelItemAcesso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(boxPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(boxItem, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAddAcesso)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(chkAllItems)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddAcesso)
                    .addComponent(jLabel11)
                    .addComponent(boxItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(boxPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkAllItems))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(panelItemAcesso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Acessos", jPanel4);

        chkPrincipal.setText("principal");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(boxBarra, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkPrincipal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAdicionarBarra))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTabbedPane1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(boxBarra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdicionarBarra)
                    .addComponent(chkPrincipal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAdicionarBarraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarBarraActionPerformed
        MenuBarraCRUD crud = new MenuBarraCRUD(this, entityManagerFactory);
        crud.setVisible(true);
        preecheBox();
    }//GEN-LAST:event_btnAdicionarBarraActionPerformed

    private void btnAddMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddMenuActionPerformed
        if (!txtNomeMenu.getText().equals("")) {
            if (!InspectorUtil.existingName(listaMenus, txtNomeMenu.getText())) {
                Menu menu = new Menu();
                menu.setMenuBarra(menuBarraAtual);
                menu.setCaminhoImagem(txtImagemMenu.getText());
                menu.setNome(txtNomeMenu.getText());
                menu.setOrdem(getOrdem(txtOrdemMenu, listaMenus));
                menu.setHabilitado(true);
                menuDao.save(menu);
                reordenaMenu(menu);
                txtNomeMenu.setText("");
                txtOrdemMenu.setText("");
                selecionaMenuBarra();
                InspectorUtil.fulfillJComboBox(boxMenu, listaMenus);
            } else {
                JOptionPane.showMessageDialog(null, "Nome já existe!");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Digite um nome!");
        }
    }//GEN-LAST:event_btnAddMenuActionPerformed

    private void btnAddItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddItemActionPerformed
        if (!txtNomeItem.getText().equals("")) {
            if (!InspectorUtil.existingName(listaComponenteMenus, txtNomeItem.getText())) {
                Item item = new Item();
                item.setMenu(menuAtual);
                item.setCaminhoImagem(txtImagemItem.getText());
                item.setNome(txtNomeItem.getText());
                item.setOrdem(getOrdem(txtOrdemItem, listaComponenteMenus));
                item.setSeparador(chkSeparador.isSelected());
                item.setHabilitado(true);
                itemDao.save(item);
                txtNomeItem.setText("");
                txtOrdemItem.setText("");
                chkSeparador.setSelected(false);
                selecionaMenu();
            } else {
                JOptionPane.showMessageDialog(null, "Nome já existe!");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Digite um nome!");
        }
    }//GEN-LAST:event_btnAddItemActionPerformed

    private void btnSubMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubMenuActionPerformed
        Menu subMenu = AddSubMenu.create(this);
        if (subMenu != null) {
            if (!InspectorUtil.existingName(listaComponenteMenus, subMenu.getNome())) {
                subMenu.setMenuBarra(menuBarraAtual);
                subMenu.setMenuSuperior(menuAtual);
                subMenu.setOrdem(getOrdem(txtNomeItem, listaItens));
                subMenu.setHabilitado(true);
                menuDao.save(subMenu);
                InspectorUtil.fulfillJComboBox(boxItem, listaItens);
                preencheTabelaMenu();
                selecionaMenu();
            } else {

            }
        }
    }//GEN-LAST:event_btnSubMenuActionPerformed

    private void btnAddAcessoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddAcessoActionPerformed
        if (!boxPerfil.getSelectedItem().toString().equals("")) {
            if (chkAllItems.isSelected()) {
                for (Item item : listaItens) {
                    ItemAcesso itemAcesso = new ItemAcesso();
                    itemAcesso.setChave(boxPerfil.getSelectedItem().toString());
                    itemAcesso.setItem(item);
                    if (verificaItemAcessoExiste(boxPerfil.getSelectedItem().toString(), item.getNome()) == false) {
                        itemAcessoDao.save(itemAcesso);
                        listaItensAcesso.add(itemAcesso);
                    }
                }
            } else {
                if (itemAtual != null) {
                    ItemAcesso itemAcesso = new ItemAcesso();
                    itemAcesso.setChave(boxPerfil.getSelectedItem().toString());
                    itemAcesso.setItem(itemAtual);
                    if (verificaItemAcessoExiste(boxPerfil.getSelectedItem().toString(), itemAtual.getNome()) == false) {
                        itemAcessoDao.save(itemAcesso);
                        listaItensAcesso.add(itemAcesso);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Sem Item!");
                }
            }
            todosOsAcessos();
            preencheTabelaItemAcesso();
        } else {
            JOptionPane.showMessageDialog(null, "Digite um nome!");
        }
    }//GEN-LAST:event_btnAddAcessoActionPerformed

    private int getOrdem(JTextField txt, List lista) {
        if (txt.getText().equals("")) {
            if (lista.isEmpty()) {
                return 0;
            } else {
                return lista.size();
            }
        } else {
            int ordem = Integer.parseInt(txt.getText());
            if (!lista.isEmpty()) {
                if (ordem > lista.size()) {
                    ordem = lista.size();
                }
            }
            return ordem;
        }
    }

    private void reordenaMenu(Menu menu) {
        if (menu.getOrdem() <= listaMenus.size() - 1) {
            for (Menu menuReordenado : listaMenus) {
                if (menuReordenado.getOrdem() >= menu.getOrdem()) {
                    menuReordenado.setOrdem(menuReordenado.getOrdem() + 1);
                    menuDao.update(menuReordenado);
                }
            }
        }
    }

    private boolean verificaItemAcessoExiste(String perfil, String item) {
        boolean achou = false;
        for (ItemAcesso itemAcesso : listaItensAcesso) {
            if (itemAcesso.getChave().toUpperCase().equals(perfil.toUpperCase()) & itemAcesso.getItem().getNome().toUpperCase().equals(item.toUpperCase())) {
                achou = true;
                break;
            }
        }
        return achou;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ControleMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ControleMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ControleMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ControleMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ControleMenu(null, null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> boxBarra;
    private javax.swing.JComboBox<String> boxItem;
    private javax.swing.JComboBox<String> boxMenu;
    private javax.swing.JComboBox<String> boxPerfil;
    private javax.swing.JButton btnAddAcesso;
    private javax.swing.JButton btnAddItem;
    private javax.swing.JButton btnAddMenu;
    private javax.swing.JButton btnAdicionarBarra;
    private javax.swing.JButton btnSubMenu;
    private javax.swing.JCheckBox chkAllItems;
    private javax.swing.JCheckBox chkPrincipal;
    private javax.swing.JCheckBox chkSeparador;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel panelItem;
    private javax.swing.JPanel panelItemAcesso;
    private javax.swing.JPanel panelMenu;
    private javax.swing.JTextField txtImagemItem;
    private javax.swing.JTextField txtImagemMenu;
    private javax.swing.JTextField txtNomeItem;
    private javax.swing.JTextField txtNomeMenu;
    private javax.swing.JTextField txtOrdemItem;
    private javax.swing.JTextField txtOrdemMenu;
    // End of variables declaration//GEN-END:variables

    @Override
    public void atualizarTela() {
        listaBarras = menuBarraDao.listAll();

        InspectorUtil.fulfillJComboBox(boxBarra, listaBarras);

        selecionaMenuBarra();

        InspectorUtil.fulfillJComboBox(boxMenu, listaMenus);

        selecionaMenu();

        InspectorUtil.fulfillJComboBox(boxItem, listaItens);

        //selecionaItem();
        todosOsAcessos();

        boxPerfil.removeAllItems();
        boxPerfil.addItem("");
        for (String perfil : perfis) {
            boxPerfil.addItem(perfil);
        }
    }
}
