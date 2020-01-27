package com.ikubinfo.rental.repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ikubinfo.rental.entity.UserEntity;

@Repository
public class UserRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	public UserRepository() {
		
	}
	
	public UserEntity getByUsername(String username) throws NoResultException {
		TypedQuery<UserEntity> query = em.createQuery("Select u from UserEntity u where u.username=?1 and u.active=?2", UserEntity.class);
		query.setParameter(1, username);
		query.setParameter(2, true);
		return query.getSingleResult();
	}
	public UserEntity getByUsernameAndPassword(String username,String password) {
		TypedQuery<UserEntity> query = em.createQuery("Select u from UserEntity u where u.username=?1 and u.password=?2 and u.active=?3", UserEntity.class);
		query.setParameter(1, username);
		query.setParameter(2, password);
		query.setParameter(3, true);
		return query.getSingleResult();
	}
	
	
	@Transactional
	public void save(UserEntity user) {
		em.persist(user);
	}
}
