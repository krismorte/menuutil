
import com.github.krismorte.menuutil.annotation.ItemMetodo;
import com.github.krismorte.menuutil.annotation.MenuClasse;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author krisnamourtscf
 */
@MenuClasse(name = "ARQUIVO:SYBMENU")
public class MenuTest {
    
    @ItemMetodo(name = "subitem")
    public void teste1() {
        System.out.println("subitem");
    }
    
}
