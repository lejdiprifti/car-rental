package com.ikubinfo.rental.database;

import com.ikubinfo.rental.service.user.dto.UserEntity;
import com.ikubinfo.rental.service.user.dto.UserFilter;
import com.ikubinfo.rental.service.user.repository.UserRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<UserEntity> getAll(UserFilter userFilter) {
        TypedQuery<UserEntity> query = em
                .createQuery("Select u from UserEntity u where u.role.id = ?1 and u.active =?2 and ((u.firstName Like ?3) or (u.lastName Like ?3))", UserEntity.class);
        query.setParameter(1, 2);
        query.setParameter(2, true);
        query.setParameter(3, '%' + userFilter.getName() + '%');
        query.setFirstResult(userFilter.getStartIndex());
        query.setMaxResults(userFilter.getPageSize());
        return query.getResultList();
    }

    @Override
    public UserEntity getById(Long id) throws NoResultException {
        TypedQuery<UserEntity> query = em.createQuery("Select u from UserEntity u where u.id = ?1 and u.active =?2",
                UserEntity.class);
        query.setParameter(1, id);
        query.setParameter(2, true);
        return query.getSingleResult();
    }

    @Override
    public UserEntity getByUsername(String username) throws NoResultException {
        TypedQuery<UserEntity> query = em.createQuery("Select u from UserEntity u where u.username=?1 and u.active=?2",
                UserEntity.class);
        query.setParameter(1, username);
        query.setParameter(2, true);
        return query.getSingleResult();
    }

    @Override
    public UserEntity getByUsernameAndPassword(String username, String password) {
        TypedQuery<UserEntity> query = em.createQuery(
                "Select u from UserEntity u where u.username=?1 and u.password=?2 and u.active=?3", UserEntity.class);
        query.setParameter(1, username);
        query.setParameter(2, password);
        query.setParameter(3, true);
        return query.getSingleResult();
    }

    @Override
    public Long countActiveUsers(String name) {
        TypedQuery<Long> query = em
                .createQuery("Select Count(u.id) from UserEntity u where u.role.id = ?1 and u.active = ?2 and ((u.firstName LIKE ?3) or "
                        + "(u.lastName LIKE ?3))", Long.class);
        query.setParameter(1, 2);
        query.setParameter(2, true);
        query.setParameter(3, name);
        return query.getSingleResult();
    }

    @Override
    public Long countActiveUsers() {
        TypedQuery<Long> query = em
                .createQuery("Select Count(u.id) from UserEntity u where u.role.id = ?1 and u.active = ?2", Long.class);
        query.setParameter(1, 2);
        query.setParameter(2, true);
        return query.getSingleResult();
    }


    @Transactional
    @Override
    public void save(UserEntity user) {
        em.persist(user);
    }

    @Transactional
    @Override
    public void edit(UserEntity user) {
        em.merge(user);
    }
}
