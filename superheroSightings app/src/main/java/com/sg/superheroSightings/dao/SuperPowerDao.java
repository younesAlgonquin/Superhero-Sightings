/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superheroSightings.dao;

import com.sg.superheroSightings.dto.SuperPower;
import java.util.List;

/**
 *
 * @author pc
 */
public interface SuperPowerDao {
    
    SuperPower getSuperPowerByID(int id);
    List<SuperPower> getAllSuperPowers();
    SuperPower addSuperPower(SuperPower superPower);
    void updateSuperPower (SuperPower supePower);
    void deleteSuperPowerById (int id);
    
}
