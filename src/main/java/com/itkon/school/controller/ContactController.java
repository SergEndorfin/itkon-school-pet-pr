package com.itkon.school.controller;

import com.itkon.school.model.Contact;
import com.itkon.school.service.ContactService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
public class ContactController {

    private final ContactService contactService;
    private static final String CONTACT = "contact";

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping(CONTACT)
    public String displayContactPage(Model model) {
        model.addAttribute(CONTACT, new Contact());
        return CONTACT;
    }

    @PostMapping("saveMsg")
    public String saveMessage(@Valid @ModelAttribute(CONTACT) Contact contact, Errors errors) {
        if (errors.hasErrors()) {
            log.error("Contact form validation failed due to: {}", errors);
            return CONTACT;
        }
        contactService.saveMessageDetails(contact);
        return "redirect:/contact";
    }

    @GetMapping("displayMessages")
    public ModelAndView displayMessages(Model model) {
        List<Contact> contactMsgs = contactService.findMsgsWithStatus();
        ModelAndView modelAndView = new ModelAndView("messages");
        modelAndView.addObject("contactMsgs", contactMsgs);
        return modelAndView;
    }

    @GetMapping("closeMsg")
    public String closeMsg(@RequestParam int id) {
        contactService.updateMsgStatus(id);
        return "redirect:/displayMessages";
    }
}

