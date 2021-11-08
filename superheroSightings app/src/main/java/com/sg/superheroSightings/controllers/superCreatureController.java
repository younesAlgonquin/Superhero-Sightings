package com.sg.superheroSightings.controllers;

import com.sg.superheroSightings.dao.AddressDao;
import com.sg.superheroSightings.dao.LocationDao;
import com.sg.superheroSightings.dao.OrganisationDao;
import com.sg.superheroSightings.dao.SightingDao;
import com.sg.superheroSightings.dao.SuperCreatureDao;
import com.sg.superheroSightings.dao.SuperPowerDao;
import com.sg.superheroSightings.dto.Location;
import com.sg.superheroSightings.dto.Organisation;
import com.sg.superheroSightings.dto.SuperCreature;
import com.sg.superheroSightings.dto.SuperPower;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Younes Current date: Purpose of the class:
 */
@Controller
public class superCreatureController {

    @Autowired
    AddressDao addressDao;

    @Autowired
    SuperPowerDao superPowerDao;

    @Autowired
    SightingDao sightingDao;

    @Autowired
    SuperCreatureDao superCreatureDao;

    @Autowired
    LocationDao locationDao;

    @Autowired
    OrganisationDao organisationDao;

    //superCreatures is the list name in the html page
    @GetMapping("superCreatures")
    public String displaySuperCreatures(Model model) {
        List<SuperCreature> superCreatures = superCreatureDao.getAllSuperCreatures();
        List<SuperPower> superPowers = superPowerDao.getAllSuperPowers();
        List<Organisation> organisations = organisationDao.getAllOrganisations();
        
        Map<SuperCreature, List<Location>> map = new HashMap<>();
        for(SuperCreature s: superCreatures ){
        
            map.put(s, locationDao.getlocationsBySupercreature(s));
                
        }

        model.addAttribute("superCreatures", superCreatures);
        model.addAttribute("superPowers", superPowers);
        model.addAttribute("organisations", organisations);
        model.addAttribute("map", map);
        //that's the page name
        return "superCreatures";
    }

    @GetMapping("displayOrganisations")
    public String displayOrganisations(HttpServletRequest request, Model model) {

        int id = Integer.parseInt(request.getParameter("id"));
        SuperCreature superCreature = superCreatureDao.getSuperCreatureById(id);
        List<Organisation> organisations = superCreatureDao.getOrganisationsBySuperCreature(superCreature);

        model.addAttribute("organisations", organisations);
        model.addAttribute("superCreature", superCreature);
        //that's the page name
        return "organisationsBySuperCreature";
    }

    @GetMapping("displayLocations")
    public String displayLocations(HttpServletRequest request, Model model) {

        int id = Integer.parseInt(request.getParameter("id"));
        SuperCreature superCreature = superCreatureDao.getSuperCreatureById(id);
        List<Location> locations = locationDao.getlocationsBySupercreature(superCreature);

        model.addAttribute("locations", locations);
        model.addAttribute("superCreature", superCreature);
        //that's the page name
        return "locationsBySuperCreature";
    }

    

    //addSuperCreature is the function name in the html page
    @PostMapping("addSuperCreature")

    public String addSuperCreature(HttpServletRequest request) {
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        String description = request.getParameter("description");

        String superPowerId = request.getParameter("superPowerId");
        SuperPower superPower = superPowerDao.getSuperPowerByID(Integer.parseInt(superPowerId));

        String[] organisationIds = request.getParameterValues("organisationId");
        List<Organisation> SuperCreatureOrganisations = new ArrayList<>();
        for (String organisationId : organisationIds) {
            SuperCreatureOrganisations.add(organisationDao.getOrganisationById(Integer.parseInt(organisationId)));
        }

        SuperCreature superCreature = new SuperCreature();
        superCreature.setName(name);
        superCreature.setType(type);
        superCreature.setDescription(description);
        superCreature.setSuperPower(superPower);
        superCreature.setOrganisations(SuperCreatureOrganisations);

        superCreatureDao.addSuperCreature(superCreature);

        return "redirect:/superCreatures";
    }

    //deleteSuperCreature is the function name in the html page
    @GetMapping("deleteSuperCreature")
    public String deleteSuperCreature(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        superCreatureDao.deleteSuperCreatureById(id);

        return "redirect:/superCreatures";
    }

    @GetMapping("updateSuperCreature")
    public String updateSuperCreature(HttpServletRequest request, Model model) {
        int id = Integer.parseInt(request.getParameter("id"));
        SuperCreature superCreature = superCreatureDao.getSuperCreatureById(id);
        List<SuperPower> superPowers = superPowerDao.getAllSuperPowers();
        List<Organisation> organisations = organisationDao.getAllOrganisations();

        model.addAttribute("superCreature", superCreature);
        model.addAttribute("superPowers", superPowers);
        model.addAttribute("organisations", organisations);

        return "updateSuperCreature";
    }

    @PostMapping("updateSuperCreature")
    public String performUpdateSuperCreature(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        SuperCreature superCreature = superCreatureDao.getSuperCreatureById(id);

        superCreature.setName(request.getParameter("name"));
        superCreature.setType(request.getParameter("type"));
        superCreature.setDescription(request.getParameter("description"));

        superCreature.setSuperPower(superPowerDao.getSuperPowerByID(Integer.parseInt(request.getParameter("superPowerId"))));

        String[] organisationIds = request.getParameterValues("organisationId");
        List<Organisation> SuperCreatureOrganisations = new ArrayList<>();
        for (String organisationId : organisationIds) {
            SuperCreatureOrganisations.add(organisationDao.getOrganisationById(Integer.parseInt(organisationId)));
        }
        superCreature.setOrganisations(SuperCreatureOrganisations);

        superCreatureDao.updateSuperCreature(superCreature);

        return "redirect:/superCreatures";
    }

}
