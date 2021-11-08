package com.sg.superheroSightings.dao;

import com.sg.superheroSightings.dao.OrganisationDaoDB.OrganisationMapper;
import com.sg.superheroSightings.dao.SuperPowerDaoDB.SuperPowerMapper;
import com.sg.superheroSightings.dto.Address;
import com.sg.superheroSightings.dto.Location;
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
public class SuperCreatureDaoDB implements SuperCreatureDao {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    @Transactional
    public SuperCreature getSuperCreatureById(int id) {

        try {
            final String SELECT_SUPERCREATURE = "SELECT * FROM superCreature WHERE id = ?";
            SuperCreature superCreature
                    = jdbc.queryForObject(SELECT_SUPERCREATURE, new SuperCreatureMapper(), id);

            if (superCreature != null) {
                superCreature.setSuperPower(getSuperPowerForSuperCreature(id));
                superCreature.setOrganisations(getOrganisationsBySuperCreature(superCreature));
            }
            return superCreature;
        } catch (DataAccessException ex) {

            return null;
        }
    }

    private SuperPower getSuperPowerForSuperCreature(int id) {

        final String SELECT_SUPERPOWER = "SELECT sp.* FROM superPower sp "
                + " JOIN superCreature sc ON sp.id = sc.superPower_id "
                + " WHERE sc.id = ? ";
        return jdbc.queryForObject(SELECT_SUPERPOWER, new SuperPowerMapper(), id);
    }

    @Override
    @Transactional
    public List<SuperCreature> getAllSuperCreatures() {
        try {
            final String SELECT_ALL_SUPERCREATURES = "SELECT * FROM superCreature";
            List<SuperCreature> superCreatures
                    = jdbc.query(SELECT_ALL_SUPERCREATURES, new SuperCreatureMapper());

            if (superCreatures != null) {
                assignsuperPowersTosuperCreatures(superCreatures);
            }

            return superCreatures;

        } catch (Exception ex) {
            return null;

        }
    }

    private void assignsuperPowersTosuperCreatures(List<SuperCreature> superCreatures) {

        for (SuperCreature superCreature : superCreatures) {
            superCreature.setSuperPower(getSuperPowerForSuperCreature(superCreature.getId()));
            superCreature.setOrganisations(getOrganisationsBySuperCreature(superCreature));
        }
    }

    @Override
    @Transactional
    public SuperCreature addSuperCreature(SuperCreature superCreature) {
        try {
            List<SuperCreature> superCreatures = getAllSuperCreatures();

            if (superCreatures != null) {
                for (SuperCreature existingSuperCreature : superCreatures) {

                    if (existingSuperCreature.equals(superCreature)) {
                        return null;
                    }
                }
            }
        } catch (Exception ex) {
            return null;
        }

        superCreature.setSuperPower(addSuperPowerIfNotExist(superCreature.getSuperPower()));
        try {
            final String INSERT_SUPERCREATURE = "INSERT INTO superCreature(name, type, description, superPower_id) "
                    + "VALUES(?, ?, ?, ?)";
            jdbc.update(INSERT_SUPERCREATURE,
                    superCreature.getName(),
                    superCreature.getType(),
                    superCreature.getDescription(),
                    superCreature.getSuperPower().getId()
            );

            int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
            superCreature.setId(newId);
            addSuperCreature_Organisation_entries(superCreature);
            return superCreature;

        } catch (Exception ex) {
            return null;
        }
    }

    private void addSuperCreature_Organisation_entries(SuperCreature superCreature) {

        final String INSERT_ENTRIES = "INSERT INTO superCreature_organisation (superCreature_id, organisation_id) "
                + " Values (?, ?)";

        List<Organisation> organisations = superCreature.getOrganisations();

        if (organisations != null) {
            for (Organisation organisation : superCreature.getOrganisations()) {
                jdbc.update(INSERT_ENTRIES,
                        superCreature.getId(),
                        organisation.getId());
            }
        }

    }

    private List<SuperPower> getAllSuperPowers() {

        final String SELECT_ALL_SUPERPOWERS = "SELECT * FROM superPower";
        return jdbc.query(SELECT_ALL_SUPERPOWERS, new SuperPowerMapper());
    }

    private SuperPower addSuperPower(SuperPower superPower) {

        final String INSERT_SUPERPOWER = "INSERT INTO superPower(name, description) "
                + " VALUES(?,?)";
        jdbc.update(INSERT_SUPERPOWER,
                superPower.getName(),
                superPower.getDescription());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        superPower.setId(newId);
        return superPower;
    }

    private SuperPower addSuperPowerIfNotExist(SuperPower superPower) {

        List<SuperPower> superPowers = getAllSuperPowers();
        boolean exist = false;
        if (superPowers != null) {
            for (SuperPower existingSuperPower : superPowers) {

                if (existingSuperPower.equals(superPower)) {

                    superPower = existingSuperPower;
                    exist = true;
                    break;
                }
            }
        }
        if (!exist) {

            superPower = addSuperPower(superPower);
        }
        return superPower;
    }

    @Override
    @Transactional
    public void updateSuperCreature(SuperCreature superCreature) {

        try {
            superCreature.setSuperPower(addSuperPowerIfNotExist(superCreature.getSuperPower()));
            deleteSuperCreature_Organisation_entries(superCreature);

            final String UPADATE_SUPERCREATURE = " UPDATE superCreature SET "
                    + " name =?, type =?, description =?, superPower_id =? "
                    + " WHERE id= ?";
            jdbc.update(UPADATE_SUPERCREATURE,
                    superCreature.getName(),
                    superCreature.getType(),
                    superCreature.getDescription(),
                    superCreature.getSuperPower().getId(),
                    superCreature.getId()
            );
            addSuperCreature_Organisation_entries(superCreature);

        } catch (Exception ex) {

        }

    }

    private void deleteSuperCreature_Organisation_entries(SuperCreature superCreature) {
        try {
            final String DELETE_ENTRIES = " DELETE FROM superCreature_organisation WHERE superCreature_id = ? ";
            jdbc.update(DELETE_ENTRIES,
                    superCreature.getId()
            );
        } catch (Exception ex) {
        }
    }

    @Override
    @Transactional
    public void deleteSuperCreatureById(int id) {

        try {
            final String DELETE_SIGHTING = "DELETE FROM sighting WHERE superCreature_id = ?";
            jdbc.update(DELETE_SIGHTING, id);
        } catch (Exception ex) {
        }

        try {
            final String DELETE_SUPERCREATURE_ORGANISATION = "DELETE FROM superCreature_organisation WHERE superCreature_id = ? ";
            jdbc.update(DELETE_SUPERCREATURE_ORGANISATION, id);
        } catch (Exception ex) {
        }

        try {
            final String DELETE_SUPERCREATURE = "DELETE FROM superCreature WHERE id = ?";
            jdbc.update(DELETE_SUPERCREATURE, id);

        } catch (Exception ex) {
        }
    }

    @Override
    @Transactional
    public List<SuperCreature> getSuperCreaturesByLocation(Location location) {

        try {
            final String SELECT_SUPERCREATURES_LOCATION = "SELECT sc.* FROM superCreature sc "
                    + " JOIN sighting si ON sc.id = si.superCreature_id "
                    + " WHERE location_id = ?";

            List<SuperCreature> superCreatures
                    = jdbc.query(SELECT_SUPERCREATURES_LOCATION, new SuperCreatureMapper(), location.getId());
            if (superCreatures != null) {
                assignsuperPowersTosuperCreatures(superCreatures);
            }
            return superCreatures;

        } catch (Exception ex) {
            return null;

        }
    }

    @Override
    @Transactional
    public List<Organisation> getOrganisationsBySuperCreature(SuperCreature superCreature) {

        try {
            final String SELECT_ORGANISATIONS = " SELECT o.* FROM organisation o "
                    + " JOIN superCreature_organisation so ON o.id = so.organisation_id "
                    + " WHERE superCreature_id = ? ";

            List<Organisation> organisations
                    = jdbc.query(SELECT_ORGANISATIONS, new OrganisationMapper(), superCreature.getId());

            if (organisations != null) {
                for (Organisation organisation : organisations) {

                    organisation.setAddress(getAddressForOrganisation(organisation.getId()));
                }
            }
            return organisations;

        } catch (Exception ex) {
            return null;

        }
    }

    private Address getAddressForOrganisation(int id) {

        final String SELECT_Address = " SELECT ad.* FROM address ad "
                + " JOIN organisation o ON ad.id = o.address_id "
                + " WHERE o.id = ? ";
        return jdbc.queryForObject(SELECT_Address, new AddressDaoDB.AddressMapper(), id);

    }

    public static final class SuperCreatureMapper implements RowMapper<SuperCreature> {

        @Override
        public SuperCreature mapRow(ResultSet rs, int rowNum) throws SQLException {

            SuperCreature superCreature = new SuperCreature();

            superCreature.setId(rs.getInt("id"));
            superCreature.setName(rs.getString("name"));
            superCreature.setType(rs.getString("type"));
            superCreature.setDescription(rs.getString("description"));

            return superCreature;
        }

    }

}
