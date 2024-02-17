package com.example.pixels.repository;

import com.example.pixels.model.Address;
import com.example.pixels.service.AddressService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    public Address findByTheaterTheaterId(Long theaterId);
}
