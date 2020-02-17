package com.ikubinfo.rental.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ikubinfo.rental.entity.CarEntity;
import com.ikubinfo.rental.entity.StatusEnum;

@Repository
public class CarRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	public CarRepository() {
		
	}
	
	@Transactional
	public CarEntity getById(Long id) throws NoResultException {
		TypedQuery<CarEntity> query =em.createQuery("Select c from CarEntity c where c.id = ?1 and c.active = ?2", CarEntity.class);
		query.setParameter(1, id);
		query.setParameter(2, true);
		return query.getSingleResult();
	}
	
	@Transactional
	public List<CarEntity> getAll() {
		TypedQuery<CarEntity> query = em.createQuery("Select c from CarEntity c where c.active =?1 ORDER BY c.id DESC", CarEntity.class);
		query.setParameter(1,true);
		return query.getResultList();
	}
	
	@Transactional
	public List<CarEntity> getAllAvailable(){
		TypedQuery<CarEntity> query = em.createQuery("Select c from CarEntity c where c.availability<>?1 and c.active = ?2 ORDER BY c.id DESC", CarEntity.class);
		query.setParameter(1, StatusEnum.SERVIS);
		query.setParameter(2, true);
		return query.getResultList();
	}
	
	@Transactional
	public CarEntity getByPlate(String plate) throws NoResultException {
		TypedQuery<CarEntity> query = em.createQuery("Select c from CarEntity c where c.plate=?1 and c.active =?2", CarEntity.class);
		query.setParameter(1, plate);
		query.setParameter(2, true);
		return query.getSingleResult();
	}
	
	@Transactional
	public List<CarEntity> getByCategory(Long categoryId) {
		TypedQuery<CarEntity> query = em.createQuery("Select c from CarEntity c where c.category.id = ?1 and c.active =?2",CarEntity.class);
		query.setParameter(1, categoryId);
		query.setParameter(2, true);
		return query.getResultList();
	}
	
	public Long countCars(StatusEnum status) {
		TypedQuery<Long> query = em.createQuery("Select COUNT(c.id) from CarEntity c where c.availability = ?1 and c.active = ?2", Long.class);
		query.setParameter(1, status);
		query.setParameter(2, true);
		return query.getSingleResult();
	}
	
	@Transactional
	public void save(CarEntity entity) {
		em.persist(entity);
	}
	
	@Transactional
	public void edit(CarEntity entity) {
		em.merge(entity);
	}
	
	public Long countRentedCars() {
		TypedQuery<Long> query = em.createQuery("Select COUNT(DISTINCT r.car.id) from ReservationEntity r where r.startDate <= ?1 and r.endDate >= ?1 and r.active = ?2", Long.class);
		query.setParameter(1, LocalDateTime.now());
		query.setParameter(2, true);
		return query.getSingleResult();
	}
	
	public void checkIfExistsAnother(String plate, Long id) throws NoResultException {
		TypedQuery<String> query = em.createQuery("Select c.plate from CarEntity c where c.plate = ?1 and c.id != ?2 and c.active = ?3", String.class);
		query.setParameter(1, plate);
		query.setParameter(2, id);
		query.setParameter(3, true);
		query.getSingleResult();
	}
}
