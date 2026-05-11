package com.hirehub.app.showcase;

import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class CompanyDirectory {

    private static String googleJobsQuery(String companyName) {
        String q = companyName + " careers software engineer jobs apply";
        return "https://www.google.com/search?q=" + URLEncoder.encode(q, StandardCharsets.UTF_8);
    }

    /**
     * Public career pages + LinkedIn company jobs hubs. Reddit links point to community search
     * inside hiring-related subreddits (no scraping; user leaves HireHub to read discussions).
     */
    public List<CompanyShowcase> getShowcase() {
        return List.of(
                new CompanyShowcase(
                        "Google",
                        "Search & cloud",
                        "Global roles across engineering, product, and research.",
                        "https://careers.google.com/",
                        "https://www.linkedin.com/company/google/jobs/",
                        List.of("Software Engineer", "Site Reliability Engineer", "Product Manager", "ML Engineer"),
                        "google",
                        "https://www.reddit.com/r/cscareerquestions/search/?q=Google&restrict_sr=1&sort=relevance",
                        googleJobsQuery("Google")),
                new CompanyShowcase(
                        "Microsoft",
                        "Cloud & productivity",
                        "Azure, Office, GitHub, gaming, and more.",
                        "https://careers.microsoft.com/",
                        "https://www.linkedin.com/company/microsoft/jobs/",
                        List.of("Software Engineer", "Cloud Solution Architect", "Security Engineer", "PM"),
                        "microsoft",
                        "https://www.reddit.com/r/cscareerquestions/search/?q=Microsoft&restrict_sr=1&sort=relevance",
                        googleJobsQuery("Microsoft")),
                new CompanyShowcase(
                        "Amazon",
                        "E-commerce & AWS",
                        "Retail tech, AWS, devices, and logistics engineering.",
                        "https://www.amazon.jobs/",
                        "https://www.linkedin.com/company/amazon/jobs/",
                        List.of("SDE", "Applied Scientist", "Solutions Architect", "Data Engineer"),
                        "amazon",
                        "https://www.reddit.com/r/cscareerquestions/search/?q=Amazon+AWS&restrict_sr=1&sort=relevance",
                        googleJobsQuery("Amazon")),
                new CompanyShowcase(
                        "Meta",
                        "Social & metaverse",
                        "Facebook, Instagram, WhatsApp, Reality Labs.",
                        "https://www.metacareers.com/",
                        "https://www.linkedin.com/company/meta/jobs/",
                        List.of("Software Engineer", "Research Scientist", "Production Engineer", "Designer"),
                        "meta",
                        "https://www.reddit.com/r/cscareerquestions/search/?q=Meta+Facebook&restrict_sr=1&sort=relevance",
                        googleJobsQuery("Meta")),
                new CompanyShowcase(
                        "Apple",
                        "Consumer hardware & software",
                        "Hardware, OS, services, and retail technology.",
                        "https://jobs.apple.com/",
                        "https://www.linkedin.com/company/apple/jobs/",
                        List.of("Software Engineer", "ASIC Design", "Prototyping Engineer", "Maps"),
                        "apple",
                        "https://www.reddit.com/r/cscareerquestions/search/?q=Apple&restrict_sr=1&sort=relevance",
                        googleJobsQuery("Apple")),
                new CompanyShowcase(
                        "Netflix",
                        "Streaming",
                        "Playback, recommendations, content engineering.",
                        "https://jobs.netflix.com/",
                        "https://www.linkedin.com/company/netflix/jobs/",
                        List.of("Senior Software Engineer", "Data Engineer", "Security Engineer", "Android"),
                        "netflix",
                        "https://www.reddit.com/r/cscareerquestions/search/?q=Netflix&restrict_sr=1&sort=relevance",
                        googleJobsQuery("Netflix")),
                new CompanyShowcase(
                        "NVIDIA",
                        "AI & GPUs",
                        "CUDA, robotics, autonomous vehicles, gaming.",
                        "https://nvidia.careers/",
                        "https://www.linkedin.com/company/nvidia/jobs/",
                        List.of("CUDA Engineer", "Deep Learning", "Systems Software", "Hardware"),
                        "nvidia",
                        "https://www.reddit.com/r/cscareerquestions/search/?q=NVIDIA&restrict_sr=1&sort=relevance",
                        googleJobsQuery("NVIDIA")),
                new CompanyShowcase(
                        "Adobe",
                        "Creative & document cloud",
                        "Creative Cloud, Acrobat, Experience Cloud.",
                        "https://careers.adobe.com/",
                        "https://www.linkedin.com/company/adobe/jobs/",
                        List.of("Software Engineer", "ML Engineer", "UX Engineer", "Security"),
                        "adobe",
                        "https://www.reddit.com/r/cscareerquestions/search/?q=Adobe&restrict_sr=1&sort=relevance",
                        googleJobsQuery("Adobe")),
                new CompanyShowcase(
                        "Stripe",
                        "Fintech infrastructure",
                        "Payments, Connect, Terminal, global money movement.",
                        "https://stripe.com/jobs",
                        "https://www.linkedin.com/company/stripe/jobs/",
                        List.of("Backend Engineer", "Infrastructure", "Risk", "Support Engineer"),
                        "stripe",
                        "https://www.reddit.com/r/cscareerquestions/search/?q=Stripe&restrict_sr=1&sort=relevance",
                        googleJobsQuery("Stripe")),
                new CompanyShowcase(
                        "Atlassian",
                        "Team collaboration",
                        "Jira, Confluence, Trello, Bitbucket, cloud platform.",
                        "https://www.atlassian.com/company/careers",
                        "https://www.linkedin.com/company/atlassian/jobs/",
                        List.of("Software Engineer", "SRE", "Product Design", "Support"),
                        "atlassian",
                        "https://www.reddit.com/r/cscareerquestions/search/?q=Atlassian&restrict_sr=1&sort=relevance",
                        googleJobsQuery("Atlassian")),
                new CompanyShowcase(
                        "Shopify",
                        "Commerce",
                        "Storefront, payments, logistics for merchants.",
                        "https://www.shopify.com/careers",
                        "https://www.linkedin.com/company/shopify/jobs/",
                        List.of("Backend Developer", "Data", "Mobile", "UX Research"),
                        "shopify",
                        "https://www.reddit.com/r/cscareerquestions/search/?q=Shopify&restrict_sr=1&sort=relevance",
                        googleJobsQuery("Shopify")),
                new CompanyShowcase(
                        "Uber",
                        "Mobility & delivery",
                        "Rider, Eats, freight, maps, and marketplace tech.",
                        "https://www.uber.com/careers/",
                        "https://www.linkedin.com/company/uber-com/jobs/",
                        List.of("Software Engineer", "Maps", "ML", "Infra"),
                        "uber",
                        "https://www.reddit.com/r/cscareerquestions/search/?q=Uber&restrict_sr=1&sort=relevance",
                        googleJobsQuery("Uber")),
                new CompanyShowcase(
                        "Airbnb",
                        "Travel marketplace",
                        "Search, trust, payments, and host tools.",
                        "https://careers.airbnb.com/",
                        "https://www.linkedin.com/company/airbnb/jobs/",
                        List.of("Software Engineer", "iOS", "Data Science", "Security"),
                        "airbnb",
                        "https://www.reddit.com/r/cscareerquestions/search/?q=Airbnb&restrict_sr=1&sort=relevance",
                        googleJobsQuery("Airbnb")),
                new CompanyShowcase(
                        "Spotify",
                        "Audio streaming",
                        "Playback, personalization, podcast platform.",
                        "https://www.lifeatspotify.com/jobs",
                        "https://www.linkedin.com/company/spotify/jobs/",
                        List.of("Backend Engineer", "Android", "ML", "Web"),
                        "spotify",
                        "https://www.reddit.com/r/cscareerquestions/search/?q=Spotify&restrict_sr=1&sort=relevance",
                        googleJobsQuery("Spotify")),
                new CompanyShowcase(
                        "Oracle",
                        "Enterprise software & cloud",
                        "Database, cloud infrastructure, applications.",
                        "https://careers.oracle.com/",
                        "https://www.linkedin.com/company/oracle/jobs/",
                        List.of("Software Developer", "Cloud Engineer", "Consulting", "Java"),
                        "oracle",
                        "https://www.reddit.com/r/cscareerquestions/search/?q=Oracle&restrict_sr=1&sort=relevance",
                        googleJobsQuery("Oracle")),
                new CompanyShowcase(
                        "IBM",
                        "Hybrid cloud & AI",
                        "Watson, mainframe, consulting, research.",
                        "https://www.ibm.com/careers",
                        "https://www.linkedin.com/company/ibm/jobs/",
                        List.of("Software Engineer", "Consultant", "Quantum", "Cybersecurity"),
                        "ibm",
                        "https://www.reddit.com/r/cscareerquestions/search/?q=IBM&restrict_sr=1&sort=relevance",
                        googleJobsQuery("IBM")),
                new CompanyShowcase(
                        "GitHub",
                        "Developer platform",
                        "Part of Microsoft; code hosting, Actions, Copilot.",
                        "https://github.com/about/careers",
                        "https://www.linkedin.com/company/github/jobs/",
                        List.of("Software Engineer", "Security", "Product", "Support"),
                        "github",
                        "https://www.reddit.com/r/cscareerquestions/search/?q=GitHub&restrict_sr=1&sort=relevance",
                        googleJobsQuery("GitHub")),
                new CompanyShowcase(
                        "Reddit",
                        "Social & community",
                        "The company behind this very ecosystem.",
                        "https://www.redditinc.com/careers/",
                        "https://www.linkedin.com/company/reddit-com/jobs/",
                        List.of("Software Engineer", "iOS", "Android", "Ads"),
                        "reddit",
                        "https://www.reddit.com/r/cscareerquestions/search/?q=Reddit+Inc&restrict_sr=1&sort=relevance",
                        googleJobsQuery("Reddit careers")),
                new CompanyShowcase(
                        "LinkedIn",
                        "Professional network",
                        "Feed, hiring, learning — part of Microsoft.",
                        "https://careers.linkedin.com/",
                        "https://www.linkedin.com/company/linkedin/jobs/",
                        List.of("Software Engineer", "Data Science", "Trust & Safety", "Sales Eng"),
                        "linkedin",
                        "https://www.reddit.com/r/cscareerquestions/search/?q=LinkedIn+careers&restrict_sr=1&sort=relevance",
                        googleJobsQuery("LinkedIn")));
    }
}
