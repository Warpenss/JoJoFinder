package Main.Controllers;

import Main.Entities.JobSite;
import Main.Repository.JobRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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

        //Access list of jobs in html with this attribute
        model.addAttribute("jobs", allJobs);

        return "index";
    }
}
