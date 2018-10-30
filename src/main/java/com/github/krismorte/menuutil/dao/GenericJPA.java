/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.krismorte.menuutil.dao;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author krisnamourtscf
 */
public class GenericJPA<T> {
     private EntityManagerFactory emf;
    private EntityManager em;

    private Class<T> entityClass;

    public GenericJPA(Class<T> entityClass, EntityManagerFactory emf) {
        this.entityClass = entityClass;
        this.emf = emf;
    }

    public void beginTransaction() {
        em = emf.createEntityManager();

        em.getTransaction().begin();
    }

    public void commit() {
        em.getTransaction().commit();
    }

    public void rollback() {
        em.getTransaction().rollback();
    }

    public void closeTransaction() {
        em.close();
    }

    public void commitAndCloseTransaction() {
        commit();
        closeTransaction();
    }

    public void flush() {
        em.flush();
    }

    public void joinTransaction() {
        em = emf.createEntityManager();
        em.joinTransaction();
    }

    public boolean update(T entity) {
        em.merge(entity);
        return true;
    }

    public boolean delete(T entity) {
        em.remove(entity);
        return true;
    }

    public void delete(Long id) {
        T entityToBeRemoved = em.getReference(entityClass, id);

        em.remove(entityToBeRemoved);
    }
    
    public boolean save(T entity) {
        em.persist(entity);
        return true;
    }

    // Using the unchecked because JPA does not have a
    // em.getCriteriaBuilder().createQuery()<T> method
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<T> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return em.createQuery(cq).getResultList();
    }

    @SuppressWarnings("unchecked")
    protected List<T> findFilterResult(String queryString, Map<String, Object> parameters) {
        List<T> result = null;

        try {
            Query query = em.createQuery(queryString);

            // Method that will populate parameters if they are passed not null and empty
            if (parameters != null && !parameters.isEmpty()) {
                populateQueryParameters(query, parameters);
            }

            result = query.getResultList();

        } catch (NoResultException e) {
            System.out.println("No result found for query: " + queryString);
        } catch (Exception e) {
            System.out.println("Error while running query: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    protected List<T> findFilterResult(String queryString) {
        List<T> result = null;

        try {
            Query query = em.createQuery(queryString);

            result = query.getResultList();

        } catch (NoResultException e) {
            System.out.println("No result found for query: " + queryString);
        } catch (Exception e) {
            System.out.println("Error while running query: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }


     // Using the unchecked because JPA does not have a
    // query.getSingleResult()<T> method
    @SuppressWarnings("unchecked")
    protected T findOneResult(String namedQuery) {
        T result = null;

        try {
            //Query query = em.createNamedQuery(namedQuery);
            Query query = em.createQuery(namedQuery);

            result = (T) query.getSingleResult();

        } catch (NoResultException e) {
            System.out.println("No result found for named query: " + namedQuery);
        } catch (Exception e) {
            System.out.println("Error while running query: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }
    
      // Using the unchecked because JPA does not have a
    // query.getSingleResult()<T> method
    @SuppressWarnings("unchecked")
    protected T findOneResult(String namedQuery, Map<String, Object> parameters) {
        T result = null;

        try {
            //Query query = em.createNamedQuery(namedQuery);
            Query query = em.createQuery(namedQuery);

            // Method that will populate parameters if they are passed not null and empty
            if (parameters != null && !parameters.isEmpty()) {
                populateQueryParameters(query, parameters);
            }

            result = (T) query.getSingleResult();

        } catch (NoResultException e) {
            System.out.println("No result found for named query: " + namedQuery);
        } catch (Exception e) {
            System.out.println("Error while running query: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }
    
    private void populateQueryParameters(Query query, Map<String, Object> parameters) {
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
    }
}
