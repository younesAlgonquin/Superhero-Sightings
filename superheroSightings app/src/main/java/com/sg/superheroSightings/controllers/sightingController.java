package com.sg.superheroSightings.controllers;

import com.sg.superheroSightings.dao.AddressDao;
import com.sg.superheroSightings.dao.LocationDao;
import com.sg.superheroSightings.dao.OrganisationDao;
import com.sg.superheroSightings.dao.SightingDao;
import com.sg.superheroSightings.dao.SuperCreatureDao;
import com.sg.superheroSightings.dao.SuperPowerDao;
import com.sg.superheroSightings.dto.Address;
import com.sg.superheroSightings.dto.Location;
import com.sg.superheroSightings.dto.Sighting;
import com.sg.superheroSightings.dto.SuperCreature;
import java.math.BigDecimal;
import java.time.LocalDate;
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
public class sightingController {

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
    @GetMapping("sightings")
    public String displaySightings(Model model) {
        List<Sighting> sightings = sightingDao.getAllSighting();
        List<SuperCreature> superCreatures = superCreatureDao.getAllSuperCreatures();
        model.addAttribute("sightings", sightings);
        model.addAttribute("superCreatures", superCreatures);
        //that's the page name
        return "sightings";
    }

    @GetMapping("displaySightingsByDay")
    public String displaySightingsByDay(Model model, HttpServletRequest request) {

        LocalDate date = LocalDate.parse(request.getParameter("date"));
        List<Sighting> sightings = sightingDao.getSightingsByDate(date);

        model.addAttribute("sightings", sightings);
        model.addAttribute("date", date);

        //that's the page name
        return "sightingsByDate";
    }

    //addSuperPower is the function name in the html page
    @PostMapping("addSighting")

    public String addSighting(HttpServletRequest request) {
        String dateAsString = request.getParameter("date");
        String description = request.getParameter("description");
        String superCreatureId = request.getParameter("superCreatureId");

        String locationName = request.getParameter("locationName");
        String locationDescription = request.getParameter("locationDescription");
        String latitude = request.getParameter("latitude");
        String longitude = request.getParameter("longitude");
        String street = request.getParameter("street");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String country = request.getParameter("country");
        String postalCode = request.getParameter("postalCode");

        Address address = new Address();
        address.setStreet(street);
        address.setCity(city);
        address.setState(state);
        address.setCountry(country);
        address.setPostalCode(postalCode);

        addressDao.addAddress(address);

        Location location = new Location();
        location.setName(locationName);
        location.setDescription(locationDescription);
        location.setLatitude(new BigDecimal(latitude));
        location.setLongitude(new BigDecimal(longitude));
        location.setAddress(address);

        SuperCreature superCreature = superCreatureDao.getSuperCreatureById(Integer.parseInt(superCreatureId));
        LocalDate date = LocalDate.parse(dateAsString);

        Sighting sighting = new Sighting();

        sighting.setDate(date);
        sighting.setDescription(description);
        sighting.setSuperCreature(superCreature);
        sighting.setLocation(location);

        sightingDao.addSighting(sighting);

        return "redirect:/sightings";
    }

    //deleteSuperPower is the function name in the html page
    @GetMapping("deleteSighting")
    public String deleteSighting(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        int initialaddressId = sightingDao.getSightingById(id).getLocation().getAddress().getId();
        sightingDao.deleteSightingById(id);
        addressDao.deleteAddressById(initialaddressId);

        return "redirect:/sightings";
    }

    @GetMapping("updateSighting")
    public String updateSighting(HttpServletRequest request, Model model) {
        int id = Integer.parseInt(request.getParameter("id"));
        Sighting sighting = sightingDao.getSightingById(id);
        List<SuperCreature> superCreatures = superCreatureDao.getAllSuperCreatures();

        model.addAttribute("superCreatures", superCreatures);
        model.addAttribute("sighting", sighting);

        return "updateSighting";
    }

    @PostMapping("updateSighting")
    public String performUpdateSighting(HttpServletRequest request) {

        int id = Integer.parseInt(request.getParameter("id"));
        String dateAsString = request.getParameter("date");
        String description = request.getParameter("description");
        String superCreatureId = request.getParameter("superCreatureId");

        Sighting sighting = sightingDao.getSightingById(id);
        int initialAddressId = sightingDao.getSightingById(id).getLocation().getAddress().getId();

        String locationName = request.getParameter("locationName");
        String locationDescription = request.getParameter("locationDescription");
        String latitude = request.getParameter("latitude");
        String longitude = request.getParameter("longitude");
        String street = request.getParameter("street");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String country = request.getParameter("country");
        String postalCode = request.getParameter("postalCode");

        Address address = new Address();
        address.setStreet(street);
        address.setCity(city);
        address.setState(state);
        address.setCountry(country);
        address.setPostalCode(postalCode);

        addressDao.addAddress(address);

        Location location = new Location();
        location.setName(locationName);
        location.setDescription(locationDescription);
        location.setLatitude(new BigDecimal(latitude));
        location.setLongitude(new BigDecimal(longitude));
        location.setAddress(address);

        SuperCreature superCreature = superCreatureDao.getSuperCreatureById(Integer.parseInt(superCreatureId));
        LocalDate date = LocalDate.parse(dateAsString);

        sighting.setDate(date);
        sighting.setDescription(description);
        sighting.setSuperCreature(superCreature);
        sighting.setLocation(location);

        sightingDao.updateSighting(sighting);
        addressDao.deleteAddressById(initialAddressId);

        return "redirect:/sightings";
    }

    @GetMapping("home")
    public String displayHome(Model model) {
        List<Sighting> sightings = sightingDao.getAllSighting();
        List<SuperCreature> superCreatures = superCreatureDao.getAllSuperCreatures();
        model.addAttribute("sightings", sightings);
        model.addAttribute("superCreatures", superCreatures);
        //that's the page name
        return "home";
    }

}
