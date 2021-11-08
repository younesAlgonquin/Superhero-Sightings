/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superheroSightings.dao;

import com.sg.superheroSightings.dto.Location;
import com.sg.superheroSightings.dto.Organisation;
import com.sg.superheroSightings.dto.SuperCreature;
import java.util.List;

/**
 *
 * @author pc
 */
public interface SuperCreatureDao {
    
    SuperCreature getSuperCreatureById(int id);
    List<SuperCreature> getAllSuperCreatures();
    SuperCreature addSuperCreature(SuperCreature superCreature);
    void updateSuperCreature (SuperCreature supercreature);  
    void deleteSuperCreatureById(int id);
    
    List<SuperCreature> getSuperCreaturesByLocation(Location location);
    List<Organisation> getOrganisationsBySuperCreature(SuperCreature superCreature);
    
    
}
