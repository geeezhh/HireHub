package com.hirehub.app.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_posts")
public class JobPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ── Job Details ───────────────────────────────────────────
    @Column(nullable = false)
    private String jobTitle;

    private String department;
    private String jobType;      // Full-time, Internship, etc.
    private String location;
    private String workMode;     // onsite, remote, hybrid

    // ── Requirements ─────────────────────────────────────────
    private String experience;
    private String education;

    @Column(length = 500)
    private String skills;       // comma-separated

    @Column(length = 3000)
    private String jobDescription;

    // ── Compensation ─────────────────────────────────────────
    private Double salaryMin;
    private Double salaryMax;
    private String salaryType;   // Per Annum, Per Month
    private String perks;
    private Integer openings;
    private String deadline;

    // ── Status: ACTIVE, DRAFT, CLOSED ────────────────────────
    private String status = "ACTIVE";

    // ── Employer who posted this job ─────────────────────────
    @ManyToOne
    @JoinColumn(name = "employer_id", nullable = false)
    private User employer;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = "ACTIVE";
    }

    // ── Getters & Setters ─────────────────────────────────────

    public Long getId() { return id; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getJobType() { return jobType; }
    public void setJobType(String jobType) { this.jobType = jobType; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getWorkMode() { return workMode; }
    public void setWorkMode(String workMode) { this.workMode = workMode; }

    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }

    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public String getJobDescription() { return jobDescription; }
    public void setJobDescription(String jobDescription) { this.jobDescription = jobDescription; }

    public Double getSalaryMin() { return salaryMin; }
    public void setSalaryMin(Double salaryMin) { this.salaryMin = salaryMin; }

    public Double getSalaryMax() { return salaryMax; }
    public void setSalaryMax(Double salaryMax) { this.salaryMax = salaryMax; }

    public String getSalaryType() { return salaryType; }
    public void setSalaryType(String salaryType) { this.salaryType = salaryType; }

    public String getPerks() { return perks; }
    public void setPerks(String perks) { this.perks = perks; }

    public Integer getOpenings() { return openings; }
    public void setOpenings(Integer openings) { this.openings = openings; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public User getEmployer() { return employer; }
    public void setEmployer(User employer) { this.employer = employer; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}