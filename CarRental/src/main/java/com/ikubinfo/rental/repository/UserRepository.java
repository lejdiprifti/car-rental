package com.ikubinfo.rental.repository;

import java.util.List;

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

	public List<UserEntity> getAll(int startIndex, int pageSize, String name) {
		TypedQuery<UserEntity> query = em
				.createQuery("Select u from UserEntity u where u.role.id = ?1 and u.active =?2 and ((u.firstName Like ?3) or (u.lastName Like ?3))", UserEntity.class);
		query.setParameter(1, 2);
		query.setParameter(2, true);
		query.setParameter(3, '%'+name+'%');
		query.setFirstResult(startIndex);
		query.setMaxResults(pageSize);
		return query.getResultList();
	}

	public UserEntity getById(Long id) throws NoResultException {
		TypedQuery<UserEntity> query = em.createQuery("Select u from UserEntity u where u.id = ?1 and u.active =?2",
				UserEntity.class);
		query.setParameter(1, id);
		query.setParameter(2, true);
		return query.getSingleResult();
	}

	public UserEntity getByUsername(String username) throws NoResultException {
		TypedQuery<UserEntity> query = em.createQuery("Select u from UserEntity u where u.username=?1 and u.active=?2",
				UserEntity.class);
		query.setParameter(1, username);
		query.setParameter(2, true);
		return query.getSingleResult();
	}

	public UserEntity getByUsernameAndPassword(String username, String password) {
		TypedQuery<UserEntity> query = em.createQuery(
				"Select u from UserEntity u where u.username=?1 and u.password=?2 and u.active=?3", UserEntity.class);
		query.setParameter(1, username);
		query.setParameter(2, password);
		query.setParameter(3, true);
		return query.getSingleResult();
	}

	public Long countActiveUsers(String name) {
		TypedQuery<Long> query = em
				.createQuery("Select Count(u.id) from UserEntity u where u.role.id = ?1 and u.active = ?2 and ((u.firstName LIKE ?3) or "
						+ "(u.lastName LIKE ?3))", Long.class);
		query.setParameter(1, 2);
		query.setParameter(2, true);
		query.setParameter(3, name);
		return query.getSingleResult();
	}
	
	public Long countActiveUsers() {
		TypedQuery<Long> query = em
				.createQuery("Select Count(u.id) from UserEntity u where u.role.id = ?1 and u.active = ?2", Long.class);
		query.setParameter(1, 2);
		query.setParameter(2, true);
		return query.getSingleResult();
	}


	@Transactional
	public void save(UserEntity user) {
		em.persist(user);
	}

	@Transactional
	public void edit(UserEntity user) {
		em.merge(user);
	}
}
