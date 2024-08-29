package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class RoleRepositoryImpl implements RoleRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Role findById(Long id) {
        return entityManager.find(Role.class, id);
    }

    @Transactional
    public List<Role> findAll() {
        return entityManager.createQuery("SELECT r FROM Role r", Role.class).getResultList();
    }

    @Transactional
    public List<Role> findAllById(List<Long> roles) {
        return entityManager.createQuery("SELECT r FROM Role r WHERE r.id IN :ids", Role.class)
                .setParameter("ids", roles)
                .getResultList();
    }

    @Transactional
    public void save(Role role) {
        entityManager.persist(role);
    }

    @Transactional
    public void update(Role role) {
        entityManager.merge(role);
    }

    @Transactional
    public void deleteById(Long id) {
        Role role = findById(id);
        if (role != null) {
            entityManager.remove(role);
        }
    }

    @Transactional
    public long count() {
        return entityManager.createQuery("SELECT COUNT(r) FROM Role r", Long.class).getSingleResult();
    }
}
