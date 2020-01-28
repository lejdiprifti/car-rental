package com.ikubinfo.rental.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ikubinfo.rental.entity.CarEntity;

@Repository
public class CarRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	public CarRepository() {
		
	}
	
	public CarEntity getById(Long id) throws NoResultException {
		TypedQuery<CarEntity> query =em.createQuery("Select c from CarEntity c where c.id = ?1 and c.active = ?2", CarEntity.class);
		query.setParameter(1, id);
		query.setParameter(2, true);
		return query.getSingleResult();
	}
	
	public List<CarEntity> getAll() {
		TypedQuery<CarEntity> query = em.createQuery("Select c from CarEntity c where c.active =?2", CarEntity.class);
		query.setParameter(1,true);
		return query.getResultList();
	}
	
	public List<CarEntity> getAllAvailable(){
		TypedQuery<CarEntity> query = em.createQuery("Select c from CarEntity c where c.availability = ?1 and c.active =?1",CarEntity.class);
		query.setParameter(1, true);
		return query.getResultList();
	}
	
	public List<CarEntity> getByCategory(Long categoryId) {
		TypedQuery<CarEntity> query = em.createQuery("Select c from CarEntity c where c.category = ?1 and c.active =?2",CarEntity.class);
		query.setParameter(1, categoryId);
		query.setParameter(2, true);
		return query.getResultList();
	}
	
	@Transactional
	public void save(CarEntity entity) {
		em.persist(entity);
	}
	
	@Transactional
	public void edit(CarEntity entity) {
		em.merge(entity);
	}
}
