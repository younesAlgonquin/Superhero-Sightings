package com.sg.superheroSightings.dao;

import com.sg.superheroSightings.dao.AddressDaoDB.AddressMapper;
import com.sg.superheroSightings.dao.SuperCreatureDaoDB.SuperCreatureMapper;
import com.sg.superheroSightings.dto.Address;
import com.sg.superheroSightings.dto.Organisation;
import com.sg.superheroSightings.dto.SuperCreature;
import com.sg.superheroSightings.dto.SuperPower;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Younes Current date: Purpose of the class:
 */
@Repository
public class OrganisationDaoDB implements OrganisationDao {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    @Transactional
    public Organisation getOrganisationById(int id) {

        try {
            final String SELECT_ORGANISATION = "SELECT * FROM organisation WHERE id = ?";
            Organisation organisation
                    = jdbc.queryForObject(SELECT_ORGANISATION, new OrganisationMapper(), id);
            organisation.setAddress(getAddressForOrganisation(id));
            organisation.setSuperCreatures(getSuperCreaturesByOrganisation(organisation));
            return organisation;

        } catch (Exception ex) {

            return null;
        }
    }

    private Address getAddressForOrganisation(int id) {

        try {
            final String SELECT_Address = "SELECT ad.* FROM address ad "
                    + "JOIN organisation o ON ad.id = o.address_id "
                    + "WHERE o.id = ?";
            return jdbc.queryForObject(SELECT_Address, new AddressMapper(), id);

        } catch (Exception ex) {

            return null;
        }

    }

    @Override
    @Transactional
    public List<Organisation> getAllOrganisations() {
        try {
            final String SELECT_ALL_ORGANISATION = "SELECT * FROM organisation";
            List<Organisation> organisations = jdbc.query(SELECT_ALL_ORGANISATION, new OrganisationMapper());
            assignAddressesAndSuperCreaturesToOrganisations(organisations);
            return organisations;

        } catch (Exception ex) {
            return null;

        }
    }

    private void assignAddressesAndSuperCreaturesToOrganisations(List<Organisation> organisations) {

        for (Organisation organisation : organisations) {
            organisation.setAddress(getAddressForOrganisation(organisation.getId()));
            organisation.setSuperCreatures(getSuperCreaturesByOrganisation(organisation));
        }
    }

    @Override
    @Transactional
    public Organisation addOrganisation(Organisation organisation) {
        try {
            List<Organisation> organisations = getAllOrganisations();
            for (Organisation existingOrganisation : organisations) {

                if (existingOrganisation.equals(organisation)) {
                    return null;
                }
            }
        } catch (Exception ex) {
            return null;

        }

        organisation.setAddress(addAddressIfNotExist(organisation.getAddress()));
        try {

            final String INSERT_ORGANISATION = "INSERT INTO organisation"
                    + "(name, description, phone, email, address_id)"
                    + "VALUES (?, ?, ?, ?, ?)";

            jdbc.update(INSERT_ORGANISATION,
                    organisation.getName(),
                    organisation.getDescription(),
                    organisation.getPhone(),
                    organisation.getEmail(),
                    organisation.getAddress().getId()
            );

            int newID = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
            organisation.setId(newID);
            //addSuperCreature_Organisation_entries(organisation);
            return organisation;

        } catch (Exception ex) {
            return null;

        }
    }

//    public void addSuperCreature_Organisation_entries(Organisation organisation) {
//
//        final String INSERT_ENTRIES = " INSERT INTO superCreature_organisation(superCreature_id, organisation_id) "
//                + " VALUES (?, ?) ";
//
//        for (SuperCreature superCreature : organisation.getSuperCreatures()) {
//            jdbc.update(INSERT_ENTRIES,
//                    superCreature.getId(),
//                    organisation.getId());
//        }
//
//    }
    private List<Address> getAllAddresses() {

        final String SELECT_ALL_Addresses = "SELECT * FROM address";
        return jdbc.query(SELECT_ALL_Addresses, new AddressMapper());
    }

    @Transactional
    private Address addAddress(Address address) {

        final String INSERT_ADDRESS = " INSERT INTO address(street, city, state, country, postalCode) "
                + " VALUES(?,?,?,?,?) ";
        jdbc.update(INSERT_ADDRESS,
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getCountry(),
                address.getPostalCode()
        );

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        address.setId(newId);
        return address;
    }

    private Address addAddressIfNotExist(Address address) {

        List<Address> addresses = getAllAddresses();
        boolean exist = false;

        for (Address existingAddress : addresses) {

            if (existingAddress.equals(address)) {

                address = existingAddress;
                exist = true;
                break;
            }
        }
        if (!exist) {

            address = addAddress(address);
        }
        return address;
    }

    @Override
    @Transactional
    public void updateOrganisation(Organisation organisation) {
        try {
            organisation.setAddress(addAddressIfNotExist(organisation.getAddress()));

            final String UPDATE_ORGANISATION = " UPDATE organisation SET "
                    + " name = ?, description = ?, phone = ?, email = ?, address_id = ? "
                    + " WHERE id = ? ";
            jdbc.update(UPDATE_ORGANISATION,
                    organisation.getName(),
                    organisation.getDescription(),
                    organisation.getPhone(),
                    organisation.getEmail(),
                    organisation.getAddress().getId(),
                    organisation.getId()
            );

        } catch (Exception ex) {

        }
    }

    @Override
    @Transactional
    public void deleteOrganisationById(int id) {
        try {
            final String DELETE_SUPERCREATURE_ORGANISATION = "DELETE FROM superCreature_organisation WHERE organisation_id = ?";
            jdbc.update(DELETE_SUPERCREATURE_ORGANISATION, id);
        } catch (Exception ex) {
        }

        try {
            final String DELETE_ORGANISATION = "DELETE FROM organisation WHERE id = ?";
            jdbc.update(DELETE_ORGANISATION, id);

        } catch (Exception ex) {
        }
    }

    @Override
    @Transactional
    public List<SuperCreature> getSuperCreaturesByOrganisation(Organisation organisation) {
        try {
            final String SELECT_SUPERCREATURES = "SELECT sc.* FROM superCreature sc "
                    + "JOIN superCreature_organisation so ON sc.id = so.superCreature_id "
                    + " WHERE so.organisation_id = ?";
            List<SuperCreature> superCreatures
                    = jdbc.query(SELECT_SUPERCREATURES, new SuperCreatureMapper(), organisation.getId());

            if (superCreatures != null) {
                for (SuperCreature superCreature : superCreatures) {
                    superCreature.setSuperPower(getSuperPowerForSuperCreature(superCreature.getId()));
                }
            }

            return superCreatures;
        } catch (Exception ex) {
        }
        return null;
    }
    
        private SuperPower getSuperPowerForSuperCreature(int id) {

        final String SELECT_SUPERPOWER = "SELECT sp.* FROM superPower sp "
                + " JOIN superCreature sc ON sp.id = sc.superPower_id "
                + " WHERE sc.id = ? ";
        return jdbc.queryForObject(SELECT_SUPERPOWER, new SuperPowerDaoDB.SuperPowerMapper(), id);
    }

    public static final class OrganisationMapper implements RowMapper<Organisation> {

        @Override
        public Organisation mapRow(ResultSet rs, int rowNum) throws SQLException {

            Organisation organisation = new Organisation();

            organisation.setId(rs.getInt("id"));
            organisation.setName(rs.getString("name"));
            organisation.setDescription(rs.getString("description"));

            organisation.setPhone(rs.getString("phone"));
            organisation.setEmail(rs.getString("email"));

            return organisation;
        }

    }

}
