package com.ikubinfo.rental.repository;

import com.ikubinfo.rental.entity.CategoryEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;

@Repository
public class CategoryRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public CategoryEntity getByName(String name) throws NoResultException {
        TypedQuery<CategoryEntity> query = em.createQuery(
                "Select c from CategoryEntity c where c.name = ?1 and c.active = ?2", CategoryEntity.class);
        query.setParameter(1, name);
        query.setParameter(2, true);
        return query.getSingleResult();
    }

    @Transactional
    public CategoryEntity getById(Long id) throws NoResultException, NonUniqueResultException {
        TypedQuery<CategoryEntity> query = em
                .createQuery("Select c from CategoryEntity c where c.id = ?1 and c.active =?2", CategoryEntity.class);
        query.setParameter(1, id);
        query.setParameter(2, true);
        return query.getSingleResult();
    }

    @Transactional
    public List<CategoryEntity> getAll(int startIndex, int pageSize) {
        TypedQuery<CategoryEntity> query = em.createQuery("Select c from CategoryEntity c where c.active = ?1",
                CategoryEntity.class);
        query.setParameter(1, true);
        query.setFirstResult(startIndex);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    @Transactional
    public List<CategoryEntity> getAll() {
        TypedQuery<CategoryEntity> query = em.createQuery("Select c from CategoryEntity c where c.active = ?1",
                CategoryEntity.class);
        query.setParameter(1, true);
        return query.getResultList();
    }

    public Long countCategories() {
        TypedQuery<Long> query = em.createQuery("Select Count(c.id) from CategoryEntity c where c.active = ?1",
                Long.class);
        query.setParameter(1, true);
        return query.getSingleResult();
    }

    @Transactional
    public CategoryEntity save(CategoryEntity entity) {
        em.persist(entity);
        return entity;
    }

    @Transactional
    public void edit(CategoryEntity entity) {
        em.merge(entity);
    }

    public void checkIfExistsAnother(String name, Long id) throws NoResultException {
        TypedQuery<String> query = em.createQuery(
                "Select c.name from CategoryEntity c where c.name = ?1 and c.id != ?2 and c.active = ?3", String.class);
        query.setParameter(1, name);
        query.setParameter(2, id);
        query.setParameter(3, true);
        query.getSingleResult();
    }
}
