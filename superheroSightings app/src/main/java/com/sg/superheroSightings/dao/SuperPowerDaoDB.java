package com.sg.superheroSightings.dao;

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
public class SuperPowerDaoDB implements SuperPowerDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public SuperPower getSuperPowerByID(int id) {
        try {
            final String SELECT_SUPERPOWER_BY_ID = "SELECT * FROM superPower WHERE id = ?";
            return jdbc.queryForObject(SELECT_SUPERPOWER_BY_ID, new SuperPowerMapper(), id);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<SuperPower> getAllSuperPowers() {
        try {
            final String SELECT_ALL_SUPERPOWERS = "SELECT * FROM superPower";
            return jdbc.query(SELECT_ALL_SUPERPOWERS, new SuperPowerMapper());

        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    @Transactional
    public SuperPower addSuperPower(SuperPower superPower) {

        try {
            List<SuperPower> superPowers = getAllSuperPowers();

            if (superPowers != null) {
                for (SuperPower existingSuperPower : superPowers) {

                    if (existingSuperPower.equals(superPower)) {
                        return null;
                    }
                }
            }

            final String INSERT_SUPERPOWER = "INSERT INTO superPower(name, description) "
                    + "VALUES(?,?)";
            jdbc.update(INSERT_SUPERPOWER,
                    superPower.getName(),
                    superPower.getDescription());

            int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
            superPower.setId(newId);
            return superPower;

        } catch (Exception ex) {
            return null;
        }

    }

    @Override
    public void updateSuperPower(SuperPower superPower) {

        try {
            final String UPDATE_SUPERPOWER = "UPDATE superPower SET name = ?, description = ?"
                    + "WHERE id = ?";

            jdbc.update(UPDATE_SUPERPOWER,
                    superPower.getName(),
                    superPower.getDescription(),
                    superPower.getId());

        } catch (Exception ex) {
        }

    }

    @Override
    @Transactional
    public void deleteSuperPowerById(int id) {
        try {
            final String DELETE_SUPERCREATURE_ORGANISATION = " DELETE so. * "
                    + " FROM superCreature_organisation so JOIN superCreature sc "
                    + " ON so.superCreature_id = sc.id "
                    + " WHERE sc.superPower_id = ? ";
            jdbc.update(DELETE_SUPERCREATURE_ORGANISATION, id);
        } catch (Exception ex) {
        }

        try {
            final String DELETE_SUPERCREATURE = " DELETE FROM superCreature WHERE superPower_id = ?";
            jdbc.update(DELETE_SUPERCREATURE, id);
        } catch (Exception ex) {
        }

        try {
            final String DELETE_SUPERPOWER = "DELETE FROM superPower WHERE id = ?";
            jdbc.update(DELETE_SUPERPOWER, id);

        } catch (Exception ex) {
        }

    }

    public static final class SuperPowerMapper implements RowMapper<SuperPower> {

        @Override
        public SuperPower mapRow(ResultSet rs, int rowNum) throws SQLException {

            SuperPower superPower = new SuperPower();
            superPower.setId(rs.getInt("id"));
            superPower.setName(rs.getString("name"));
            superPower.setDescription(rs.getString("description"));

            return superPower;

        }

    }//end SuperPowerMapper class

}//end class
