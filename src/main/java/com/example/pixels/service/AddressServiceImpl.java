package com.example.pixels.service;

import com.example.pixels.error.ItemNotFoundException;
import com.example.pixels.model.Address;
import com.example.pixels.repository.AddressRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Transactional
public class AddressServiceImpl implements AddressService{

    @Autowired
    AddressRepository addressRepository;

    private final ModelMapper modelMapper = new ModelMapper();
    @Override
    public Address getAddressByTheaterId(Long theaterId) throws ItemNotFoundException {
        Optional<Address> address = Optional.ofNullable(addressRepository.findByTheaterTheaterId(theaterId));
        if(address.isEmpty())
            throw new ItemNotFoundException("Address not Found.");
        return address.get();
    }

    @Override
    public Address updateTheater(Long addressId, Address updatedAddress) throws ItemNotFoundException {
        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new ItemNotFoundException("Address not found."));
        modelMapper.map(updatedAddress, existingAddress);

        return addressRepository.save(existingAddress);
    }

    @Override
    public Address saveAddress(Address address) {
        return addressRepository.save(address);
    }
}
