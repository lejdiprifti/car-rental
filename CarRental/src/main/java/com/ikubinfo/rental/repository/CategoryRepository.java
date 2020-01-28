package com.ikubinfo.rental.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ikubinfo.rental.entity.CategoryEntity;

@Repository
public class CategoryRepository {

	@PersistenceContext
	private EntityManager em;

	public CategoryRepository() {

	}
	
	@Transactional
	public CategoryEntity getByName(String name) throws NoResultException {
		TypedQuery<CategoryEntity> query = em.createQuery("Select c from CategoryEntity c where c.name = ?1 and c.active = ?2", CategoryEntity.class);
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
	public List<CategoryEntity> getAll() {
		TypedQuery<CategoryEntity> query = em.createQuery("Select c from CategoryEntity c where c.active = ?1",
				CategoryEntity.class);
		query.setParameter(1, true);
		return query.getResultList();
	}

	@Transactional
	public void save(CategoryEntity entity) {
		em.persist(entity);
	}
	
	@Transactional
	public void edit(CategoryEntity entity) {
		em.merge(entity);
	}
}
