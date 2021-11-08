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
public class SightingDaoDBTest {

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

    public SightingDaoDBTest() {
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
    public void testAddAndGetSighting() {

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

        assertNotNull(sighting);

        Sighting fromDao = sightingDao.getSightingById(sighting.getId());

        assertNotNull(fromDao.getSuperCreature());
        assertNotNull(fromDao.getSuperCreature().getSuperPower());
        assertNotNull(fromDao.getLocation());
        assertNotNull(fromDao.getLocation().getAddress());

        assertEquals(sighting, fromDao);

        Sighting duplicate = sightingDao.addSighting(sighting);
        assertNull(duplicate);

        //Add sighting with a new location
        Address address2 = new Address();
        address2.setStreet("street2");
        address2.setCity("city2");
        address2.setState("state2");
        address2.setCountry("country2");
        address2.setPostalCode("222222");

        Location location2 = new Location();
        location2.setName("location2");
        location2.setDescription("sighting location2");
        location2.setLatitude(new BigDecimal("11.11111"));
        location2.setLongitude(new BigDecimal("22.22222"));
        location2.setAddress(address2);

        Sighting sighting2 = new Sighting();
        sighting2.setDate(LocalDate.parse("2021-10-27"));
        sighting2.setDescription("sighting");
        sighting2.setSuperCreature(superCreature);
        sighting2.setLocation(location2);
        sighting = sightingDao.addSighting(sighting2);

        Sighting fromDao3 = sightingDao.getSightingById(sighting.getId());
        assertEquals(sighting2, fromDao3);
    }

    @Test
    public void testGetAllSightings() {

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

        SuperPower superPower2 = new SuperPower();
        superPower2.setName("superpower2");
        superPower2.setDescription("superpower description2");
        superPower2 = superPowerDao.addSuperPower(superPower2);

        SuperCreature superCreature2 = new SuperCreature();
        superCreature2.setName("superman2");
        superCreature2.setType("super hero2");
        superCreature2.setDescription("super creature2");
        superCreature2.setSuperPower(superPower2);
        superCreature2 = superCreatureDao.addSuperCreature(superCreature2);

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
        location2.setLatitude(new BigDecimal("44.44444"));
        location2.setLongitude(new BigDecimal("99.99999"));
        location2.setAddress(address2);
        location2 = locationDao.addLocation(location2);

        Sighting sighting2 = new Sighting();
        sighting2.setDate(LocalDate.parse("2021-12-11"));
        sighting2.setDescription("sighting2");
        sighting2.setSuperCreature(superCreature2);
        sighting2.setLocation(location2);
        sighting2 = sightingDao.addSighting(sighting2);

        List<Sighting> sightings = sightingDao.getAllSighting();

        assertEquals(2, sightings.size());
        assertTrue(sightings.contains(sighting));
        assertTrue(sightings.contains(sighting2));
    }

    @Test
    public void testUpdateSuperPower() {

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

        Sighting fromDao = sightingDao.getSightingById(sighting.getId());
        assertEquals(sighting, fromDao);

        sighting.setDescription("updated sightng");
        sightingDao.updateSighting(sighting);
        assertNotEquals(sighting, fromDao);

        Sighting fromDao2 = sightingDao.getSightingById(sighting.getId());
        assertEquals(sighting, fromDao2);
    }

    @Test
    public void testDeleteSuperCreatureById() {

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

        Sighting fromDao = sightingDao.getSightingById(sighting.getId());
        assertEquals(sighting, fromDao);

        sightingDao.deleteSightingById(sighting.getId());
        fromDao = sightingDao.getSightingById(sighting.getId());
        assertNull(fromDao);

    }
    
    
    
    @Test
    public void testgetSightingsByDate() {

        LocalDate date = LocalDate.parse("2021-10-27");
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


        List<Sighting> sightings = sightingDao.getSightingsByDate(date);
        
        assertEquals(1, sightings.size());
        assertTrue(sightings.contains(sighting));

    }

}
