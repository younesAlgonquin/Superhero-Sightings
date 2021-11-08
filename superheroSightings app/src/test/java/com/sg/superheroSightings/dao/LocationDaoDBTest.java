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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
public class LocationDaoDBTest {

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

    public LocationDaoDBTest() {
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
        for (SuperCreature teacher : superCreaturs) {
            superCreatureDao.deleteSuperCreatureById(teacher.getId());
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
    public void testAddAndGetLocation() {

        Address address = new Address();
        address.setStreet("street");
        address.setCity("city");
        address.setState("state");
        address.setCountry("country");
        address.setPostalCode("111111");
        address = addressDao.addAddress(address);

        Location location = new Location();
        location.setName("location");
        location.setDescription("sighting location");
        location.setLatitude(new BigDecimal("55.55555"));
        location.setLongitude(new BigDecimal("66.66666"));
        location.setAddress(address);
        location = locationDao.addLocation(location);

        assertNotNull(location);
        //assertEquals(location.getId(), 23);

        Location fromDao = locationDao.getLocationById(location.getId());
        assertNotNull(fromDao.getAddress());
        assertEquals(location, fromDao);

        //assertEquals(fromDao.getId(), 25);
        Location duplicate = locationDao.addLocation(location);
        assertNull(duplicate);

        //add location with a new address
        Address address2 = new Address();
        address2.setStreet("street2");
        address2.setCity("city2");
        address2.setState("state2");
        address2.setCountry("country2");
        address2.setPostalCode("222222");

        Location location2 = new Location();
        location2.setName("location2");
        location2.setDescription("sighting location2");
        location2.setLatitude(new BigDecimal("22.22222"));
        location2.setLongitude(new BigDecimal("33.33333"));
        location2.setAddress(address2);
        location2 = locationDao.addLocation(location2);

        Location fromDao2 = locationDao.getLocationById(location.getId());
        assertNotNull(fromDao2.getAddress());
        assertEquals(location.getAddress(), fromDao2.getAddress());
    }

    @Test
    public void testGetAlllocations() {

        Address address = new Address();
        address.setStreet("street");
        address.setCity("city");
        address.setState("state");
        address.setCountry("country");
        address.setPostalCode("111111");
        address = addressDao.addAddress(address);

        Location location = new Location();
        location.setName("location");
        location.setDescription("sighting location");
        location.setLatitude(new BigDecimal("55.55555"));
        location.setLongitude(new BigDecimal("66.66666"));
        location.setAddress(address);
        location = locationDao.addLocation(location);

        Address address2 = new Address();
        address2.setStreet("street2");
        address2.setCity("city2");
        address2.setState("state2");
        address2.setCountry("country2");
        address2.setPostalCode("222222");
        address2 = addressDao.addAddress(address2);

        Location location2 = new Location();
        location2.setName("location2");
        location2.setDescription("sighting location2");
        location2.setLatitude(new BigDecimal("22.22222"));
        location2.setLongitude(new BigDecimal("33.33333"));
        location2.setAddress(address2);
        location2 = locationDao.addLocation(location2);

        List<Location> locations = locationDao.getAllLocations();

        assertEquals(2, locations.size());
        assertTrue(locations.contains(location));
        assertTrue(locations.contains(location2));
    }

    @Test
    public void testUpdateLocation() {

        Address address = new Address();
        address.setStreet("street");
        address.setCity("city");
        address.setState("state");
        address.setCountry("country");
        address.setPostalCode("111111");
        address = addressDao.addAddress(address);

        Location location = new Location();
        location.setName("location");
        location.setDescription("sighting location");
        location.setLatitude(new BigDecimal("55.55555"));
        location.setLongitude(new BigDecimal("66.66666"));
        location.setAddress(address);
        location = locationDao.addLocation(location);

        Location fromDao = locationDao.getLocationById(location.getId());
        assertEquals(location, fromDao);

        location.setName("location2");
        locationDao.updateLocation(location);
        assertNotEquals(location, fromDao);

        Location fromDao2 = locationDao.getLocationById(location.getId());
        assertEquals(location, fromDao2);
    }

    @Test
    public void testDeleteSuperPowerById() {
        Address address = new Address();
        address.setStreet("street");
        address.setCity("city");
        address.setState("state");
        address.setCountry("country");
        address.setPostalCode("111111");
        address = addressDao.addAddress(address);

        Location location = new Location();
        location.setName("location");
        location.setDescription("sighting location");
        location.setLatitude(new BigDecimal("55.55555"));
        location.setLongitude(new BigDecimal("66.66666"));
        location.setAddress(address);
        location = locationDao.addLocation(location);

        Location fromDao = locationDao.getLocationById(location.getId());
        assertEquals(location, fromDao);

        locationDao.deleteLocationById(location.getId());
        fromDao = locationDao.getLocationById(location.getId());
        assertNull(fromDao);

    }

    @Test
    public void testgetlocationsBySupercreature() {

        SuperPower superPower = new SuperPower();
        superPower.setName("superpower");
        superPower.setDescription("superpower description");
        superPower = superPowerDao.addSuperPower(superPower);

        SuperCreature superCreature = new SuperCreature();
        superCreature.setName("superman");
        superCreature.setType("super hero");
        superCreature.setDescription("super creature");
        superCreature.setSuperPower(superPower);
        superCreature = superCreatureDao.addSuperCreature(superCreature);

        Address address = new Address();
        address.setStreet("street");
        address.setCity("city");
        address.setState("state");
        address.setCountry("country");
        address.setPostalCode("111111");
        address = addressDao.addAddress(address);

        Location location = new Location();
        location.setName("location");
        location.setDescription("sighting location");
        location.setLatitude(new BigDecimal("55.55555"));
        location.setLongitude(new BigDecimal("66.66666"));
        location.setAddress(address);
        location = locationDao.addLocation(location);

        Sighting sighting = new Sighting();
        sighting.setDate(LocalDate.parse("2021-10-27"));
        sighting.setDescription("sighting");
        sighting.setSuperCreature(superCreature);
        sighting.setLocation(location);
        sighting = sightingDao.addSighting(sighting);

        List<Location> locations = locationDao.getlocationsBySupercreature(superCreature);

        assertEquals(1, locations.size());
        assertTrue(locations.contains(location));

    }
}
