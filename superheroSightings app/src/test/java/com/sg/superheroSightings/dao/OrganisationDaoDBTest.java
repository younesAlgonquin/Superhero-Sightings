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
public class OrganisationDaoDBTest {

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

    public OrganisationDaoDBTest() {
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
    public void testAddAndGetOrganisation() {

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

        assertNotNull(organisation);

        Organisation fromDao = organisationDao.getOrganisationById(organisation.getId());
        assertNotNull(fromDao.getAddress());
        assertEquals(organisation, fromDao);

        //assertEquals(fromDao.getId(), 25);
        Organisation duplicate = organisationDao.addOrganisation(organisation);
        assertNull(duplicate);

    }

    @Test
    public void testGetAllorganisations() {

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

        Address address2 = new Address();
        address2.setStreet("street2");
        address2.setCity("city2");
        address2.setState("state2");
        address2.setCountry("country2");
        address2.setPostalCode("222222");
        address2 = addressDao.addAddress(address2);

        Organisation organisation2 = new Organisation();
        organisation2.setName("organisation2");
        organisation2.setDescription("superCreatures organisation2");
        organisation2.setPhone("2222222222");
        organisation2.setEmail("organisation2@organisation2.com");
        organisation2.setAddress(address2);
        organisation2 = organisationDao.addOrganisation(organisation2);

        List<Organisation> organisations = organisationDao.getAllOrganisations();

        assertEquals(2, organisations.size());
        assertTrue(organisations.contains(organisation));
        assertTrue(organisations.contains(organisation2));
    }

    @Test
    public void testUpdateOrganisation() {

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

        Organisation fromDao = organisationDao.getOrganisationById(organisation.getId());
        assertEquals(organisation, fromDao);

        organisation.setName("organisation2");
        organisationDao.updateOrganisation(organisation);
        assertNotEquals(organisation, fromDao);

        Organisation fromDao2 = organisationDao.getOrganisationById(organisation.getId());
        assertEquals(organisation, fromDao2);
    }

    @Test
    public void testOrganisationById() {

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

        Organisation fromDao = organisationDao.getOrganisationById(organisation.getId());
        assertEquals(organisation, fromDao);

        organisationDao.deleteOrganisationById(organisation.getId());
        fromDao = organisationDao.getOrganisationById(organisation.getId());
        assertNull(fromDao);

    }

    @Test
    public void testgetSuperCreaturesByOrganisation() {

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

        List<SuperCreature> supercreaturesInTheOrganisation = 
                organisationDao.getSuperCreaturesByOrganisation(organisation);

        assertNotNull(supercreaturesInTheOrganisation);
        assertEquals(1, supercreaturesInTheOrganisation.size());
        assertTrue(supercreaturesInTheOrganisation.contains(superCreature));

    }
}
