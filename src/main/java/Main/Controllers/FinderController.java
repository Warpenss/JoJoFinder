package Main.Controllers;

import Main.Entities.JobSite;
import Main.Repository.JobRepository;

import Main.Services.EpamService;
import Main.Services.LuxoftService;
import Main.Services.SoftserveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Controller
public class FinderController {

    @Autowired
    private EpamService epamService;
    @Autowired
    private LuxoftService luxoftService;
    @Autowired
    private SoftserveService softserveService;


    @Autowired
    JobRepository jobRepository;

    //Catches the "/" request, adds jobs attribute and redirect to index.html
    @RequestMapping("/")
    public String index(Model model) {
        epamService.collect();
        luxoftService.collect();
        softserveService.collect();


        //Get all vacancies from database
        List<JobSite> allJobs = jobRepository.findAll();


        for (JobSite jobSite : allJobs) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMM dd HH:mm");
            LocalDateTime now = LocalDateTime.now();
            String formated = now.format(formatter);
            LocalDateTime time = LocalDateTime.parse(formated, formatter);

            jobRepository.save(new JobSite(time, jobSite.getTitle(), jobSite.getUrl(), jobSite.getCompany(), "Kiev", "Java"));
        }
        allJobs.sort(Comparator.comparing(JobSite::getTime).reversed());
        //Access list of jobs in html with this attribute
        model.addAttribute("jobs", allJobs);

        return "index";
    }
}
