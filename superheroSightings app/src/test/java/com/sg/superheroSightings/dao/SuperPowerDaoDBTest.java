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
public class SuperPowerDaoDBTest {

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

    public SuperPowerDaoDBTest() {
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
    public void testAddAndGetSuperPower() {
        SuperPower superPower = new SuperPower();
        superPower.setName("super");
        superPower.setDescription("superpower");
        superPower = superPowerDao.addSuperPower(superPower);

        SuperPower fromDao = superPowerDao.getSuperPowerByID(superPower.getId());
        

        assertNotNull(superPower);
        assertEquals(superPower, fromDao);
        
        SuperPower duplicate = superPowerDao.addSuperPower(superPower);
        assertNull(duplicate);
    }

    @Test
    public void testGetAllAddresses() {
        SuperPower superPower = new SuperPower();
        superPower.setName("super");
        superPower.setDescription("superpower");
        superPower = superPowerDao.addSuperPower(superPower);

        SuperPower superPower2 = new SuperPower();
        superPower2.setName("super2");
        superPower2.setDescription("superpower2");
        superPower2 = superPowerDao.addSuperPower(superPower2);

        List<SuperPower> superPoweres = superPowerDao.getAllSuperPowers();

        assertEquals(2, superPoweres.size());
        assertTrue(superPoweres.contains(superPower));
        assertTrue(superPoweres.contains(superPower2));
    }

    @Test
    public void testUpdateSuperPower() {
        SuperPower superPower = new SuperPower();
        superPower.setName("super");
        superPower.setDescription("superpower");
        superPower = superPowerDao.addSuperPower(superPower);

        SuperPower fromDao = superPowerDao.getSuperPowerByID(superPower.getId());
        assertEquals(superPower, fromDao);

        superPower.setName("superman");
        superPowerDao.updateSuperPower(superPower);
        assertNotEquals(superPower, fromDao);

        SuperPower fromDao2 = superPowerDao.getSuperPowerByID(superPower.getId());
        assertEquals(superPower, fromDao2);
    }

    @Test
    public void testDeleteSuperPowerById() {
        SuperPower superPower = new SuperPower();
        superPower.setName("super");
        superPower.setDescription("superpower");
        superPower = superPowerDao.addSuperPower(superPower);

        SuperPower fromDao = superPowerDao.getSuperPowerByID(superPower.getId());
        assertEquals(superPower, fromDao);

        superPowerDao.deleteSuperPowerById(superPower.getId());
        fromDao = superPowerDao.getSuperPowerByID(superPower.getId());
        assertNull(fromDao);

    }
}
