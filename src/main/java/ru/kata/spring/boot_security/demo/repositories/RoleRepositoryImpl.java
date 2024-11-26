package ru.kata.spring.boot_security.demo.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(RoleRepositoryImpl.class);

    @Override
    public Role findById(Long id) {
        return entityManager.find(Role.class, id);
    }

    @Override
    public List<Role> findAll() {
        return entityManager.createQuery("SELECT r FROM Role r", Role.class).getResultList();
    }

    @Override
    public List<Role> findAllById(List<Long> roles) {
        return entityManager.createQuery("SELECT r FROM Role r WHERE r.id IN :ids", Role.class)
                .setParameter("ids", roles)
                .getResultList();
    }

    @Override
    public void save(Role role) {
        entityManager.persist(role);
    }

    @Override
    public void update(Role role) {
        entityManager.merge(role);
    }

    @Override
    public void deleteById(Long id) {
        Role role = findById(id);
        if (role != null) {
            entityManager.remove(role);
        }
    }

    @Override
    public Role findByRoleName(String roleName) {
        logger.debug("Searching for role with name: {}", roleName);
        Role role = entityManager.createQuery("SELECT r from Role r WHERE r.name IN :roleName", Role.class)
                .setParameter("roleName", roleName)
                .getSingleResult();
        logger.debug("Found role: {}", role);
        return role;
    }

    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(r) FROM Role r", Long.class).getSingleResult();
    }
}
