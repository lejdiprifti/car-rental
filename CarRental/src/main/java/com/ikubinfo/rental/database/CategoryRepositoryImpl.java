package com.ikubinfo.rental.database;

import com.ikubinfo.rental.service.category.dto.CategoryEntity;
import com.ikubinfo.rental.service.category.repository.CategoryRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public CategoryEntity getByName(String name) throws NoResultException {
        TypedQuery<CategoryEntity> query = em.createQuery(
                "Select c from CategoryEntity c where c.name = ?1 and c.active = ?2", CategoryEntity.class);
        query.setParameter(1, name);
        query.setParameter(2, true);
        return query.getSingleResult();
    }

    @Override
    @Transactional
    public CategoryEntity getById(Long id) throws NoResultException {
        TypedQuery<CategoryEntity> query = em
                .createQuery("Select c from CategoryEntity c where c.id = ?1 and c.active =?2", CategoryEntity.class);
        query.setParameter(1, id);
        query.setParameter(2, true);
        return query.getSingleResult();
    }

    @Override
    @Transactional
    public List<CategoryEntity> getAll(int startIndex, int pageSize) {
        TypedQuery<CategoryEntity> query = em.createQuery("Select c from CategoryEntity c where c.active = ?1",
                CategoryEntity.class);
        query.setParameter(1, true);
        query.setFirstResult(startIndex);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    @Transactional
    public List<CategoryEntity> getAll() {
        TypedQuery<CategoryEntity> query = em.createQuery("Select c from CategoryEntity c where c.active = ?1",
                CategoryEntity.class);
        query.setParameter(1, true);
        return query.getResultList();
    }

    @Override
    public Long countCategories() {
        TypedQuery<Long> query = em.createQuery("Select Count(c.id) from CategoryEntity c where c.active = ?1",
                Long.class);
        query.setParameter(1, true);
        return query.getSingleResult();
    }

    @Override
    @Transactional
    public CategoryEntity save(CategoryEntity entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    @Transactional
    public void edit(CategoryEntity entity) {
        em.merge(entity);
    }

    @Override
    public void checkIfAnotherCategoryWithSameNameExists(String name, Long id) throws NoResultException {
        TypedQuery<String> query = em.createQuery(
                "Select c.name from CategoryEntity c where c.name = ?1 and c.id <> ?2 and c.active = ?3", String.class);
        query.setParameter(1, name);
        query.setParameter(2, id);
        query.setParameter(3, true);
        query.getSingleResult();
    }
}
