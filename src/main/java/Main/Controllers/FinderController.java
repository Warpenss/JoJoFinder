package Main.Controllers;

import Main.Entities.JobSite;

import Main.Repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
public class FinderController {
    @Autowired
    JobRepository jobRepository;

    //Catches the "/" request, adds jobs attribute and redirect to index.html
    @RequestMapping("/")
    public String index(Model model) {
        //Get all vacancies from database
        List<JobSite> allJobs = jobRepository.findAll();

        allJobs.sort(Comparator.comparing(JobSite::getTime).reversed());
        //Access list of jobs in html with this attribute
        model.addAttribute("jobs", allJobs);

        return "index";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String submit(@RequestParam MultiValueMap<String, String> params, Model model) {

        List<JobSite> jobsList = jobRepository.findByTitleIgnoreCaseContaining(params.getFirst("title"));




        jobsList.sort(Comparator.comparing(JobSite::getTime).reversed());
        model.addAttribute("jobs", jobsList);

        return "index";
    }
}
