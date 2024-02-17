package com.example.pixels.service;

import com.example.pixels.error.ItemNotFoundException;
import com.example.pixels.model.Address;
import org.springframework.stereotype.Service;

@Service
public interface AddressService {
    Address getAddressByTheaterId(Long theaterId) throws ItemNotFoundException;

    Address updateTheater(Long addressId, Address address) throws ItemNotFoundException;

    Address saveAddress(Address address);
}
