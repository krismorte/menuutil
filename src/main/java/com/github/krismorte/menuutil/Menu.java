/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.krismorte.menuutil;

import com.github.krismorte.menuutil.interfaces.ComponenteMenu;
import com.github.krismorte.menuutil.util.BooleanToIntegerConverter;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.swing.JMenu;

/**
 *
 * @author krisnamourtscf
 */
@Entity
@Table(name = "tbmenu")
public class Menu extends JMenu implements Serializable, ComponenteMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer ordem;
    @Column(length = 70)
    private String nome;
    @Column(length = 150)
    private String caminhoImagem;
    @Convert(converter = BooleanToIntegerConverter.class)
    private Boolean habilitado;
    @OneToOne
    private Menu menuSuperior;
    @OneToOne
    private MenuBarra menuBarra;

    public Menu() {
        super();
    }

    /**
     * @return the id
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the ordem
     */
    @Override
    public Integer getOrdem() {
        return ordem;
    }

    /**
     * @param ordem the ordem to set
     */
    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    /**
     * @return the nome
     */
    @Override
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the menuSuperior
     */
    public Menu getMenuSuperior() {
        return menuSuperior;
    }

    /**
     * @param menuSuperior the menuSuperior to set
     */
    public void setMenuSuperior(Menu menuSuperior) {
        this.menuSuperior = menuSuperior;
    }

    /**
     * @return the menuBarra
     */
    public MenuBarra getMenuBarra() {
        return menuBarra;
    }

    /**
     * @param menuBarra the menuBarra to set
     */
    public void setMenuBarra(MenuBarra menuBarra) {
        this.menuBarra = menuBarra;
    }

    /**
     * @return the caminhoImagem
     */
    public String getCaminhoImagem() {
        return caminhoImagem;
    }

    /**
     * @param caminhoImagem the caminhoImagem to set
     */
    public void setCaminhoImagem(String caminhoImagem) {
        this.caminhoImagem = caminhoImagem;
    }

    @Override
    public String toString() {
        if (getMenuSuperior() == null) {
            return getNome().toUpperCase();
        } else {
            return getMenuSuperior().getNome().toUpperCase() + ":" + getNome().toUpperCase();
        }
    }

    /**
     * @return the habilitado
     */
    @Override
    public Boolean getHabilitado() {
        return habilitado;
    }

    /**
     * @param habilitado the habilitado to set
     */
    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    @Override
    public Menu getMenu() {
        return getMenuSuperior();
    }

    @Override
    public Boolean getSeparador() {
        return false;
    }

    @Override
    public String getTipo() {
        if (menuSuperior == null) {
            return "MENU";
        } else {
            return "SUBMENU";
        }

    }

}
