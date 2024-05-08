package com.itkon.school.controller;

import com.itkon.school.model.Holiday;
import com.itkon.school.repository.HolidaysRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HolidaysController {

    public static final String FESTIVAL = "festival";
    public static final String FEDERAL = "federal";
    private final HolidaysRepository holidaysRepository;

    @GetMapping("holidays/{display}")
    public String displayContactPage(@PathVariable String display,
                                     Model model) {
        if (null != display && display.equals("all")) {
            model.addAttribute(FESTIVAL, true);
            model.addAttribute(FEDERAL, true);
        } else if (null != display && display.equals(FEDERAL)) {
            model.addAttribute(FEDERAL, true);
        } else if (null != display && display.equals(FESTIVAL)) {
            model.addAttribute(FESTIVAL, true);
        }

        Iterable<Holiday> holidaysIterable = holidaysRepository.findAll();
        List<Holiday> holidays = StreamSupport.stream(holidaysIterable.spliterator(), false)
                .collect(Collectors.toList());
        Holiday.Type[] types = Holiday.Type.values();
        for (Holiday.Type type : types) {
            model.addAttribute(type.toString(),
                    (holidays.stream().filter(holiday -> holiday.getType().equals(type)).collect(Collectors.toList())));
        }
        return "holidays";
    }
}
