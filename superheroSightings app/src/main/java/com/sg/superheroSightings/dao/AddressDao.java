/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superheroSightings.dao;

import com.sg.superheroSightings.dto.Address;
import java.util.List;

/**
 *
 * @author pc
 */
public interface AddressDao {

    Address getAddressById(int id);
    List<Address> getAllAddresses();
    Address addAddress(Address address);
    void updateAddress(Address address);
    void deleteAddressById(int id);

}
