package com.itkon.school.controller;

import com.itkon.school.model.Person;
import com.itkon.school.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("public")
public class PublicController {

    private final PersonService personService;

    @GetMapping("register")
    public String displayRegisterPage(Model model) {
        model.addAttribute("person", new Person());
        return "register";
    }

    @PostMapping("createUser")
    public String createUser(@Valid @ModelAttribute("person") Person person,
                             Errors errors) {
        boolean isSaved = personService.createNewPerson(person);
        if (isSaved) {
            return "redirect:/login?register=true";
        } else {
            return "register";
        }
    }
}
