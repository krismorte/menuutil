/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.krismorte.menuutil.dao;

import com.github.krismorte.menuutil.Item;
import com.github.krismorte.menuutil.Menu;
import com.github.krismorte.menuutil.interfaces.ComponenteMenu;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author c007329
 */
public class ComponenteMenuDao {

    public List<ComponenteMenu> buscaComponentes(EntityManagerFactory emf, Menu menu) {
        MenuDao menuDao = new MenuDao(Menu.class, emf);
        ItemDao itemDao = new ItemDao(Item.class, emf);;
        List<ComponenteMenu> componentes = new ArrayList<>();

        for (Item i : itemDao.listByMenu(menu)) {
            componentes.add(i);
        }

        for (Menu m : menuDao.listEnableByMenu(menu)) {
            componentes.add(m);
        }

        bubbleSort(componentes);

        return componentes;
    }

    public List<ComponenteMenu> buscaComponentesHabilitados(EntityManagerFactory emf, Menu menu) {
        System.out.println("busca componentese");
        MenuDao menuDao = new MenuDao(Menu.class, emf);
        ItemDao itemDao = new ItemDao(Item.class, emf);
        List<ComponenteMenu> componentes = new ArrayList<>();

        for (Item i : itemDao.listEnableByMenu(menu)) {
            componentes.add(i);
        }

        for (Menu m : menuDao.listEnableByMenu(menu)) {
            componentes.add(m);
        }

        bubbleSort(componentes);

        return componentes;
    }

   /* public List<ComponenteMenu> listaComponentes(EntityManagerFactory emf, Menu menu) {
        EntityManager em = emf.createEntityManager();

        List<ComponenteMenu> lista = new ArrayList<>();

        //("select l from Linha l LEFT JOIN FETCH l.responsavel where l.contrato.id=:contratoid and l.numero=:numero", parameters);
        em.getTransaction().begin();

        List<Object[]> lst = em.createNativeQuery("select menu.*,item.* from tbmenubarra bar "
                + " left join tbmenu menu on bar.id=menu.menuBarra_id "
                + " left join tbitemmenu item on menu.id=item.menu_id "
                + " where bar.id=:id ",Object[].class)
                .setParameter("id", new Long(1))
                .getResultList();
                
       /* List<Object[]> lst = em.createQuery("select i,m from Item i "
                + " JOIN FETCH i.menu m"
                + " JOIN FETCH m.menuBarra b"
                + " where b.id=:id",
                Object[].class)
                .setParameter("id", new Long(1))
                .getResultList();*/

        /*List<Object[]> lst = em.createQuery("select i,m from Item i,Menu m  "
                + "where  i.menu=m.menuSuperior and i.menu=:menu and i.habilitado=true or m.menuSuperior=:menu and m.habilitado=true ",
                Object[].class)
                .setParameter("menu", menu)
                .getResultList();*
        em.getTransaction().commit();

        /*List<ComponenteMenu> listaComponentes = new ArrayList<>();
        for(Item i:)*
        System.out.println("Lista: " + lst.size());
        for (Object[] obj : lst) {
            //Itens
            if (lista.isEmpty()) {
                lista.add((Item) obj[0]);
            } else {
                boolean naoAchou = true;
                for (ComponenteMenu c : lista) {
                    if (c.getId() == ((Item) obj[0]).getId() & c.getNome().equals(((Item) obj[0]).getNome())) {
                        naoAchou = false;
                        break;
                    }
                }
                if (naoAchou) {
                    lista.add((Item) obj[0]);
                }
            }
            //Menus
            if (lista.isEmpty()) {
                lista.add((Menu) obj[1]);
            } else {
                boolean naoAchou = true;
                for (ComponenteMenu c : lista) {
                    if (c.getId() == ((Menu) obj[1]).getId() & c.getNome().equals(((Menu) obj[1]).getNome())) {
                        naoAchou = false;
                        break;
                    }
                }
                if (naoAchou) {
                    lista.add((Menu) obj[1]);
                }
            }

            System.out.println("I: " + ((Item) obj[0]).getNome());
        }

        return lista;
    }*/

    private void bubbleSort(List<ComponenteMenu> componentes) {
        for (int i = componentes.size(); i >= 1; i--) {
            for (int j = 1; j < i; j++) {
                if (componentes.get(j - 1).getOrdem() > componentes.get(j).getOrdem()) {
                    ComponenteMenu aux = componentes.get(j);
                    componentes.set(j, componentes.get(j - 1));
                    componentes.set(j - 1, aux);
                }
            }
        }
    }

}
