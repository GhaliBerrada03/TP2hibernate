package com.example.service;

import com.example.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class UserService extends AbstractCrudService<User, Long> {

    public UserService(EntityManagerFactory emf) {
        super(emf);
    }

    public Optional<User> findByEmail(String email) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.emailAddress = :email", User.class);
            query.setParameter("email", email);

            List<User> results = query.getResultList();
            return results.isEmpty()
                    ? Optional.empty()
                    : Optional.of(results.get(0));

        } finally {
            em.close();
        }
    }
}
