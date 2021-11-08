package com.sg.superheroSightings.dao;

import com.sg.superheroSightings.dao.AddressDaoDB.AddressMapper;
import com.sg.superheroSightings.dao.LocationDaoDB.LocationMapper;
import com.sg.superheroSightings.dao.SuperCreatureDaoDB.SuperCreatureMapper;
import com.sg.superheroSightings.dto.Address;
import com.sg.superheroSightings.dto.Location;
import com.sg.superheroSightings.dto.Organisation;
import com.sg.superheroSightings.dto.Sighting;
import com.sg.superheroSightings.dto.SuperCreature;
import com.sg.superheroSightings.dto.SuperPower;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
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
public class SightingDaoDB implements SightingDao {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    @Transactional
    public Sighting getSightingById(int id) {
        try {
            final String SELECT_Sighting = "SELECT * FROM sighting WHERE id = ?";
            Sighting sighting
                    = jdbc.queryForObject(SELECT_Sighting, new SightingMapper(), id);

            if (sighting != null) {
                sighting.setSuperCreature(getSuperCreatureForSighting(sighting));
                sighting.setLocation(getLocationForSighting(sighting));
            }
            return sighting;
        } catch (DataAccessException ex) {

            return null;
        }
    }

    private SuperCreature getSuperCreatureForSighting(Sighting sighting) {

        final String SELECT_SUPERCREATURE = "SELECT sc.* FROM superCreature sc "
                + " JOIN sighting si ON sc.id = si.superCreature_id "
                + " WHERE si.id = ?";
        SuperCreature superCreature
                = jdbc.queryForObject(SELECT_SUPERCREATURE, new SuperCreatureMapper(), sighting.getId());
        if (superCreature != null) {
            superCreature.setSuperPower(getSuperPowerForSuperCreature(superCreature.getId()));
            //superCreature.setOrganisations(getOrganisationsBySuperCreature(superCreature));
        }
        return superCreature;

    }

    private SuperPower getSuperPowerForSuperCreature(int id) {

        final String SELECT_SUPERPOWER = " SELECT sp.* FROM superPower sp "
                + " JOIN superCreature sc ON sp.id = sc.superPower_id "
                + " WHERE sc.id = ? ";
        return jdbc.queryForObject(SELECT_SUPERPOWER, new SuperPowerDaoDB.SuperPowerMapper(), id);
    }

//    private List<Organisation> getOrganisationsBySuperCreature(SuperCreature superCreature) {
//
//        final String SELECT_ORGANISATIONS = "SELECT o.* FROM organisation o"
//                + "JOIN superCreature_organisation so ON o.id = so.organisation_id"
//                + "WHERE superCreature_id = ?";
//
//        List<Organisation> organisations = jdbc.query(SELECT_ORGANISATIONS, new OrganisationDaoDB.OrganisationMapper(), superCreature.getId());
//
//        for (Organisation organisation : organisations) {
//
//            organisation.setAddress(getAddressForOrganisation(organisation.getId()));
//        }
//        return organisations;
//    }
//    private Address getAddressForOrganisation(int id) {
//
//        final String SELECT_Address = "SELECT ad.* FROM address ad"
//                + "JOIN organisation o ON ad.id = o.address_id"
//                + "WHERE o.id = ?";
//        return jdbc.queryForObject(SELECT_Address, new AddressMapper(), id);
//    }
    private Location getLocationForSighting(Sighting sighting) {

        final String SELECT_Location = " SELECT lo.* FROM location lo "
                + " JOIN sighting si ON lo.id = si.location_id "
                + " WHERE si.id = ? ";
        Location location
                = jdbc.queryForObject(SELECT_Location, new LocationMapper(), sighting.getId());
        if (location != null) {
            location.setAddress(getAddressForLocation(location.getId()));
        }
        return location;

    }

    private Address getAddressForLocation(int id) {

        final String SELECT_Address = " SELECT ad.* FROM address ad "
                + " JOIN location lo ON ad.id = lo.address_id "
                + " WHERE lo.id = ? ";
        return jdbc.queryForObject(SELECT_Address, new AddressMapper(), id);
    }

    @Override
    @Transactional
    public List<Sighting> getAllSighting() {
        try {
            final String SELECT_ALL_SIGHTINGS = "SELECT * FROM sighting";
            List<Sighting> sightings
                    = jdbc.query(SELECT_ALL_SIGHTINGS, new SightingMapper());

            if (sightings != null) {
                for (Sighting sighting : sightings) {

                    sighting.setSuperCreature(getSuperCreatureForSighting(sighting));
                    sighting.setLocation(getLocationForSighting(sighting));
                }
            }

            return sightings;

        } catch (Exception ex) {
            return null;

        }
    }

    @Override
    @Transactional
    public Sighting addSighting(Sighting sighting) {

        try {
            List<Sighting> sightings = getAllSighting();

            if (sightings != null) {
                for (Sighting existingSighting : sightings) {
                    if (existingSighting.equals(sighting)) {
                        return null;
                    }
                }
            }

        } catch (Exception ex) {
            return null;
        }

        Location location = addLocationIfNotExist(sighting.getLocation());
        sighting.setLocation(location);

        try {
            final String INSERT_SIGHTING
                    = " INSERT INTO sighting(date, description, superCreature_id, location_id) "
                    + " VALUES(?,?, ?, ?)";
            jdbc.update(INSERT_SIGHTING,
                    java.sql.Date.valueOf(sighting.getDate()),
                    sighting.getDescription(),
                    sighting.getSuperCreature().getId(),
                    sighting.getLocation().getId()
            );

            int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
            sighting.setId(newId);
            return sighting;

        } catch (Exception ex) {
            return null;

        }
    }

    private Location addLocationIfNotExist(Location loocation) {

        boolean exist = false;
        try {
            List<Location> locations = getAllLocations();
            if (locations != null) {
                for (Location existinglocation : locations) {

                    if (existinglocation.equals(loocation)){

                        loocation = existinglocation;
                        exist = true;
                        break;
                    }
                }
            }
            
            
        } catch (Exception ex) {
            return null;

        }

        if (exist) {
            return loocation;
        } else {

            try {

                Address address = addAddressIfNotExist(loocation.getAddress());
                loocation.setAddress(address);

                final String INSERT_LOCATION = "INSERT INTO location "
                        + " (name, description, latitude, longitude, address_id) "
                        + " VALUES (?, ?, ?, ?, ?)";

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
    }

    private List<Location> getAllLocations() {

        try {
            final String SELECT_ALL_LOCATIONS = " SELECT * FROM location ";
            List<Location> locations
                    = jdbc.query(SELECT_ALL_LOCATIONS, new LocationMapper());
            return locations;
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
            final String INSERT_ADDRESS = " INSERT INTO address(street, city, state, country, postalCode) "
                    + " VALUES(?,?,?,?,?)";
            jdbc.update(INSERT_ADDRESS,
                    address.getStreet(),
                    address.getCity(),
                    address.getState(),
                    address.getCountry(),
                    address.getPostalCode()
            );

            int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class
            );
            address.setId(newId);
            return address;
        } catch (Exception ex) {

            return null;
        }
    }

    private Address addAddressIfNotExist(Address address) {

        List<Address> addresses = getAllAddresses();
        boolean exist = false;
        if (addresses != null) {
            for (Address existingAddress : addresses) {

                if (existingAddress.equals(address)) {

                    address = existingAddress;
                    exist = true;
                    break;
                }
            }
        }
        if (!exist) {

            address = addAddress(address);
        }
        return address;
    }

    @Override
    public void updateSighting(Sighting sighting) {
        try {
            Location location = addLocationIfNotExist(sighting.getLocation());
            sighting.setLocation(location);

            final String UPADATE_SIGHTING = " UPDATE sighting SET "
                    + " date = ?, description = ?, superCreature_id = ?, location_id = ? "
                    + " WHERE id = ? ";
            jdbc.update(UPADATE_SIGHTING,
                    java.sql.Date.valueOf(sighting.getDate()),
                    sighting.getDescription(),
                    sighting.getSuperCreature().getId(),
                    sighting.getLocation().getId(),
                    sighting.getId()
            );
        } catch (Exception ex) {

        }
    }

    @Override
    public void deleteSightingById(int id) {
        try {
            final String DELETE_SIGHTING = "DELETE FROM sighting WHERE id = ?";
            jdbc.update(DELETE_SIGHTING, id);

        } catch (Exception ex) {

        }
    }

    @Override
    public List<Sighting> getSightingsByDate(LocalDate date) {

        try {
            final String SELECT_SIGHTINGS = "SELECT * FROM sighting WHERE date = ?";
            List<Sighting> sightings
                    = jdbc.query(SELECT_SIGHTINGS, new SightingMapper(), java.sql.Date.valueOf(date));

            if (sightings != null) {
                for (Sighting sighting : sightings) {

                    sighting.setSuperCreature(getSuperCreatureForSighting(sighting));
                    sighting.setLocation(getLocationForSighting(sighting));

                }
            }
            return sightings;
        } catch (Exception ex) {
            return null;

        }

    }

    public static final class SightingMapper implements RowMapper<Sighting> {

        @Override
        public Sighting mapRow(ResultSet rs, int rowNum) throws SQLException {

            Sighting sighting = new Sighting();

            sighting.setId(rs.getInt("id"));
            sighting.setDate(rs.getDate("date").toLocalDate());
            sighting.setDescription(rs.getString("description"));

            return sighting;
        }

    }

}
