/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superheroSightings.dao;

import com.sg.superheroSightings.dto.Sighting;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author pc
 */
public interface SightingDao {
    
    Sighting getSightingById(int id);
    List<Sighting> getAllSighting();
    Sighting addSighting (Sighting sighting);
    void updateSighting (Sighting sighting);
    void deleteSightingById(int id);
    
    List<Sighting> getSightingsByDate(LocalDate date);
    
    
}
