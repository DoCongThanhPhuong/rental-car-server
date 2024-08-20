package net.codejava.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.codejava.domain.entity.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    @Query(
            value = "SELECT f.* FROM feedbacks f " + "INNER JOIN bookings b ON f.booking_id = b.booking_id "
                    + "INNER JOIN cars c ON b.car_id = c.car_id WHERE c.car_owner_id = :ownerId "
                    + "AND ( :rating IS NULL OR f.rating = :rating)",
            countQuery = "SELECT COUNT(*) FROM feedbacks f " + "INNER JOIN bookings b ON f.booking_id = b.booking_id "
                    + "INNER JOIN cars c ON b.car_id = c.car_id "
                    + "WHERE c.car_owner_id = :ownerId "
                    + "AND (:rating IS NULL OR f.rating = :rating)",
            nativeQuery = true)
    Page<Feedback> getListByOwnerId(
            @Param("ownerId") Integer ownerId, @Param("rating") Integer rating, Pageable pageable);

    @Query(
            value = "SELECT AVG(f.rating) FROM feedbacks f " + "INNER JOIN bookings b ON f.booking_id = b.booking_id "
                    + "INNER JOIN cars c ON b.car_id = c.car_id "
                    + "WHERE c.car_owner_id = :ownerId;",
            nativeQuery = true)
    Double getRatingByOwner(@Param("ownerId") Integer ownerId);

    @Query(
            value = "SELECT AVG(f.rating) FROM feedbacks f "
                    + "INNER JOIN bookings b ON f.booking_id = b.booking_id WHERE b.car_id = :carId;",
            nativeQuery = true)
    Double getRatingByCarId(@Param("carId") Integer carId);
}
