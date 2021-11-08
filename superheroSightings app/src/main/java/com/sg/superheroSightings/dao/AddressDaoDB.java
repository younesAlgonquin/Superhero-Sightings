package com.sg.superheroSightings.dao;

import com.sg.superheroSightings.dto.Address;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Younes Current date: Purpose of the class:
 */
@Repository
public class AddressDaoDB implements AddressDao {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public Address getAddressById(int id) {
        try {
            final String SELECT_ADDRESS_BY_ID = "SELECT * FROM address WHERE id = ?";
            return jdbc.queryForObject(SELECT_ADDRESS_BY_ID, new AddressMapper(), id);

        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public List<Address> getAllAddresses() {
        try {
            final String SELECT_ALL_Addresses = "SELECT * FROM address";
            return jdbc.query(SELECT_ALL_Addresses, new AddressMapper());

        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    @Transactional
    public Address addAddress(Address address) {

        try {
            List<Address> addresses = getAllAddresses();
            for (Address existingaddress : addresses) {

                if (existingaddress.equals(address)) {
                    return null;
                }
            }
            final String INSERT_ADDRESS = "INSERT INTO address(street, city, state, country, postalCode) "
                    + "VALUES(?,?,?,?,?)";
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

        } catch (Exception ex) {
            return null;

        }
    }

    @Override
    public void updateAddress(Address address) {
        try {
            final String UPDATE_ADDRESS = "UPDATE address "
                    + "SET street =?, city =?, state =?, country =?, postalCode =?"
                    + "WHERE id = ?";

            jdbc.update(UPDATE_ADDRESS,
                    address.getStreet(),
                    address.getCity(),
                    address.getState(),
                    address.getCountry(),
                    address.getPostalCode(),
                    address.getId()
            );

        } catch (Exception ex) {
        }
    }

    @Override
    @Transactional
    public void deleteAddressById(int id) {

//        try {
//            final String DELETE_SUPERCREATURE_ORGANISATION = "DELETE so.* "
//                    + "FROM superCreature_organisation so JOIN organisation o "
//                    + "ON so.organisation_id = o.id"
//                    + "WHERE o.address_id = ? ";
//            jdbc.update(DELETE_SUPERCREATURE_ORGANISATION, id);
//        } catch (Exception ex) {
//        }
//
//        try {
//            final String DELETE_ORGANISATION = "DELETE FROM organisation WHERE address_id = ?";
//            jdbc.update(DELETE_ORGANISATION, id);
//        } catch (Exception ex) {
//        }
//
//        try {
//            final String DELETE_SIGHTING = "DELETE si.* "
//                    + "FROM sighting si JOIN location lo "
//                    + "ON si.location_id = lo.id"
//                    + "WHERE lo.address_id = ? ";
//            jdbc.update(DELETE_SIGHTING, id);
//        } catch (Exception ex) {
//        }
//
//        try {
//            final String DELETE_Location = "DELETE FROM location WHERE address_id = ?";
//            jdbc.update(DELETE_Location, id);
//        } catch (Exception ex) {
//        }

        try {
            final String DELETE_Address = "DELETE FROM address WHERE id = ?";
            jdbc.update(DELETE_Address, id);

        } catch (Exception ex) {
        }
    }

    public static final class AddressMapper implements RowMapper<Address> {

        @Override
        public Address mapRow(ResultSet rs, int rowNum) throws SQLException {

            Address address = new Address();
            address.setId(rs.getInt("id"));
            address.setStreet(rs.getString("street"));
            address.setCity(rs.getString("city"));
            address.setState(rs.getString("state"));
            address.setCountry(rs.getString("country"));
            address.setPostalCode(rs.getString("postalCode"));

            return address;

        }

    }//end SuperPowerMapper class

}
