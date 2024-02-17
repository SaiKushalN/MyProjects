package com.example.pixels.repository;

import com.example.pixels.model.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {
    public List<Theater> findByTheaterNameIgnoreCase(String theaterName);

    List<Theater> findByTheaterAddressCityIgnoreCase(String city);

    List<Theater> findByTheaterAddressZipcode(Long zipcode);

    List<Theater> findByTheaterAddressStateIgnoreCase(String state);
}
