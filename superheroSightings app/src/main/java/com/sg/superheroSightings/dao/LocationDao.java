/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superheroSightings.dao;

import com.sg.superheroSightings.dto.Location;
import com.sg.superheroSightings.dto.SuperCreature;
import java.util.List;

/**
 *
 * @author pc
 */
public interface LocationDao {
    
    Location getLocationById(int id);
    List<Location> getAllLocations();
    Location addLocation(Location loocation);
    void updateLocation (Location location);
    void deleteLocationById(int id);
    
    List<Location> getlocationsBySupercreature(SuperCreature superCreature);
    
    
    
}
