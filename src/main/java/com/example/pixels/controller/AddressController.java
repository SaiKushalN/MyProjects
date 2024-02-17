package com.example.pixels.controller;

import com.example.pixels.error.ItemNotFoundException;
import com.example.pixels.model.Address;
import com.example.pixels.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/address")
public class AddressController {

    //Do Exception handling.

    @Autowired
    AddressService addressService;

    @GetMapping("/{theaterId}")
    public Address getAddressByTheaterId(@PathVariable("theaterId") Long theaterId) throws ItemNotFoundException {
        return addressService.getAddressByTheaterId(theaterId);
    }

//    @PostMapping("/{theaterId}/addAddress")
//    public Address saveAddress(@PathVariable("theaterId") Long theaterId,
//                               @RequestBody Address address) {
//        return addressService.saveAddress(address, theaterId);
//    }

//    public Address saveAddress(Address address) {
//        return addressService.saveAddress(address);
//    }

    @PutMapping("/updateAddress/{addressId}")
    public Address updateAddress(@PathVariable("addressId") Long addressId, @RequestBody Address address) throws ItemNotFoundException {
        return addressService.updateTheater(addressId, address);
    }

}
