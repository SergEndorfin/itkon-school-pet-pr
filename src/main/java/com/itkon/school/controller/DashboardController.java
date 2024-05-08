package com.itkon.school.controller;

import com.itkon.school.model.Person;
import com.itkon.school.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final PersonRepository personRepository;

    @RequestMapping("dashboard")
    public String displayDashboard(Model model, Authentication authentication,
                                   HttpSession httpSession) {
        Person person = personRepository.readByEmail(authentication.getName());
        httpSession.setAttribute("loggedInPerson", person);

        model.addAttribute("username", person.getName());
        model.addAttribute("roles", authentication.getAuthorities().toString());
        if (null != person.getItClass() && null != person.getItClass().getName()) {
            model.addAttribute("enrolledClass", person.getItClass().getName());
        }
        return "dashboard";
    }


}
