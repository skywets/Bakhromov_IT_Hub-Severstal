package ru.severstal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.severstal.entity.Role;
import ru.severstal.entity.User;
import ru.severstal.repository.UserRepo;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public String getToRegistrationPage() {
        return "registration";
    }

    @PostMapping
    public String addUser(@Valid User user, BindingResult bindingResult, Map<String, Object> model, Model model1 ) {
        User newUser = userRepo.findByUsername(user.getUsername());

        if (newUser != null) {
            model.put("message", "Такой пользователь существует!");
            return "registration";
        }else if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);

            model1.mergeAttributes(errorsMap);
            model1.addAttribute("note", user);
            return  "registration";
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepo.save(user);

        return "redirect:/login";
    }
}
