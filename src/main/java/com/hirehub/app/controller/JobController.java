package com.hirehub.app.controller;

import com.hirehub.app.entity.JobApplication;
import com.hirehub.app.entity.JobPost;
import com.hirehub.app.entity.User;
import com.hirehub.app.repository.JobApplicationRepository;
import com.hirehub.app.repository.JobPostRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class JobController {

    @Autowired
    private JobPostRepository jobPostRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    // ── POST A JOB (Employer) ─────────────────────────────────

    @PostMapping("/jobs/new")
    public String postJob(
            @RequestParam String jobTitle,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String jobType,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String workMode,
            @RequestParam(required = false) String experience,
            @RequestParam(required = false) String education,
            @RequestParam(required = false) String skills,
            @RequestParam(required = false) String jobDescription,
            @RequestParam(required = false) Double salaryMin,
            @RequestParam(required = false) Double salaryMax,
            @RequestParam(required = false) String salaryType,
            @RequestParam(required = false) String perks,
            @RequestParam(required = false) Integer openings,
            @RequestParam(required = false) String deadline,
            @RequestParam(required = false) String action,
            HttpSession session,
            Model model) {

        User employer = (User) session.getAttribute("loggedInUser");

        // Must be logged in as employer
        if (employer == null || !"EMPLOYER".equals(employer.getRole())) {
            return "redirect:/login";
        }

        JobPost job = new JobPost();
        job.setJobTitle(jobTitle);
        job.setDepartment(department);
        job.setJobType(jobType);
        job.setLocation(location);
        job.setWorkMode(workMode);
        job.setExperience(experience);
        job.setEducation(education);
        job.setSkills(skills);
        job.setJobDescription(jobDescription);
        job.setSalaryMin(salaryMin);
        job.setSalaryMax(salaryMax);
        job.setSalaryType(salaryType);
        job.setPerks(perks);
        job.setOpenings(openings);
        job.setDeadline(deadline);
        job.setEmployer(employer);

        // Save as draft or publish
        job.setStatus("draft".equals(action) ? "DRAFT" : "ACTIVE");

        jobPostRepository.save(job);

        return "redirect:/dashboard/employer";
    }

    // ── APPLY TO A JOB (Seeker) ───────────────────────────────

    @PostMapping("/jobs/{jobId}/apply")
    public String applyToJob(
            @PathVariable Long jobId,
            HttpSession session) {

        User seeker = (User) session.getAttribute("loggedInUser");

        if (seeker == null || !"SEEKER".equals(seeker.getRole())) {
            return "redirect:/login";
        }

        JobPost job = jobPostRepository.findById(jobId).orElse(null);
        if (job == null)
            return "redirect:/jobs";

        // Don't apply twice
        if (jobApplicationRepository.existsBySeekerAndJobPost(seeker, job)) {
            return "redirect:/dashboard/seeker?alreadyApplied=true";
        }

        JobApplication application = new JobApplication();
        application.setSeeker(seeker);
        application.setJobPost(job);
        application.setStatus("APPLIED");

        jobApplicationRepository.save(application);

        return "redirect:/dashboard/seeker?applied=true";
    }

    // ── UPDATE APPLICATION STATUS (Employer shortlist/reject) ─

    @PostMapping("/applications/{appId}/status")
    public String updateStatus(
            @PathVariable Long appId,
            @RequestParam String status,
            HttpSession session) {

        User employer = (User) session.getAttribute("loggedInUser");
        if (employer == null || !"EMPLOYER".equals(employer.getRole())) {
            return "redirect:/login";
        }

        jobApplicationRepository.findById(appId).ifPresent(app -> {
            app.setStatus(status.toUpperCase());
            jobApplicationRepository.save(app);
        });

        return "redirect:/dashboard/employer";
    }
}