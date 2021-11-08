/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superheroSightings.dao;

import com.sg.superheroSightings.dto.Address;
import com.sg.superheroSightings.dto.Location;
import com.sg.superheroSightings.dto.Organisation;
import com.sg.superheroSightings.dto.Sighting;
import com.sg.superheroSightings.dto.SuperCreature;
import com.sg.superheroSightings.dto.SuperPower;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author pc
 */
@SpringBootTest
public class AddressDaoDBTest {

    @Autowired
    AddressDao addressDao;

    @Autowired
    SuperPowerDao superPowerDao;

    @Autowired
    SightingDao sightingDao;

    @Autowired
    SuperCreatureDao superCreatureDao;

    @Autowired
    LocationDao locationDao;

    @Autowired
    OrganisationDao organisationDao;

    public AddressDaoDBTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {

        List<Sighting> sightings = sightingDao.getAllSighting();
        for (Sighting sighting : sightings) {
            sightingDao.deleteSightingById(sighting.getId());
        }

        List<SuperCreature> superCreaturs = superCreatureDao.getAllSuperCreatures();
        for (SuperCreature superCreature : superCreaturs) {
            superCreatureDao.deleteSuperCreatureById(superCreature.getId());
        }

        List<Location> locations = locationDao.getAllLocations();
        for (Location location : locations) {
            locationDao.deleteLocationById(location.getId());
        }

        List<Organisation> organisations = organisationDao.getAllOrganisations();
        for (Organisation organisation : organisations) {
            organisationDao.deleteOrganisationById(organisation.getId());
        }

        List<Address> addresses = addressDao.getAllAddresses();
        for (Address address : addresses) {
            addressDao.deleteAddressById(address.getId());
        }

        List<SuperPower> superPowers = superPowerDao.getAllSuperPowers();
        for (SuperPower superPower : superPowers) {
            superPowerDao.deleteSuperPowerById(superPower.getId());
        }

    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testAddAndGetAddress() {
        Address address = new Address();
        address.setStreet("street");
        address.setCity("city");
        address.setState("state");
        address.setCountry("country");
        address.setPostalCode("postal");
        address = addressDao.addAddress(address);

        Address fromDao = addressDao.getAddressById(address.getId());
        assertEquals(address, fromDao);

        Address duplicate = addressDao.addAddress(address);
        assertNull(duplicate);
    }

    @Test
    public void testGetAllAddresses() {
        Address address = new Address();
        address.setStreet("street");
        address.setCity("city");
        address.setState("state");
        address.setCountry("country");
        address.setPostalCode("postal");
        address = addressDao.addAddress(address);

        Address address2 = new Address();
        address2.setStreet("street2");
        address2.setCity("city2");
        address2.setState("state2");
        address2.setCountry("country2");
        address2.setPostalCode("postal");
        address2 = addressDao.addAddress(address2);

        List<Address> addresses = addressDao.getAllAddresses();

        assertEquals(2, addresses.size());
        assertTrue(addresses.contains(address));
        assertTrue(addresses.contains(address2));
    }

    @Test
    public void testUpdateTeacher() {
        Address address = new Address();
        address.setStreet("street");
        address.setCity("city");
        address.setState("state");
        address.setCountry("country");
        address.setPostalCode("postal");
        address = addressDao.addAddress(address);

        Address fromDao = addressDao.getAddressById(address.getId());
        assertEquals(address, fromDao);

        address.setCity("ottawa");
        addressDao.updateAddress(address);

        assertNotEquals(address, fromDao);

        Address fromDao2 = addressDao.getAddressById(address.getId());

        assertEquals(address, fromDao2);
    }

    @Test
    public void testDeleteAddressById() {
        Address address = new Address();
        address.setStreet("street");
        address.setCity("city");
        address.setState("state");
        address.setCountry("country");
        address.setPostalCode("postal");
        address = addressDao.addAddress(address);

        Address fromDao = addressDao.getAddressById(address.getId());
        assertEquals(address, fromDao);

        addressDao.deleteAddressById(address.getId());

        fromDao = addressDao.getAddressById(address.getId());
        assertNull(fromDao);
    }

}
