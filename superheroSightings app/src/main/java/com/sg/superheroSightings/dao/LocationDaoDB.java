package com.sg.superheroSightings.dao;

import com.sg.superheroSightings.dao.AddressDaoDB.AddressMapper;
import com.sg.superheroSightings.dto.Address;
import com.sg.superheroSightings.dto.Location;
import com.sg.superheroSightings.dto.SuperCreature;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class LocationDaoDB implements LocationDao {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    @Transactional
    public Location getLocationById(int id) {
        try {
            final String SELECT_LOCATION_BY_ID = "SELECT * FROM location WHERE id = ?";
            Location location
                    = jdbc.queryForObject(SELECT_LOCATION_BY_ID, new LocationMapper(), id);

            if (location != null) {
                location.setAddress(getAddressForLocation(id));
            }
            return location;
        } catch (Exception ex) {

            return null;
        }

    }

    private Address getAddressForLocation(int id) {
        try {
            final String SELECT_ADDRESS = "SELECT ad.* FROM address ad "
                    + " JOIN location lo ON ad.id = lo.address_id "
                    + " WHERE lo.id = ?";
            return jdbc.queryForObject(SELECT_ADDRESS, new AddressMapper(), id);

        } catch (Exception ex) {
            return null;
        }

    }

    @Override
    @Transactional
    public List<Location> getAllLocations() {

        try {
            final String SELECT_ALL_LOCATIONS = "SELECT * FROM location";
            List<Location> locations = jdbc.query(SELECT_ALL_LOCATIONS, new LocationMapper());

            if (locations != null) {
                assignAddressesToLocations(locations);
            }
            return locations;

        } catch (Exception ex) {
            return null;

        }
    }

    private void assignAddressesToLocations(List<Location> locations) {

        for (Location location : locations) {
            location.setAddress(getAddressForLocation(location.getId()));
        }
    }

    @Override
    @Transactional
    public Location addLocation(Location loocation) {
        try {
            List<Location> locations = getAllLocations();

            if (locations != null) {
                for (Location existinglocation : locations) {

                    if (existinglocation.equals(loocation)) {
                        return null;
                    }
                }
            }

            loocation.setAddress(addAddressIfNotExist(loocation.getAddress()));
            final String INSERT_LOCATION = "INSERT INTO location"
                    + "(name, description, latitude, longitude, address_id) "
                    + "VALUES (?, ?, ?, ?, ?)";

            jdbc.update(INSERT_LOCATION,
                    loocation.getName(),
                    loocation.getDescription(),
                    loocation.getLatitude(),
                    loocation.getLongitude(),
                    loocation.getAddress().getId()
            );

            int newID = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
            loocation.setId(newID);
            return loocation;
        } catch (Exception ex) {
            return null;

        }
    }

    private List<Address> getAllAddresses() {

        try {
            final String SELECT_ALL_Addresses = "SELECT * FROM address";
            return jdbc.query(SELECT_ALL_Addresses, new AddressMapper());

        } catch (Exception ex) {
            return null;
        }
    }

    private Address addAddress(Address address) {

        try {

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

    private Address addAddressIfNotExist(Address address) {

        try {
            List<Address> addresses = getAllAddresses();
            boolean exist = false;
            if (addresses != null) {
                for (Address existingAddress : addresses) {

                    if (existingAddress.equals(address)) {

                        address=existingAddress;
                        exist = true;
                        break;
                    }
                }
            }
            if (!exist) {

                address = addAddress(address);
            }
            return address;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    @Transactional
    public void updateLocation(Location location) {
        try {
            location.setAddress(addAddressIfNotExist(location.getAddress()));
        } catch (Exception ex) {

        }

        try {
            final String UPDATE_LOCATION = "UPDATE location SET "
                    + " name = ?, description = ?, latitude = ?, longitude = ?, address_id = ?"
                    + " WHERE id = ?";
            jdbc.update(UPDATE_LOCATION,
                    location.getName(),
                    location.getDescription(),
                    location.getLatitude(),
                    location.getLongitude(),
                    location.getAddress().getId(),
                    location.getId()
            );

        } catch (Exception ex) {

        }
    }

    @Override
    @Transactional
    public void deleteLocationById(int id) {
        try {
            final String DELETE_SIGHTING = "DELETE FROM sighting WHERE location_id = ?";
            jdbc.update(DELETE_SIGHTING, id);
        } catch (Exception ex) {
        }

        try {
            final String DELETE_LOCATION = "DELETE FROM location WHERE id = ?";
            jdbc.update(DELETE_LOCATION, id);

        } catch (Exception ex) {
        }

    }

    @Override
    @Transactional
    public List<Location> getlocationsBySupercreature(SuperCreature superCreature) {

        try {
            final String SELECT_LOCATIONS = "SELECT lo.* FROM "
                    + " location lo JOIN sighting si ON lo.id = si.location_id "
                    + " WHERE si.superCreature_id = ? ";
            List<Location> locations
                    = jdbc.query(SELECT_LOCATIONS, new LocationMapper(), superCreature.getId());

            if (locations != null) {
                assignAddressesToLocations(locations);
            }
            return locations;

        } catch (Exception ex) {
            return null;

        }
    }

    public static final class LocationMapper implements RowMapper<Location> {

        @Override
        public Location mapRow(ResultSet rs, int rowNum) throws SQLException {

            Location location = new Location();

            location.setId(rs.getInt("id"));
            location.setName(rs.getString("name"));
            location.setDescription(rs.getString("description"));

            location.setLatitude(rs.getBigDecimal("latitude"));
            location.setLongitude(rs.getBigDecimal("longitude"));

            return location;
        }

    }

}
