package com.ikubinfo.rental.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ikubinfo.rental.entity.ReservationEntity;

@Repository
public class ReservationRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	public ReservationRepository() {
		
	}
	
	
	@Transactional
	public List<ReservationEntity> getAll() {
		TypedQuery<ReservationEntity> query = em.createQuery("Select r from ReservationEntity r order by r.startDate", ReservationEntity.class);
		return query.getResultList();
	}
	
	@Transactional
	public ReservationEntity getById(Long id) throws NoResultException {
		TypedQuery<ReservationEntity> query = em.createQuery("Select r from ReservationEntity r where r.id=?1", ReservationEntity.class);
		query.setParameter(1, id);
		return query.getSingleResult();
	}
	
	@Transactional
	public List<ReservationEntity> getByUser(String username) throws NoResultException {
		TypedQuery<ReservationEntity> query = em.createQuery("Select r from ReservationEntity r where r.user.username = ?1", ReservationEntity.class);
		query.setParameter(1, username);
		return query.getResultList();
	}
	
	@Transactional
	public List<ReservationEntity> getByCar(Long carId) {
		TypedQuery<ReservationEntity> query = em.createQuery("Select r from ReservationEntity r where r.car.id = ?1 and r.active = ?2", ReservationEntity.class);
		query.setParameter(1, carId);
		query.setParameter(2, true);
		return query.getResultList();
	}
	
	@Transactional
	public boolean checkIfAvailable(Long carId, LocalDateTime startDate, LocalDateTime endDate) {
		TypedQuery<ReservationEntity> query = em.createQuery("Select r from ReservationEntity r where r.car.id = ?1 and r.endDate > ?2 and r.startDate < ?3 and r.active= ?4", ReservationEntity.class);
		query.setParameter(1, carId);
		query.setParameter(2, startDate);
		query.setParameter(3, endDate);
		query.setParameter(4, true);
		if (query.getResultList().size() > 0) {
			return false;
		} else {
			return true;
		}
	}
	
	@Transactional
	public void save(ReservationEntity entity) {
		em.persist(entity);
	}
	
	@Transactional
	public void edit(ReservationEntity entity) {
		em.merge(entity);
	}
}
