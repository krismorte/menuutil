
import com.github.krismorte.menuutil.annotation.MenuClass;
import com.github.krismorte.menuutil.annotation.ItemMethod;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author krisnamourtscf
 */
@MenuClass(name = "ARQUIVO:SYBMENU",text = "Arquivo")
public class MenuTest {
    
    @ItemMethod(name = "item",text = "item")
    public void teste1() {
        System.out.println("subitem");
    }
    
    @ItemMethod(name = "item02:subitem",text = "subitem")
    public void teste2() {
        System.out.println("subitem");
    }
    
}
