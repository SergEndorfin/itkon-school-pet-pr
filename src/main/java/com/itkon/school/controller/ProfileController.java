package com.itkon.school.controller;

import com.itkon.school.model.Address;
import com.itkon.school.model.Person;
import com.itkon.school.model.Profile;
import com.itkon.school.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProfileController {

    private final PersonRepository personRepository;


    @GetMapping("displayProfile")
    public ModelAndView displayProfile(HttpSession session) {
        Person person = (Person) session.getAttribute("loggedInPerson");
        Profile profile = Profile.builder()
                .name(person.getName())
                .mobileNumber(person.getMobileNumber())
                .email(person.getEmail()).build();
        if (person.getAddress() != null && person.getAddress().getAddressId() > 0) {
            Address address = person.getAddress();
            profile.setAddress1(address.getAddress1());
            profile.setAddress2(address.getAddress2());
            profile.setCity(address.getCity());
            profile.setState(address.getState());
            profile.setZipCode(address.getZipCode());
        }
        return new ModelAndView("profile")
                .addObject("profile", profile);
    }

    @PostMapping("updateProfile")
    public String updateProfile(@Valid @ModelAttribute("profile") Profile profile,
                                Errors errors, HttpSession session) {
        if (errors.hasErrors()) {
            return "profile";
        }
        Person person = (Person) session.getAttribute("loggedInPerson");
        person.setName(profile.getName());
        person.setEmail(profile.getEmail());
        person.setMobileNumber(profile.getMobileNumber());
        if (person.getAddress() == null || person.getAddress().getAddressId() <= 0) {
            person.setAddress(new Address());
        }
        Address address = person.getAddress();
        address.setAddress1(profile.getAddress1());
        address.setAddress2(profile.getAddress2());
        address.setCity(profile.getCity());
        address.setState(profile.getState());
        address.setZipCode(profile.getZipCode());
        person.setAddress(address);
        personRepository.save(person);
        session.setAttribute("loggedInPerson", person);
        return "redirect:/displayProfile";
    }
}
