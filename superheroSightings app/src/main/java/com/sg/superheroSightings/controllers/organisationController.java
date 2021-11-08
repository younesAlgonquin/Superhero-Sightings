package com.sg.superheroSightings.controllers;

import com.sg.superheroSightings.dao.AddressDao;
import com.sg.superheroSightings.dao.LocationDao;
import com.sg.superheroSightings.dao.OrganisationDao;
import com.sg.superheroSightings.dao.SightingDao;
import com.sg.superheroSightings.dao.SuperCreatureDao;
import com.sg.superheroSightings.dao.SuperPowerDao;
import com.sg.superheroSightings.dto.Address;
import com.sg.superheroSightings.dto.Organisation;
import com.sg.superheroSightings.dto.SuperCreature;
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
public class organisationController {

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
    @GetMapping("organisations")
    public String displayOrganisations(Model model) {
        List<Organisation> organisations = organisationDao.getAllOrganisations();
        model.addAttribute("organisations", organisations);
        //that's the page name
        return "organisations";
    }

    @GetMapping("displaySuperCreaturesForOrganisation")
    public String displaySuperCreaturesForOrganisation(HttpServletRequest request, Model model) {

        int id = Integer.parseInt(request.getParameter("id"));
        Organisation organisation = organisationDao.getOrganisationById(id);
        List<SuperCreature> superCreatures = organisationDao.getSuperCreaturesByOrganisation(organisation);

        model.addAttribute("superCreatures", superCreatures);
        model.addAttribute("organisation", organisation);
        //that's the page name
        return "superCreaturesInTheOrganisation";
    }


    //addSuperPower is the function name in the html page
    @PostMapping("addOrganisation")

    public String addOrganisation(HttpServletRequest request) {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
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

        Organisation organisation = new Organisation();
        organisation.setName(name);
        organisation.setDescription(description);
        organisation.setPhone(phone);
        organisation.setEmail(email);
        organisation.setAddress(address);

        organisationDao.addOrganisation(organisation);

        return "redirect:/organisations";
    }

    //deleteSuperPower is the function name in the html page
    @GetMapping("deleteOrganisation")
    public String deleteOrganisation(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        int addressId = organisationDao.getOrganisationById(id).getAddress().getId();
        organisationDao.deleteOrganisationById(id);
        addressDao.deleteAddressById(addressId);

        return "redirect:/organisations";
    }

    @GetMapping("updateOrganisation")
    public String updateOrganisation(HttpServletRequest request, Model model) {
        int id = Integer.parseInt(request.getParameter("id"));
        Organisation organisation = organisationDao.getOrganisationById(id);

        model.addAttribute("organisation", organisation);
        return "updateOrganisation";
    }

    @PostMapping("updateOrganisation")
    public String performUpdateOrganisation(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        Organisation organisation = organisationDao.getOrganisationById(id);
        int initialAddressId = organisation.getAddress().getId();

        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
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

        organisation.setName(name);
        organisation.setDescription(description);
        organisation.setPhone(phone);
        organisation.setEmail(email);
        organisation.setAddress(address);

        organisationDao.updateOrganisation(organisation);
        addressDao.deleteAddressById(initialAddressId);

        return "redirect:/organisations";
    }
}
