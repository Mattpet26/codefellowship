package com.petersen.CodeFellowship.controllers;

import com.petersen.CodeFellowship.models.user.ApplicationUser;
import com.petersen.CodeFellowship.models.user.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.sql.Date;

@Controller
public class ApplicationUserController {

    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login (Principal principal, Model m){
        m.addAttribute("principal", principal);
        return "login";
    }

    @GetMapping("/signup")
    public String showSignUp(Principal principal, Model m){
        m.addAttribute("principal", principal);
        return "signup";
    }

    @GetMapping("/user/{username}")
    public String showUserDetails(@PathVariable String username, Model userinfo, Principal principal){
        ApplicationUser user = applicationUserRepository.findByUsername(username);
        if(user == null){
            userinfo.addAttribute("userDoesNotExist", true);
        }
        userinfo.addAttribute("user", user);
        userinfo.addAttribute("principal", principal.getName());
        return "userdetail";
    }

    @PostMapping("/newuser")
    //makes user
    public RedirectView makeANewUser(
            String username,
            String password,
            String firstName,
            String lastName,
            Date dateOfBirth,
            String bio
    ){
        password = passwordEncoder.encode(password);
        System.out.println("adding user");
        //saves user

        ApplicationUser newUser = new ApplicationUser(username, password, firstName, lastName, dateOfBirth, bio);
        applicationUserRepository.save(newUser);

        //sends user home
        return new RedirectView("/login");
    }
    @PostMapping("/feed/{username}")
    public String showFeedDetails(@PathVariable String username, Model userinfo, Principal principal) {
        ApplicationUser user = applicationUserRepository.findByUsername(username);
        userinfo.addAttribute("user", user);
        userinfo.addAttribute("principal", principal.getName());
        return "feed";
    }
}
