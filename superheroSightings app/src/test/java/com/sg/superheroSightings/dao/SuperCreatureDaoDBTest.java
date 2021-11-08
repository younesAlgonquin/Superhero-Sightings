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
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author pc
 */
@SpringBootTest
public class SuperCreatureDaoDBTest {

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

    public SuperCreatureDaoDBTest() {
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
    public void testAddAndGetSuperCreature() {

        SuperPower superPower = new SuperPower();
        superPower.setName("superpower");
        superPower.setDescription("superpower description");
        superPower = superPowerDao.addSuperPower(superPower);

        Address address = new Address();
        address.setStreet("street");
        address.setCity("city");
        address.setState("state");
        address.setCountry("country");
        address.setPostalCode("111111");
        address = addressDao.addAddress(address);

        Organisation organisation = new Organisation();
        organisation.setName("organisation");
        organisation.setDescription("superCreatures organisation");
        organisation.setPhone("1111111111");
        organisation.setEmail("organisation@organisation.com");
        organisation.setAddress(address);
        organisation = organisationDao.addOrganisation(organisation);
        List<Organisation> organisations = new ArrayList<>();
        organisations.add(organisation);

        SuperCreature superCreature = new SuperCreature();
        superCreature.setName("superman");
        superCreature.setType("super hero");
        superCreature.setDescription("super creature");
        superCreature.setSuperPower(superPower);
        superCreature.setOrganisations(organisations);
        superCreature = superCreatureDao.addSuperCreature(superCreature);

        assertNotNull(superCreature);

        SuperCreature fromDao = superCreatureDao.getSuperCreatureById(superCreature.getId());
        assertNotNull(fromDao.getSuperPower());
        //assertNotNull(fromDao.getOrganisations());
        assertEquals(superCreature, fromDao);

        SuperCreature duplicate = superCreatureDao.addSuperCreature(superCreature);
        assertNull(duplicate);

    }

    @Test
    public void testGetAllSuperCreatures() {

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

        SuperPower superPower2 = new SuperPower();
        superPower2.setName("superpower2");
        superPower2.setDescription("superpower2 description");
        superPower2 = superPowerDao.addSuperPower(superPower2);

        SuperCreature superCreature2 = new SuperCreature();
        superCreature2.setName("superman2");
        superCreature2.setType("super hero2");
        superCreature2.setDescription("super creature2");
        superCreature2.setSuperPower(superPower2);
        superCreature2 = superCreatureDao.addSuperCreature(superCreature2);

        List<SuperCreature> superCreatures = superCreatureDao.getAllSuperCreatures();

        assertEquals(2, superCreatures.size());
        assertTrue(superCreatures.contains(superCreature));
        assertTrue(superCreatures.contains(superCreature2));
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

        SuperCreature fromDao = superCreatureDao.getSuperCreatureById(superCreature.getId());
        assertEquals(superCreature, fromDao);

        superCreature.setName("supermario22");
        superCreatureDao.updateSuperCreature(superCreature);
        assertNotEquals(superCreature, fromDao);

        SuperCreature fromDao2 = superCreatureDao.getSuperCreatureById(superCreature.getId());
        assertEquals(superCreature, fromDao2);
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

        SuperCreature fromDao = superCreatureDao.getSuperCreatureById(superCreature.getId());
        assertEquals(superCreature, fromDao);

        superCreatureDao.deleteSuperCreatureById(superCreature.getId());
        fromDao = superCreatureDao.getSuperCreatureById(superCreature.getId());
        assertNull(fromDao);

    }

    @Test
    public void testgetSuperCreaturesByLocation() {

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

        List<SuperCreature> superCreatures = superCreatureDao.getSuperCreaturesByLocation(location);

        assertEquals(1, superCreatures.size());
        assertTrue(superCreatures.contains(superCreature));

    }

    @Test
    public void testgetOrganisationsBySuperCreature() {

        SuperPower superPower = new SuperPower();
        superPower.setName("superpower");
        superPower.setDescription("superpower description");
        superPower = superPowerDao.addSuperPower(superPower);

        Address address = new Address();
        address.setStreet("street");
        address.setCity("city");
        address.setState("state");
        address.setCountry("country");
        address.setPostalCode("111111");
        address = addressDao.addAddress(address);

        Organisation organisation = new Organisation();
        organisation.setName("organisation");
        organisation.setDescription("superCreatures organisation");
        organisation.setPhone("1111111111");
        organisation.setEmail("organisation@organisation.com");
        organisation.setAddress(address);
        organisation = organisationDao.addOrganisation(organisation);
        List<Organisation> organisations = new ArrayList<>();
        organisations.add(organisation);

        SuperCreature superCreature = new SuperCreature();
        superCreature.setName("superman");
        superCreature.setType("super hero");
        superCreature.setDescription("super creature");
        superCreature.setSuperPower(superPower);
        superCreature.setOrganisations(organisations);
        superCreature = superCreatureDao.addSuperCreature(superCreature);

        List<Organisation> superCreatureOrganisations = superCreatureDao.getOrganisationsBySuperCreature(superCreature);

        assertNotNull(superCreatureOrganisations);
        assertEquals(1, superCreatureOrganisations.size());
        assertTrue(superCreatureOrganisations.contains(organisation));

    }

}
