package net.codejava.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.codejava.domain.entity.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {
    @Query("SELECT DISTINCT c FROM Car c " + "JOIN FETCH c.carOwner co "
            + "LEFT JOIN FETCH c.images i "
            + "WHERE co.id = :ownerId")
    Page<Car> getListCarByOwner(@Param(("ownerId")) Integer ownerId, Pageable pageable);

    @Query("SELECT DISTINCT c FROM Car c " + "JOIN FETCH c.carOwner co "
            + "LEFT JOIN FETCH c.images i "
            + "WHERE co.id = :ownerId "
            + "AND CONCAT(c.name,' ',c.brand,' ',c.model,' ',c.address) LIKE %:keyword%")
    Page<Car> getListCarByOwnerWithKeyword(
            @Param("ownerId") Integer ownerId, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT c FROM Car c " + "INNER JOIN Image i ON c.id = i.id " + "WHERE c.id = :id")
    Optional<Car> findByIdWithImage(@Param("id") Integer id);

    @Query("SELECT c FROM Car c " + "JOIN FETCH c.carOwner "
            +
            //            "JOIN FETCH c.imageList " +
            "WHERE c.id = :id")
    Optional<Car> findByIdWithOwner(@Param("id") Integer id);

    @Query(
            value =
                    "SELECT * FROM cars c\n"
                            + "WHERE c.is_stopped = false AND c.address LIKE %:address% AND c.car_id NOT IN (\n"
                            + "\t\t\t\t\tSELECT b.car_id FROM bookings b \n"
                            + "                    WHERE b.`status` != 'COMPLETED' AND b.`status` != 'CANCELLED' \n"
                            + "\t\t\t\t\t\tAND ((b.end_date_time >= :endTime AND b.start_date_time <= :endTime) \n"
                            + "\t\t\t\t\t\t\t\tOR (b.start_date_time <= :startTime AND b.end_date_time >= :startTime) \n"
                            + "                                OR (b.end_date_time <= :endTime AND b.start_date_time >= :startTime)))",
            nativeQuery = true)
    Page<Car> searchCarV2(
            @Param("address") String address,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * FROM cars c\n"
                            + "WHERE c.car_id = :carId AND c.is_stopped = false AND c.car_id NOT IN (\n"
                            + "\t\t\t\t\tSELECT b.car_id FROM bookings b \n"
                            + "                    WHERE b.`status` != 'COMPLETED' AND b.`status` != 'CANCELLED' \n"
                            + "\t\t\t\t\t\tAND ((b.end_date_time >= :endTime AND b.start_date_time <= :endTime) \n"
                            + "\t\t\t\t\t\t\t\tOR (b.start_date_time <= :startTime AND b.end_date_time >= :startTime) \n"
                            + "                                OR (b.end_date_time <= :endTime AND b.start_date_time >= :startTime)))",
            nativeQuery = true)
    Optional<Car> checkScheduleCar(
            @Param("carId") Integer carId, @Param("startTime") String startTime, @Param("endTime") String endTime);
}
