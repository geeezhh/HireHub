package com.hirehub.app.controller;

import com.hirehub.app.entity.User;
import com.hirehub.app.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // ── REGISTER ──────────────────────────────────────────────

    @PostMapping("/register")
    public String register(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String role,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String location,
            // Seeker fields
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String experience,
            @RequestParam(required = false) String skills,
            @RequestParam(required = false) MultipartFile resume,
            // Employer fields
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) String companySize,
            @RequestParam(required = false) String companyWebsite,
            @RequestParam(required = false) String companyDesc,
            Model model,
            HttpSession session) {

        // Check if email already registered
        if (userRepository.existsByEmail(email)) {
            model.addAttribute("error", "An account with this email already exists.");
            return "register";
        }

        // Build user
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password); // ⚠ plain text for now — see note below
        user.setRole(role.toUpperCase());
        user.setPhone(phone);
        user.setLocation(location);

        if ("SEEKER".equalsIgnoreCase(role)) {
            user.setTitle(title);
            user.setExperience(experience);
            user.setSkills(skills);

            // Save resume file if uploaded
            if (resume != null && !resume.isEmpty()) {
                try {
                    String uploadDir = "uploads/resumes/";
                    new File(uploadDir).mkdirs();
                    String filename = email.replaceAll("[^a-zA-Z0-9]", "_") + "_" + resume.getOriginalFilename();
                    resume.transferTo(new File(uploadDir + filename));
                    user.setResumePath(uploadDir + filename);
                } catch (IOException e) {
                    // Resume upload failed — continue without it
                }
            }
        } else if ("EMPLOYER".equalsIgnoreCase(role)) {
            user.setCompanyName(companyName);
            user.setIndustry(industry);
            user.setCompanySize(companySize);
            user.setCompanyWebsite(companyWebsite);
            user.setCompanyDesc(companyDesc);
        }

        userRepository.save(user);

        // Auto-login after registration
        session.setAttribute("loggedInUser", user);

        // Redirect based on role
        return redirectByRole(user.getRole());
    }

    // ── LOGIN ─────────────────────────────────────────────────

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            Model model,
            HttpSession session) {

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            model.addAttribute("error", "No account found with this email.");
            return "login";
        }

        User user = userOpt.get();

        if (!user.getPassword().equals(password)) {
            model.addAttribute("error", "Incorrect password. Please try again.");
            return "login";
        }

        // Save user in session
        session.setAttribute("loggedInUser", user);

        return redirectByRole(user.getRole());
    }

    // ── LOGOUT ────────────────────────────────────────────────

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // ── HELPER ───────────────────────────────────────────────

    private String redirectByRole(String role) {
        return switch (role) {
            case "EMPLOYER" -> "redirect:/dashboard/employer";
            case "ADMIN" -> "redirect:/dashboard/admin";
            default -> "redirect:/dashboard/seeker";
        };
    }
}