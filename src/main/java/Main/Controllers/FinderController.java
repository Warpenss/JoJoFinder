package Main.Controllers;

import Main.Entities.JobSite;
import Main.Services.EpamService;
import Main.Services.LuxoftService;
import Main.Services.SoftserveService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;


@Controller
public class FinderController {
    @RequestMapping("/")
    public String run() {
        findJobs();
        return "index" ;
    }

    @ModelAttribute("jobs")
    public List<JobSite> findJobs() {
        List<JobSite> allJobs = new ArrayList<>();
        allJobs.addAll(new EpamService().collect());
        allJobs.addAll(new LuxoftService().collect());
        allJobs.addAll(new SoftserveService().collect());

        return allJobs;
    }
}
