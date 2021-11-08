package com.sg.superheroSightings.controllers;

import com.sg.superheroSightings.dao.AddressDao;
import com.sg.superheroSightings.dao.LocationDao;
import com.sg.superheroSightings.dao.OrganisationDao;
import com.sg.superheroSightings.dao.SightingDao;
import com.sg.superheroSightings.dao.SuperCreatureDao;
import com.sg.superheroSightings.dao.SuperPowerDao;
import com.sg.superheroSightings.dto.Address;
import com.sg.superheroSightings.dto.Location;
import com.sg.superheroSightings.dto.SuperCreature;
import java.math.BigDecimal;
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
public class LocationController {

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
    @GetMapping("locations")
    public String displaySuLocations(Model model) {
        List<Location> locations = locationDao.getAllLocations();

        Map<Location, List<SuperCreature>> map = new HashMap<>();
        for (Location l : locations) {

            map.put(l , superCreatureDao.getSuperCreaturesByLocation(l));

        }
        model.addAttribute("locations", locations);
        model.addAttribute("map", map);
        //that's the page name
        return "locations";
    }

    @GetMapping("displaySuperCreatures")
    public String displaySuperCreatures(HttpServletRequest request, Model model) {

        int id = Integer.parseInt(request.getParameter("id"));
        Location location = locationDao.getLocationById(id);
        List<SuperCreature> superCreatures = superCreatureDao.getSuperCreaturesByLocation(location);

        model.addAttribute("superCreatures", superCreatures);
        model.addAttribute("location", location);
        //that's the page name
        return "SuperCreaturesByLocation";
    }

    //addSuperPower is the function name in the html page
    @PostMapping("addLocation")
    public String addLocation(HttpServletRequest request) {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
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
        location.setName(name);
        location.setDescription(description);
        location.setLatitude(new BigDecimal(latitude));
        location.setLongitude(new BigDecimal(longitude));
        location.setAddress(address);

        locationDao.addLocation(location);

        return "redirect:/locations";
    }

    //deleteSuperPower is the function name in the html page
    @GetMapping("deleteLocation")
    public String deleteLocation(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        int addressId = locationDao.getLocationById(id).getAddress().getId();
        locationDao.deleteLocationById(id);
        addressDao.deleteAddressById(addressId);

        return "redirect:/locations";
    }

    @GetMapping("updateLocation")
    public String updateLocation(HttpServletRequest request, Model model) {
        int id = Integer.parseInt(request.getParameter("id"));
        Location location = locationDao.getLocationById(id);

        model.addAttribute("location", location);
        return "updateLocation";
    }

    @PostMapping("updateLocation")
    public String performUpdateLocation(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        Location location = locationDao.getLocationById(id);
        int initialAddressId = location.getAddress().getId();

        String name = request.getParameter("name");
        String description = request.getParameter("description");
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

        location.setName(name);
        location.setDescription(description);
        location.setLatitude(new BigDecimal(latitude));
        location.setLongitude(new BigDecimal(longitude));
        location.setAddress(address);

        locationDao.updateLocation(location);
        addressDao.deleteAddressById(initialAddressId);

        return "redirect:/locations";
    }

}
