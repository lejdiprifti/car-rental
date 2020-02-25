package com.ikubinfo.rental.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
		TypedQuery<CarEntity> query = em.createQuery("Select c from CarEntity c where c.id = ?1 and c.active = ?2",
				CarEntity.class);
		query.setParameter(1, id);
		query.setParameter(2, true);
		return query.getSingleResult();
	}

	@Transactional
	public List<Object[]> getAll(int startIndex, int pageSize, List<Long> selectedCategoryIds, LocalDateTime startDate2,
			LocalDateTime endDate2) {
		Query query = em.createQuery(
				"Select c.id as id, c.photo as photo, c.name as name, c.description as description, c.plate as plate, c.diesel as diesel,"
						+ " c.type as type, c.year as year, c.price as price, c.availability as availability,"
						+ "c.category.id as categoryId, c.category.name as categoryName from CarEntity c where ((c.category.id IN (?1)) or ((?1) is NULL))"
						+ "and c.id NOT IN ( Select c2.id from ReservationEntity r "
						+ "JOIN CarEntity c2 ON c2.id = r.car.id "
						+ "where ((r.startDate >= ?2 and r.endDate >= ?3 and r.startDate <= ?3) or (r.startDate >= ?2 and r.endDate <= ?3 )"
						+ "or (r.startDate <= ?2 and r.endDate >= ?3 ) or (r.startDate <= ?2 and r.endDate >= ?2 and r.endDate <= ?3 )) and r.active = ?4) "
						+ " and c.active = ?4 ORDER BY c.id DESC");
		query.setParameter(1, selectedCategoryIds);
		query.setParameter(2, startDate2);
		query.setParameter(3, endDate2);
		query.setParameter(4, true);
		query.setFirstResult(startIndex);
		query.setMaxResults(pageSize);
		return query.getResultList();
	}

	public Long countAvailableCars(List<Long> selectedCategoryIds, LocalDateTime startDate2, LocalDateTime endDate2) {
		TypedQuery<Long> query = em.createQuery("Select Count(c.id) FROM CarEntity c where ((c.category.id IN (?1)) or ((?1) is NULL)) "
				+ "and c.id NOT IN ( Select c2.id from ReservationEntity r "
				+ "JOIN CarEntity c2 ON c2.id = r.car.id "
				+ "where ((r.startDate >= ?2 and r.endDate >= ?3 and r.startDate <= ?3) or (r.startDate >= ?2 and r.endDate <= ?3 )"
				+ "or (r.startDate <= ?2 and r.endDate >= ?3 ) or (r.startDate <= ?2 and r.endDate >= ?2 and r.endDate <= ?3 )) and r.active = ?4) "
				+ " and c.active = ?4 and c.availability <> ?5", Long.class);
		query.setParameter(1, selectedCategoryIds);
		query.setParameter(2, startDate2);
		query.setParameter(3, endDate2);
		query.setParameter(5, StatusEnum.SERVIS);
		query.setParameter(4, true);
		return query.getSingleResult();
	}

	public Long countAllCars(List<Long> selectedCategoryIds, LocalDateTime startDate2, LocalDateTime endDate2) {
		TypedQuery<Long> query = em.createQuery("Select COUNT(c.id) FROM CarEntity c where (((c.category.id IN (?1)) or ((?1) is NULL)) "
				+ "and c.id NOT IN ( Select r.car.id from ReservationEntity r "
				+ "where r.car.id=c.id and ((r.startDate >= ?2 and r.endDate >= ?3 and r.startDate <= ?3) or (r.startDate >= ?2 and r.endDate <= ?3 )"
				+ "or (r.startDate <= ?2 and r.endDate >= ?3 ) or (r.startDate <= ?2 and r.endDate >= ?2 and r.endDate <= ?3 )) and r.active = ?4) "
				+ " and c.active = ?4 )", Long.class);
		query.setParameter(1, selectedCategoryIds);
		query.setParameter(2, startDate2);
		query.setParameter(3, endDate2);
		query.setParameter(4, true);
		return query.getSingleResult();
	}

	@Transactional
	public List<Object[]> getAllAvailable(int startIndex, int pageSize, List<Long> selectedCategoryIds, LocalDateTime startDate2, LocalDateTime endDate2) {
		Query query = em.createQuery(
				"Select c.id as id, c.photo as photo, c.name as name, c.description as description, c.plate as plate, c.diesel as diesel,"
						+ " c.type as type, c.year as year, c.price as price, c.availability as availability,"
						+ "c.category.id as categoryId, c.category.name as categoryName from CarEntity c where ((c.category.id IN (?1)) or ((?1) is NULL))"
						+ "and c.id NOT IN ( Select c2.id from ReservationEntity r "
						+ "JOIN CarEntity c2 ON c2.id = r.car.id "
						+ "where ((r.startDate >= ?2 and r.endDate >= ?3 and r.startDate <= ?3) or (r.startDate >= ?2 and r.endDate <= ?3 )"
						+ "or (r.startDate <= ?2 and r.endDate >= ?3 ) or (r.startDate <= ?2 and r.endDate >= ?2 and r.endDate <= ?3 )) and r.active = ?4) "
						+ " and c.active = ?4 and c.availability <> ?5 ORDER BY c.id DESC");
		query.setParameter(1, selectedCategoryIds);
		query.setParameter(2, startDate2);
		query.setParameter(3, endDate2);
		query.setParameter(5, StatusEnum.SERVIS);
		query.setParameter(4, true);
		query.setFirstResult(startIndex);
		query.setMaxResults(pageSize);
		return query.getResultList();
	}

	@Transactional
	public List<Object[]> getAllAvailable() {
		Query query = em.createQuery(
				"Select c.id as id, c.photo as photo, c.name as name, c.description as description, c.plate as plate, c.diesel as diesel,"
						+ " c.type as type, c.year as year, c.price as price, c.availability as availability"
						+ ",c.category.id as categoryId, c.category.name as categoryName from CarEntity c where c.availability<>?1 and c.active = ?2 ORDER BY c.id DESC");
		query.setParameter(1, StatusEnum.SERVIS);
		query.setParameter(2, true);
		return query.getResultList();
	}

	@Transactional
	public CarEntity getByPlate(String plate) throws NoResultException {
		TypedQuery<CarEntity> query = em.createQuery("Select c from CarEntity c where c.plate=?1 and c.active =?2",
				CarEntity.class);
		query.setParameter(1, plate);
		query.setParameter(2, true);
		return query.getSingleResult();
	}

	@Transactional
	public List<CarEntity> getByCategory(Long categoryId) {
		TypedQuery<CarEntity> query = em
				.createQuery("Select c from CarEntity c where c.category.id = ?1 and c.active =?2", CarEntity.class);
		query.setParameter(1, categoryId);
		query.setParameter(2, true);
		return query.getResultList();
	}

	public Long countFreeCars() {
		TypedQuery<Long> query = em.createQuery(
				"Select COUNT(DISTINCT c.id) from CarEntity c where c.id NOT IN (SELECT r.car.id from ReservationEntity r where"
						+ " r.startDate <= ?1 and r.endDate >= ?1 and r.active = ?2) and c.active = ?2 and c.availability = ?3",
				Long.class);
		query.setParameter(1, LocalDateTime.now());
		query.setParameter(2, true);
		query.setParameter(3, StatusEnum.AVAILABLE);
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
		TypedQuery<Long> query = em.createQuery(
				"Select COUNT(DISTINCT r.car.id) from ReservationEntity r where r.startDate <= ?1 and r.endDate >= ?1 and r.active = ?2",
				Long.class);
		query.setParameter(1, LocalDateTime.now());
		query.setParameter(2, true);
		return query.getSingleResult();
	}

	public void checkIfExistsAnother(String plate, Long id) throws NoResultException {
		TypedQuery<String> query = em.createQuery(
				"Select c.plate from CarEntity c where c.plate = ?1 and c.id != ?2 and c.active = ?3", String.class);
		query.setParameter(1, plate);
		query.setParameter(2, id);
		query.setParameter(3, true);
		query.getSingleResult();
	}
}
