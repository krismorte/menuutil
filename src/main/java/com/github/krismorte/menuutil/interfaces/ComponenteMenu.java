/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.krismorte.menuutil.interfaces;

import com.github.krismorte.menuutil.Menu;

/**
 *
 * @author c007329
 */
public interface ComponenteMenu {
    
    public Menu getMenu();
    
    public Long getId();
    
    public Integer getOrdem();
    
    public String getTipo();
    
    public String getNome();
    
    public String getCaminhoImagem();
    
    public Boolean getSeparador();
    
    public Boolean getHabilitado();
    
}
