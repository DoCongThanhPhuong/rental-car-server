package net.codejava.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.codejava.domain.entity.User;
import net.codejava.domain.enums.UserType;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.id = :id AND u.userType = :user_type")
    Optional<User> findUserByIdAndUserType(@Param("id") Integer id, @Param("user_type") UserType userType);
}
