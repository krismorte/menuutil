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
import javax.swing.JMenuItem;

/**
 *
 * @author krisnamourtscf
 */
@Entity
@Table(name = "tbitemmenu")
public class Item extends JMenuItem implements Serializable,ComponenteMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer ordem;
    @Column(length = 70)
    private String nome;
    @Column(length = 150)
    private String caminhoImagem;
    @Convert(converter = BooleanToIntegerConverter.class)
    private Boolean separador;
    @Convert(converter = BooleanToIntegerConverter.class)
    private Boolean habilitado;
    @OneToOne
    private Menu menu;

    public Item() {
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
     * @return the caminhoImagem
     */
    @Override
    public String getCaminhoImagem() {
        return caminhoImagem;
    }

    /**
     * @param caminhoImagem the caminhoImagem to set
     */
    public void setCaminhoImagem(String caminhoImagem) {
        this.caminhoImagem = caminhoImagem;
    }

    /**
     * @return the separador
     */
    @Override
    public Boolean getSeparador() {
        return separador;
    }

    /**
     * @param separador the separador to set
     */
    public void setSeparador(Boolean separador) {
        this.separador = separador;
    }

    /**
     * @return the menu
     */
    @Override
    public Menu getMenu() {
        return menu;
    }

    /**
     * @param menu the menu to set
     */
    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public String getTipo() {
        return "ITEM";
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
    public String toString() {
        return getNome();
    }
            
}
