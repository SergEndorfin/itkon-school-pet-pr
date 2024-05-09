package com.itkon.school.controller;

import com.itkon.school.model.Courses;
import com.itkon.school.model.ItClass;
import com.itkon.school.model.Person;
import com.itkon.school.repository.CoursesRepository;
import com.itkon.school.repository.ItClassRepository;
import com.itkon.school.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("admin")
public class AdminController {

    private final ItClassRepository itClassRepository;
    private final PersonRepository personRepository;
    private final CoursesRepository coursesRepository;

    @RequestMapping("displayClasses")
    public ModelAndView displayClasses() {
        ModelAndView modelAndView = new ModelAndView("classes");
        modelAndView
                .addObject("itClass", new ItClass())
                .addObject("itClasses", itClassRepository.findAll());
        return modelAndView;
    }

    @PostMapping("addNewClass")
    public ModelAndView addNewClass(@ModelAttribute("itClass") ItClass itClass) {
        itClassRepository.save(itClass);
        return new ModelAndView("redirect:/admin/displayClasses");
    }

    @RequestMapping("deleteClass")
    public ModelAndView deleteClass(@RequestParam int id) {
        itClassRepository.findById(id)
                .ifPresent(itClass -> itClass.getPersons().forEach(
                        person -> {
                            person.setItClass(null);
                            personRepository.save(person);
                        }));
        itClassRepository.deleteById(id);
        return new ModelAndView("redirect:/admin/displayClasses");
    }

    @RequestMapping("displayStudents")
    public ModelAndView displayStudents(@RequestParam("classId") int id, HttpSession session,
                                        @RequestParam(value = "error", required = false) String error) {
        ItClass itClass = itClassRepository.findById(id).orElse(new ItClass());
        session.setAttribute("itClass", itClass);
        ModelAndView modelAndView = new ModelAndView("students")
                .addObject("person", new Person())
                .addObject("itClass", itClass);
        if (error != null) {
            modelAndView.addObject("errorMessage", "Invalid Email entered!");
        }
        return modelAndView;
    }

    @PostMapping("addStudent")
    public ModelAndView addStudent(@ModelAttribute("person") Person person, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        ItClass itClass = (ItClass) session.getAttribute("itClass");
        Person personEntity = personRepository.readByEmail(person.getEmail());
        if (personEntity == null || personEntity.getPersonId() < 1) {
            modelAndView.setViewName("redirect:/admin/displayStudents?classId=" + itClass.getClassId() +
                    "&error=true");
            return modelAndView;
        }
        personEntity.setItClass(itClass);
        personRepository.save(personEntity);
        itClass.getPersons().add(personEntity);
        itClassRepository.save(itClass);
        modelAndView.setViewName("redirect:/admin/displayStudents?classId=" + itClass.getClassId());
        return modelAndView;
    }

    @GetMapping("deleteStudent")
    public ModelAndView deleteStudent(@RequestParam int personId, HttpSession session) {
        ItClass itClass = (ItClass) session.getAttribute("itClass");
        personRepository.findById(personId)
                .ifPresent(person -> {
                    person.setItClass(null);
                    personRepository.save(person);
                    itClass.getPersons().remove(person);
                    session.setAttribute("itClass", itClassRepository.save(itClass));
                });
        return new ModelAndView("redirect:/admin/displayStudents?classId=" + itClass.getClassId());
    }

    @GetMapping("displayCourses")
    public ModelAndView displayCourses() {
//        List<Courses> courses = coursesRepository.findByOrderByNameDesc(); // static sorting here
        List<Courses> courses = coursesRepository.findAll(Sort.by("name").ascending());// dynamic sorting here
        ModelAndView modelAndView = new ModelAndView("courses_secure");
        modelAndView.addObject("courses", courses);
        modelAndView.addObject("course", new Courses());
        return modelAndView;
    }

    @PostMapping("/addNewCourse")
    public ModelAndView addNewCourse(@ModelAttribute("course") Courses course) {
        ModelAndView modelAndView = new ModelAndView();
        coursesRepository.save(course);
        modelAndView.setViewName("redirect:/admin/displayCourses");
        return modelAndView;
    }

    @GetMapping("/viewStudents")
    public ModelAndView viewStudents(@RequestParam int id,
                                     HttpSession session,
                                     @RequestParam(required = false) String error) {
        String errorMessage = null;
        Optional<Courses> courses = coursesRepository.findById(id);
        ModelAndView modelAndView = new ModelAndView("course_students")
                .addObject("courses", courses.get())
                .addObject("person", new Person());

        session.setAttribute("courses", courses.get());
        if (error != null) {
            errorMessage = "Invalid Email entered!!";
            modelAndView.addObject("errorMessage", errorMessage);
        }
        return modelAndView;
    }

    @PostMapping("/addStudentToCourse")
    public ModelAndView addStudentToCourse(@ModelAttribute("person") Person person,
                                           HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        Courses courses = (Courses) session.getAttribute("courses");
        Person personEntity = personRepository.readByEmail(person.getEmail());
        if (personEntity == null || personEntity.getPersonId() <= 0) {
            modelAndView.setViewName("redirect:/admin/viewStudents?id=" + courses.getCourseId()
                    + "&error=true");
            return modelAndView;
        }
        personEntity.getCourses().add(courses);
        courses.getPersons().add(personEntity);
        personRepository.save(personEntity);
        session.setAttribute("courses", courses);
        modelAndView.setViewName("redirect:/admin/viewStudents?id=" + courses.getCourseId());
        return modelAndView;
    }

    @GetMapping("/deleteStudentFromCourse")
    public ModelAndView deleteStudentFromCourse(@RequestParam int personId,
                                                HttpSession session) {
        Courses courses = (Courses) session.getAttribute("courses");
        Optional<Person> person = personRepository.findById(personId);
        person.get().getCourses().remove(courses);
        courses.getPersons().remove(person);
        personRepository.save(person.get());
        session.setAttribute("courses", courses);
        return new ModelAndView("redirect:/admin/viewStudents?id=" + courses.getCourseId());
    }
}
