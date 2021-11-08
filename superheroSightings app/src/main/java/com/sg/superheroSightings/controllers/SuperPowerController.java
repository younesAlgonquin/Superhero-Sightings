package com.sg.superheroSightings.controllers;

import com.sg.superheroSightings.dao.AddressDao;
import com.sg.superheroSightings.dao.LocationDao;
import com.sg.superheroSightings.dao.OrganisationDao;
import com.sg.superheroSightings.dao.SightingDao;
import com.sg.superheroSightings.dao.SuperCreatureDao;
import com.sg.superheroSightings.dao.SuperPowerDao;
import com.sg.superheroSightings.dto.SuperPower;
import java.util.List;
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
public class SuperPowerController {

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

    //superPowers is the list name in the html page
    @GetMapping("superPowers")
    public String displaySuperPowers(Model model) {
        List<SuperPower> superPowers = superPowerDao.getAllSuperPowers();
        model.addAttribute("superPowers", superPowers);
        //that's the page name
        return "superPowers";
    }

    //addSuperPower is the function name in the html page
    @PostMapping("addSuperPower")
    public String addSuperPower(HttpServletRequest request) {
        String name = request.getParameter("name");
        String description = request.getParameter("description");

        SuperPower superPower = new SuperPower();
        superPower.setName(name);
        superPower.setDescription(description);

        superPowerDao.addSuperPower(superPower);

        return "redirect:/superPowers";
    }

    //deleteSuperPower is the function name in the html page
    @GetMapping("deleteSuperPower")
    public String deleteSuperPower(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        superPowerDao.deleteSuperPowerById(id);

        return "redirect:/superPowers";
    }

    @GetMapping("updateSuperPower")
    public String editTeacher(HttpServletRequest request, Model model) {
        int id = Integer.parseInt(request.getParameter("id"));
        SuperPower superPower = superPowerDao.getSuperPowerByID(id);

        model.addAttribute("superPower", superPower);
        return "updateSuperPower";
    }

    @PostMapping("updateSuperPower")
    public String performUpdateSuperPower(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        SuperPower superPower = superPowerDao.getSuperPowerByID(id);

        superPower.setName(request.getParameter("name"));
        superPower.setDescription(request.getParameter("description"));

        superPowerDao.updateSuperPower(superPower);

        return "redirect:/superPowers";
    }

}
