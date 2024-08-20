package net.codejava.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.codejava.domain.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query("SELECT b FROM Booking b " + "JOIN FETCH b.car c " + "JOIN FETCH b.user u " + "WHERE u.id = :userId")
    Page<Booking> getListBookingByUserId(@Param("userId") Integer userId, Pageable pageable);

    @Query("SELECT b FROM Booking b " + "JOIN FETCH b.car c " + "JOIN FETCH b.user u " + "WHERE c.id = :carId")
    Page<Booking> getListBookingByCarId(@Param("carId") Integer carId, Pageable pageable);

    @Query("SELECT b FROM Booking b " + "JOIN FETCH b.car c "
            + "JOIN FETCH b.user u "
            + "WHERE u.id = :userId AND CONCAT(b.status,' ',b.paymentMethod) LIKE %:keyword%")
    Page<Booking> getListBookingByUserIdWithKeyword(
            @Param("userId") Integer userId, @Param("keyword") String keyword, Pageable pageable);
}
