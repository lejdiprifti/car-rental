package com.ikubinfo.rental.repository;

import com.ikubinfo.rental.entity.ReservationEntity;
import com.ikubinfo.rental.model.ReservedDates;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

@Repository
public class ReservationRepository {

    @PersistenceContext
    private EntityManager em;

    public List<Object[]> getAll() {
        Query query = em.createQuery(
                "Select r.id, r.startDate, r.endDate, c.id, c.name, c.type, c.price, u.id, u.firstName, u.lastName, c.plate from ReservationEntity r "
                        + "Join CarEntity c ON c.id = r.car " + "Join UserEntity u ON u.id = r.user "
                        + "where r.active=?1 order by r.created_at DESC");
        query.setParameter(1, true);
        return query.getResultList();
    }

    @Transactional
    public ReservationEntity getById(Long id) throws NoResultException {
        TypedQuery<ReservationEntity> query = em.createQuery(
                "Select r from ReservationEntity r where r.id=?1 and r.active=?2", ReservationEntity.class);
        query.setParameter(1, id);
        query.setParameter(2, true);
        return query.getSingleResult();
    }

    @Transactional
    public List<Object[]> getByUser(String username, int startIndex, int pageSize, String carName,
                                    LocalDateTime startDate, LocalDateTime endDate) throws NoResultException {
        Query query = em.createQuery(
                "Select r.id, r.startDate, r.endDate, c.id, c.name, c.type, c.price, u.id, u.firstName, u.lastName,c.plate, c.photo from ReservationEntity r "
                        + "Join CarEntity c ON c.id = r.car " + "Join UserEntity u ON u.id = r.user "
                        + "where u.username = ?1 and r.active = ?2 and (COALESCE(c.name, ?3) LIKE ?3 or COALESCE(c.type,?3) LIKE ?3) and COALESCE(r.startDate,?4) >= ?4 "
                        + "and COALESCE(r.endDate,?5) <= ?5 " + "Order by r.created_at DESC");
        query.setParameter(1, username);
        query.setParameter(2, true);
        query.setParameter(3, "%" + carName + "%");
        query.setParameter(4, startDate);
        query.setParameter(5, endDate);
        query.setFirstResult(startIndex);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    @Transactional
    public List<Object[]> getByUser(String username) throws NoResultException {
        Query query = em.createQuery(
                "Select r.id, r.startDate, r.endDate, c.id, c.name, c.type, c.price, u.id, u.firstName, u.lastName,c.plate, c.photo from ReservationEntity r "
                        + "Join CarEntity c ON c.id = r.car " + "Join UserEntity u ON u.id = r.user "
                        + "where u.username = ?1 and r.active = ?2 Order by r.created_at DESC");
        query.setParameter(1, username);
        query.setParameter(2, true);
        return query.getResultList();
    }

    public List<ReservedDates> getReservedDatesByCar(Long carId) {
        Query query = em.createQuery(
                "Select r.startDate, r.endDate from ReservationEntity r where r.car.id = ?1 and r.active =?2 and (r.startDate >= ?3 or r.endDate >= ?3)");
        query.setParameter(1, carId);
        query.setParameter(2, true);
        query.setParameter(3, LocalDateTime.now());
        return query.getResultList();
    }

    @Transactional
    public List<ReservationEntity> getByCar(Long carId) {
        TypedQuery<ReservationEntity> query = em.createQuery(
                "Select r from ReservationEntity r where r.car.id = ?1 and r.active = ?2 and (r.startDate >= ?3 or r.endDate >= ?3)",
                ReservationEntity.class);
        query.setParameter(1, carId);
        query.setParameter(2, true);
        query.setParameter(3, LocalDateTime.now());
        return query.getResultList();
    }

    public Long checkIfAvailable(Long carId, LocalDateTime startDate, LocalDateTime endDate) {
        TypedQuery<Long> query = em.createQuery(
                "Select COUNT(r.id) from ReservationEntity r where r.car.id = ?1 and ((r.startDate >= ?2 and r.endDate >= ?3 and r.startDate <= ?3) or (r.startDate >= ?2 and r.endDate <= ?3)"
                        + "or (r.startDate <= ?2 and r.endDate >= ?3) or (r.startDate <= ?2 and r.endDate >= ?2 and r.endDate <= ?3)) and r.active= ?4",
                Long.class);
        query.setParameter(1, carId);
        query.setParameter(2, startDate);
        query.setParameter(3, endDate);
        query.setParameter(4, true);
        return query.getSingleResult();
    }

    public Long updateIfAvailable(Long carId, LocalDateTime startDate, LocalDateTime endDate, Long id) {
        TypedQuery<Long> query = em.createQuery(
                "Select COUNT(r.id) from ReservationEntity r where r.car.id = ?1 and ((r.startDate >= ?2 and r.endDate >= ?3 and r.startDate <= ?3) or (r.startDate >= ?2 and r.endDate <= ?3)"
                        + "or (r.startDate <= ?2 and r.endDate >= ?2 and r.endDate <= ?3)) and r.active= ?4 and r.id <> ?5",
                Long.class);
        query.setParameter(1, carId);
        query.setParameter(2, startDate);
        query.setParameter(3, endDate);
        query.setParameter(4, true);
        query.setParameter(5, id);
        return query.getSingleResult();
    }

    public Long countNewBookings(Calendar date) {
        TypedQuery<Long> query = em.createQuery(
                "Select COUNT(r.id) from ReservationEntity r where r.created_at > ?1 and r.active = ?2", Long.class);
        query.setParameter(1, date);
        query.setParameter(2, true);
        return query.getSingleResult();
    }

    public Long countActiveReservationsByCar(Long carId) {
        TypedQuery<Long> query = em.createQuery(
                "Select COUNT(r.id) from ReservationEntity r where r.car.id = ?1 and r.active =?2 and (r.startDate > ?3 or r.endDate > ?3)",
                Long.class);
        query.setParameter(1, carId);
        query.setParameter(2, true);
        query.setParameter(3, LocalDateTime.now());
        return query.getSingleResult();
    }

    @Transactional
    public List<ReservationEntity> getByCarAndDate(LocalDateTime date, Long carId) {
        TypedQuery<ReservationEntity> query = em.createQuery(
                "Select r from ReservationEntity r where r.active = ?1 and (r.startDate <=?2 or r.endDate <=?2) and r.car.id = ?3",
                ReservationEntity.class);
        query.setParameter(1, true);
        query.setParameter(2, date);
        query.setParameter(3, carId);
        return query.getResultList();
    }

    public Long countNumberOfReservationsByUsername(String username, String carName, LocalDateTime startDate,
                                                    LocalDateTime endDate) {
        TypedQuery<Long> query = em.createQuery(
                "Select Count(r.id) from ReservationEntity r where r.user.username= ?1 and r.active = ?2 and (COALESCE(r.car.name, ?3) LIKE ?3 or COALESCE(r.car.type,?3) LIKE ?3) and COALESCE(r.startDate,?4) >= ?4 "
                        + " and COALESCE(r.endDate,?5) <= ?5 ",
                Long.class);
        query.setParameter(1, username);
        query.setParameter(2, true);
        query.setParameter(3, "%" + carName + "%");
        query.setParameter(4, startDate);
        query.setParameter(5, endDate);
        return query.getSingleResult();
    }

    @Transactional
    public ReservationEntity save(ReservationEntity entity) {
        em.persist(entity);
        return entity;
    }

    @Transactional
    public void edit(ReservationEntity entity) {
        em.merge(entity);
    }
}
