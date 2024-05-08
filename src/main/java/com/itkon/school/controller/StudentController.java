package com.itkon.school.controller;

import com.itkon.school.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping("student")
public class StudentController {

    @GetMapping("/displayCourses")
    public ModelAndView displayCourses(HttpSession session) {
        Person person = (Person) session.getAttribute("loggedInPerson");

        return new ModelAndView("courses_enrolled")
                .addObject("person", person);
    }
}
