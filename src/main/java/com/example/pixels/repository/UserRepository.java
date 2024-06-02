package com.example.pixels.repository;


import com.example.pixels.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUserEmail(String email);

    Optional<List<User>> findAllByUserRoleContains(String critic);

    @Query(value = "SELECT u.full_name FROM users u WHERE ',' || u.user_role || ',' LIKE '%,CRITIC,%'", nativeQuery = true)
    List<String> findAllCriticFullNames();
}
