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
import java.util.Locale;
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
        String normalizedEmail = email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
        String normalizedRole = role == null ? "SEEKER" : role.trim().toUpperCase(Locale.ROOT);

        // Check if email already registered (case-insensitive)
        if (userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            model.addAttribute("error", "An account with this email already exists.");
            return "register";
        }

        // Build user
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(normalizedEmail);
        user.setPassword(password == null ? "" : password.trim()); // ⚠ plain text for now — see note below
        user.setRole(normalizedRole);
        user.setPhone(phone);
        user.setLocation(location);

        if ("SEEKER".equals(normalizedRole)) {
            user.setTitle(title);
            user.setExperience(experience);
            user.setSkills(skills);

            // Save resume file if uploaded
            if (resume != null && !resume.isEmpty()) {
                try {
                    String uploadDir = "uploads/resumes/";
                    new File(uploadDir).mkdirs();
                    String filename = normalizedEmail.replaceAll("[^a-zA-Z0-9]", "_") + "_" + resume.getOriginalFilename();
                    resume.transferTo(new File(uploadDir + filename));
                    user.setResumePath(uploadDir + filename);
                } catch (IOException e) {
                    // Resume upload failed — continue without it
                }
            }
        } else if ("EMPLOYER".equals(normalizedRole)) {
            user.setCompanyName(companyName);
            user.setIndustry(industry);
            user.setCompanySize(companySize);
            user.setCompanyWebsite(companyWebsite);
            user.setCompanyDesc(companyDesc);
        }

        User savedUser = userRepository.save(user);

        // Auto-login after registration
        session.setAttribute("loggedInUser", savedUser);

        // Redirect based on role
        return redirectByRole(savedUser.getRole());
    }

    // ── LOGIN ─────────────────────────────────────────────────

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(required = false) String role,
            Model model,
            HttpSession session) {
        String emailInput = email == null ? "" : email.trim();
        String passwordInput = password == null ? "" : password.trim();
        Optional<User> userOpt = userRepository.findByEmailIgnoreCase(emailInput);

        if (userOpt.isEmpty()) {
            model.addAttribute("error", "No account found with this email.");
            return "login";
        }

        User user = userOpt.get();
        String storedPassword = user.getPassword() == null ? "" : user.getPassword().trim();

        if (!storedPassword.equals(passwordInput)) {
            model.addAttribute("error", "Incorrect password. Please try again.");
            return "login";
        }

        String tabRole = normalizeTabRole(role);
        String userRole = user.getRole() == null ? "SEEKER" : user.getRole().trim().toUpperCase(Locale.ROOT);
        if (tabRole != null && !tabMatchesAccount(tabRole, userRole)) {
            model.addAttribute("error", mismatchMessage(user.getRole()));
            return "login";
        }

        // Fresh instance from DB for a clean session (avoids stale / detached entities)
        User sessionUser = userRepository.findById(user.getId()).orElse(user);
        session.setAttribute("loggedInUser", sessionUser);

        return redirectByRole(sessionUser.getRole());
    }

    // ── LOGOUT ────────────────────────────────────────────────

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // ── HELPER ───────────────────────────────────────────────

    private String redirectByRole(String role) {
        String safeRole = role == null ? "SEEKER" : role.trim().toUpperCase(Locale.ROOT);
        return switch (safeRole) {
            case "EMPLOYER" -> "redirect:/dashboard/employer";
            case "ADMIN" -> "redirect:/dashboard/employer";
            default -> "redirect:/dashboard/seeker";
        };
    }

    /** Maps login tab value (seeker / employer / admin) to stored role, or null if absent. */
    private static String normalizeTabRole(String roleParam) {
        if (roleParam == null || roleParam.isBlank()) {
            return null;
        }
        return switch (roleParam.trim().toLowerCase(Locale.ROOT)) {
            case "employer" -> "EMPLOYER";
            case "admin" -> "ADMIN";
            default -> "SEEKER";
        };
    }

    private static String mismatchMessage(String actualRole) {
        String r = actualRole == null ? "SEEKER" : actualRole.trim().toUpperCase(Locale.ROOT);
        String label = switch (r) {
            case "EMPLOYER" -> "an Employer";
            case "ADMIN" -> "an Admin";
            default -> "a Job Seeker";
        };
        return "This email is registered as " + label + " account. Use the matching tab above and try again.";
    }

    /** Admin tab is for recruiter-side access; allow employer (and real admin) accounts only. */
    private static boolean tabMatchesAccount(String tabRole, String userRole) {
        if ("ADMIN".equals(tabRole)) {
            return "EMPLOYER".equals(userRole) || "ADMIN".equals(userRole);
        }
        return tabRole.equals(userRole);
    }
}