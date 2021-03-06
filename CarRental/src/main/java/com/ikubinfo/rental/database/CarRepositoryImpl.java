package com.ikubinfo.rental.database;

import com.ikubinfo.rental.service.car.dto.CarEntity;
import com.ikubinfo.rental.service.car.dto.CarFilter;
import com.ikubinfo.rental.service.car.repository.CarRepository;
import com.ikubinfo.rental.service.car.status.StatusEnum;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CarRepositoryImpl implements CarRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public CarEntity getById(Long id) throws NoResultException {
        TypedQuery<CarEntity> query = em.createQuery("Select c from CarEntity c where c.id = ?1 and c.active = ?2",
                CarEntity.class);
        query.setParameter(1, id);
        query.setParameter(2, true);
        return query.getSingleResult();
    }

    @Override
    @Transactional
    public List<Object[]> getAll(CarFilter carFilter) {
        Query query = em.createQuery(
                "Select c.id as id, c.photo as photo, c.name as name, c.description as description, c.plate as plate, c.diesel as diesel,"
                        + " c.type as type, c.year as year, c.price as price, c.availability as availability,"
                        + "c.category.id as categoryId, c.category.name as categoryName from CarEntity c where ((c.category.id IN (?1)) or ((?1) is NULL))"
                        + "and c.id NOT IN ( Select c2.id from ReservationEntity r "
                        + "JOIN CarEntity c2 ON c2.id = r.car.id "
                        + "where ((r.startDate >= ?2 and r.endDate >= ?3 and r.startDate <= ?3) or (r.startDate >= ?2 and r.endDate <= ?3 )"
                        + "or (r.startDate <= ?2 and r.endDate >= ?3 ) or (r.startDate <= ?2 and r.endDate >= ?2 and r.endDate <= ?3 )) and r.active = ?4) "
                        + " and c.active = ?4 and (c.type LIKE ?5) ORDER BY c.id DESC");
        query.setParameter(1, carFilter.getSelectedCategoryIds());
        query.setParameter(2, carFilter.getStartLocalDateTime());
        query.setParameter(3, carFilter.getEndLocalDateTime());
        query.setParameter(4, true);
        query.setParameter(5, '%' + carFilter.getBrand() + '%');
        query.setFirstResult(carFilter.getStartIndex());
        query.setMaxResults(carFilter.getPageSize());
        return query.getResultList();
    }

    @Override
    public Long countAvailableCars(CarFilter carFilter) {
        TypedQuery<Long> query = em
                .createQuery("Select Count(c.id) FROM CarEntity c where ((c.category.id IN (?1)) or ((?1) is NULL)) "
                        + "and c.id NOT IN ( Select c2.id from ReservationEntity r "
                        + "JOIN CarEntity c2 ON c2.id = r.car.id "
                        + "where ((r.startDate >= ?2 and r.endDate >= ?3 and r.startDate <= ?3) or (r.startDate >= ?2 and r.endDate <= ?3 )"
                        + "or (r.startDate <= ?2 and r.endDate >= ?3 ) or (r.startDate <= ?2 and r.endDate >= ?2 and r.endDate <= ?3 )) and r.active = ?4) "
                        + " and c.active = ?4 and c.availability <> ?5 and (c.type LIKE ?6)", Long.class);
        query.setParameter(1, carFilter.getSelectedCategoryIds());
        query.setParameter(2, carFilter.getStartLocalDateTime());
        query.setParameter(3, carFilter.getEndLocalDateTime());
        query.setParameter(5, StatusEnum.SERVIS);
        query.setParameter(6, '%' + carFilter.getBrand() + '%');
        query.setParameter(4, true);
        return query.getSingleResult();
    }

    @Override
    public Long countAllCars(CarFilter carFilter) {
        TypedQuery<Long> query = em
                .createQuery("Select COUNT(c.id) FROM CarEntity c where (((c.category.id IN (?1)) or ((?1) is NULL)) "
                        + "and c.id NOT IN ( Select r.car.id from ReservationEntity r "
                        + "where r.car.id=c.id and ((r.startDate >= ?2 and r.endDate >= ?3 and r.startDate <= ?3) or (r.startDate >= ?2 and r.endDate <= ?3 )"
                        + "or (r.startDate <= ?2 and r.endDate >= ?3 ) or (r.startDate <= ?2 and r.endDate >= ?2 and r.endDate <= ?3 )) and r.active = ?4) "
                        + " and c.active = ?4 and (c.type LIKE ?5))", Long.class);
        query.setParameter(1, carFilter.getSelectedCategoryIds());
        query.setParameter(2, carFilter.getStartLocalDateTime());
        query.setParameter(3, carFilter.getEndLocalDateTime());
        query.setParameter(4, true);
        query.setParameter(5, '%' + carFilter.getBrand() + '%');
        return query.getSingleResult();
    }

    @Override
    @Transactional
    public List<Object[]> getAllAvailable(CarFilter carFilter) {
        Query query = em.createQuery(
                "Select c.id as id, c.photo as photo, c.name as name, c.description as description, c.plate as plate, c.diesel as diesel,"
                        + " c.type as type, c.year as year, c.price as price, c.availability as availability,"
                        + "c.category.id as categoryId, c.category.name as categoryName from CarEntity c where ((c.category.id IN (?1)) or ((?1) is NULL))"
                        + "and c.id NOT IN ( Select c2.id from ReservationEntity r "
                        + "JOIN CarEntity c2 ON c2.id = r.car.id "
                        + "where ((r.startDate >= ?2 and r.endDate >= ?3 and r.startDate <= ?3) or (r.startDate >= ?2 and r.endDate <= ?3 )"
                        + "or (r.startDate <= ?2 and r.endDate >= ?3 ) or (r.startDate <= ?2 and r.endDate >= ?2 and r.endDate <= ?3 )) and r.active = ?4) "
                        + " and c.active = ?4 and c.availability <> ?5 and (c.type LIKE ?6) ORDER BY c.id DESC");
        query.setParameter(1, carFilter.getSelectedCategoryIds());
        query.setParameter(2, carFilter.getStartLocalDateTime());
        query.setParameter(3, carFilter.getEndLocalDateTime());
        query.setParameter(5, StatusEnum.SERVIS);
        query.setParameter(6, '%' + carFilter.getBrand() + '%');
        query.setParameter(4, true);
        query.setFirstResult(carFilter.getStartIndex());
        query.setMaxResults(carFilter.getPageSize());
        return query.getResultList();
    }

    @Override
    @Transactional
    public CarEntity getByPlate(String plate) throws NoResultException {
        TypedQuery<CarEntity> query = em.createQuery("Select c from CarEntity c where c.plate=?1 and c.active =?2",
                CarEntity.class);
        query.setParameter(1, plate);
        query.setParameter(2, true);
        return query.getSingleResult();
    }

    @Override
    @Transactional
    public List<CarEntity> getByCategory(Long categoryId) {
        TypedQuery<CarEntity> query = em
                .createQuery("Select c from CarEntity c where c.category.id = ?1 and c.active =?2", CarEntity.class);
        query.setParameter(1, categoryId);
        query.setParameter(2, true);
        return query.getResultList();
    }

    @Override
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

    @Override
    @Transactional
    public CarEntity save(CarEntity entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    @Transactional
    public void edit(CarEntity entity) {
        em.merge(entity);
    }

    @Override
    public Long countRentedCars() {
        TypedQuery<Long> query = em.createQuery(
                "Select COUNT(DISTINCT r.car.id) from ReservationEntity r where r.startDate <= ?1 and r.endDate >= ?1 and r.active = ?2",
                Long.class);
        query.setParameter(1, LocalDateTime.now());
        query.setParameter(2, true);
        return query.getSingleResult();
    }

    @Override
    public void checkIfExistsAnother(String plate, Long id) throws NoResultException {
        TypedQuery<String> query = em.createQuery(
                "Select c.plate from CarEntity c where c.plate = ?1 and c.id <> ?2 and c.active = ?3", String.class);
        query.setParameter(1, plate);
        query.setParameter(2, id);
        query.setParameter(3, true);
        query.getSingleResult();
    }

    @Override
    public Long countActiveReservationsByCar(Long carId) {
        TypedQuery<Long> query = em.createQuery(
                "Select COUNT(r.id) from ReservationEntity r where r.car.id = ?1 and r.active =?2 and (r.startDate > ?3 or r.endDate > ?3)",
                Long.class);
        query.setParameter(1, carId);
        query.setParameter(2, true);
        query.setParameter(3, LocalDateTime.now());
        return query.getSingleResult();
    }
}
