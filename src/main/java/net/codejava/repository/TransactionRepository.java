package net.codejava.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.codejava.domain.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    //    @Query("SELECT t FROM Transaction t " + "JOIN FETCH t.user u " + "WHERE u.id = :userId " +
    //            "AND ((:endTime IS NULL) OR (:startTime IS NULL) OR (t.createdAt < :endTime AND t.createdAt >=
    // :startTime))")
    @Query(
            value = "SELECT * FROM transactions t WHERE t.user_id = :userId "
                    + "AND ((:startTime IS NULL) OR (:endTime IS NULL) OR "
                    + "(t.created_at <= :endTime AND t.created_at >= :startTime))",
            nativeQuery = true)
    Page<Transaction> getListByUserId(
            @Param("userId") Integer userId, String startTime, String endTime, Pageable pageable);
}
