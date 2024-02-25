package com.mziuri;

import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transaction;

import java.io.IOException;
import java.util.List;

public class DatabaseManager {
    private static DatabaseManager instance;
    private final EntityManagerFactory entityManagerFactory;

    private DatabaseManager() {
        // Create EntityManagerFactory during initialization
        entityManagerFactory = Persistence.createEntityManagerFactory("chemi-unit");
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            synchronized (StorageReader.class) {
                if (instance == null) {
                    instance = new DatabaseManager();
                }
            }
        }
        return instance;
    }

    public void save(Product product) throws IOException {
        EntityManager manager = entityManagerFactory.createEntityManager();
        try { // try and finally to ensure that we close the entity manager
            manager.getTransaction().begin();

            CriteriaBuilder cb = manager.getCriteriaBuilder();
            CriteriaQuery<Product> cq = cb.createQuery(Product.class);
            Root<Product> root = cq.from(Product.class);
            cq.select(root);

            // checks if this product exists or not
            Predicate condition = cb.equal(root.get("id"), String.valueOf(product.getProd_id()));
            cq.where(condition);

            TypedQuery<Product> query = manager.createQuery(cq);
            List<Product> result = query.getResultList();

            if (result.isEmpty()) {
                manager.persist(product); // add the product
            } else {
                request(product.getProd_name(), product.getProd_amount()); // replacing the amount with the ones inside the json file (resetting the values)
            }
            manager.getTransaction().commit();
        } finally {
            if (manager.isOpen()) {
                manager.close();
            }
        }
    }

    public List<Product> select() {
        EntityManager manager = entityManagerFactory.createEntityManager();
        try {
            CriteriaBuilder cb = manager.getCriteriaBuilder();
            CriteriaQuery<Product> cq = cb.createQuery(Product.class);
            Root<Product> root = cq.from(Product.class);
            cq.select(root);

            TypedQuery<Product> query = manager.createQuery(cq);
            return query.getResultList();
        } finally {
            if (manager.isOpen()) {
                manager.close();
            }
        }
    }

    public Product find(String name) {
        EntityManager manager = entityManagerFactory.createEntityManager();
        try {
            CriteriaBuilder cb = manager.getCriteriaBuilder();
            CriteriaQuery<Product> cq = cb.createQuery(Product.class);
            Root<Product> root = cq.from(Product.class);
            cq.select(root);

            Predicate condition = cb.equal(root.get("prod_name"), name);
            cq.where(condition);
            TypedQuery<Product> query = manager.createQuery(cq);
            try {
                return query.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        } finally {
            if (manager.isOpen()) {
                manager.close();
            }
        }
    }

    public void request(String name, Integer amount) {
        EntityManager manager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();

        try {
            CriteriaBuilder cb = manager.getCriteriaBuilder();
            CriteriaQuery<Product> cq = cb.createQuery(Product.class);
            CriteriaUpdate<Product> criteriaUpdate = cb.createCriteriaUpdate(Product.class);
            Root<Product> root = criteriaUpdate.from(Product.class);
            criteriaUpdate.set("prod_amount", amount);
            System.out.println(name);
            criteriaUpdate.where(cb.equal(root.get("id"), find(name).getProd_id()));

            transaction.begin();
            manager.createQuery(criteriaUpdate).executeUpdate();
            transaction.commit();
        } finally {
            if (manager.isOpen()) {
                manager.close();
            }
        }
    }
}
