/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superheroSightings.dao;

import com.sg.superheroSightings.dto.Organisation;
import com.sg.superheroSightings.dto.SuperCreature;
import java.util.List;

/**
 *
 * @author pc
 */
public interface OrganisationDao {
    
    Organisation getOrganisationById(int id);
    List<Organisation> getAllOrganisations();
    Organisation addOrganisation(Organisation organisation);
    void updateOrganisation(Organisation organisation);
    void deleteOrganisationById(int id);
    
    List<SuperCreature> getSuperCreaturesByOrganisation(Organisation organisation);
    
    
}
