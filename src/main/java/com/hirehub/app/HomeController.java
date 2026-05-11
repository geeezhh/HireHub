package com.hirehub.app;

import com.hirehub.app.entity.JobPost;
import com.hirehub.app.entity.User;
import com.hirehub.app.repository.JobApplicationRepository;
import com.hirehub.app.repository.JobPostRepository;
import com.hirehub.app.repository.UserRepository;
import com.hirehub.app.showcase.CompanyDirectory;
import com.hirehub.app.showcase.CompanyShowcase;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private JobPostRepository jobPostRepository;
    @Autowired
    private JobApplicationRepository jobApplicationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompanyDirectory companyDirectory;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        if (session.getAttribute("loggedInUser") != null) {
            User u = (User) session.getAttribute("loggedInUser");
            return "EMPLOYER".equals(u.getRole())
                    ? "redirect:/dashboard/employer"
                    : "redirect:/dashboard/seeker";
        }
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    // ── Seeker Dashboard ──────────────────────────────────────
    @GetMapping("/dashboard/seeker")
    public String seekerDashboard(HttpSession session, Model model,
            @RequestParam(required = false) String applied) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null)
            return "redirect:/login";

        model.addAttribute("user", user);
        model.addAttribute("applications",
                jobApplicationRepository.findBySeekerOrderByAppliedAtDesc(user));
        model.addAttribute("totalApplied",
                jobApplicationRepository.countBySeekerAndStatus(user, "APPLIED")
                        + jobApplicationRepository.countBySeekerAndStatus(user, "REVIEWING")
                        + jobApplicationRepository.countBySeekerAndStatus(user, "SHORTLISTED")
                        + jobApplicationRepository.countBySeekerAndStatus(user, "REJECTED"));
        model.addAttribute("shortlisted",
                jobApplicationRepository.countBySeekerAndStatus(user, "SHORTLISTED"));
        model.addAttribute("allJobs",
                jobPostRepository.findByStatusOrderByCreatedAtDesc("ACTIVE"));
        if (applied != null)
            model.addAttribute("successMsg", "Application submitted!");

        return "seeker-dashboard";
    }

    // ── Employer Dashboard ────────────────────────────────────
    @GetMapping("/dashboard/employer")
    public String employerDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null)
            return "redirect:/login";

        model.addAttribute("user", user);
        model.addAttribute("myJobs",
                jobPostRepository.findByEmployerOrderByCreatedAtDesc(user));
        model.addAttribute("recentApplicants",
                jobApplicationRepository.findByJobPost_EmployerOrderByAppliedAtDesc(user));
        model.addAttribute("activeJobs",
                jobPostRepository.countByEmployerAndStatus(user, "ACTIVE"));
        model.addAttribute("shortlisted",
                jobApplicationRepository.findByJobPost_EmployerAndStatus(user, "SHORTLISTED").size());

        return "employer-dashboard";
    }

    @GetMapping("/jobs/new")
    public String postJob(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null)
            return "redirect:/login";
        return "post-job";
    }

    // ── Placeholder routes (no 404) ───────────────────────────
    @GetMapping("/jobs")
    public String jobs(@RequestParam(required = false) String q, Model model, HttpSession session,
            @RequestParam(required = false) String applied,
            @RequestParam(required = false) String alreadyApplied) {
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", user);

        if (q != null && !q.isBlank()) {
            model.addAttribute("jobs", jobPostRepository.searchJobs(q));
        } else {
            model.addAttribute("jobs", jobPostRepository.findByStatusOrderByCreatedAtDesc("ACTIVE"));
        }

        if (applied != null) {
            model.addAttribute("successMsg", "Application submitted successfully.");
        }
        if (alreadyApplied != null) {
            model.addAttribute("infoMsg", "You already applied to this job.");
        }

        return "jobs";
    }

    @GetMapping("/companies")
    public String companies(Model model) {
        List<JobPost> activeJobs = jobPostRepository.findByStatusOrderByCreatedAtDesc("ACTIVE");
        model.addAttribute("hireHubJobs", activeJobs);

        Map<String, List<JobPost>> jobsByCompanyKey = new HashMap<>();
        for (CompanyShowcase c : companyDirectory.getShowcase()) {
            String kw = c.matchKeyword().toLowerCase();
            List<JobPost> matches = activeJobs.stream()
                    .filter(j -> employerMatchesKeyword(j.getEmployer(), kw))
                    .limit(6)
                    .collect(Collectors.toList());
            jobsByCompanyKey.put(c.matchKeyword(), matches);
        }
        model.addAttribute("jobsByCompanyKey", jobsByCompanyKey);
        model.addAttribute("companies", companyDirectory.getShowcase());
        return "companies";
    }

    private static boolean employerMatchesKeyword(User employer, String kw) {
        if (employer == null || kw == null || kw.isBlank()) {
            return false;
        }
        if (employer.getCompanyName() != null
                && employer.getCompanyName().toLowerCase().contains(kw)) {
            return true;
        }
        String full = ((employer.getFirstName() == null ? "" : employer.getFirstName()) + " "
                + (employer.getLastName() == null ? "" : employer.getLastName())).trim().toLowerCase();
        return full.contains(kw);
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "index";
    }

    @GetMapping("/privacy")
    public String privacy() {
        return "index";
    }

    @GetMapping("/settings")
    public String settings() {
        return "index";
    }

    @GetMapping("/forgot-password")
    public String forgotPw() {
        return "login";
    }

    @GetMapping("/dashboard/seeker/applications")
    public String seekerApps(HttpSession s, Model m) {
        return seekerDashboard(s, m, null);
    }

    @GetMapping("/dashboard/seeker/saved")
    public String seekerSaved(HttpSession s, Model m) {
        return seekerDashboard(s, m, null);
    }

    @GetMapping("/dashboard/seeker/profile")
    public String seekerProfile(HttpSession s, Model m) {
        return seekerDashboard(s, m, null);
    }

    @GetMapping("/dashboard/seeker/resume")
    public String seekerResume(HttpSession s, Model m) {
        return seekerDashboard(s, m, null);
    }

    @GetMapping("/dashboard/employer/jobs")
    public String empJobs(HttpSession s, Model m) {
        return employerDashboard(s, m);
    }

    @GetMapping("/dashboard/employer/applicants")
    public String empApplicants(HttpSession s, Model m) {
        return employerDashboard(s, m);
    }

    @GetMapping("/dashboard/employer/shortlisted")
    public String empShortlisted(HttpSession s, Model m) {
        return employerDashboard(s, m);
    }

    @GetMapping("/dashboard/employer/profile")
    public String empProfile(HttpSession s, Model m) {
        return employerDashboard(s, m);
    }

    // ── Report Preview Pages (no login needed) ───────────────
    @GetMapping("/report/dashboard")
    public String reportDashboard() {
        return "report-dashboard";
    }

    @GetMapping("/report/job-posting")
    public String reportJobPosting() {
        return "report-job-posting";
    }

    @GetMapping("/report/applications")
    public String reportApplications() {
        return "report-applications";
    }

}