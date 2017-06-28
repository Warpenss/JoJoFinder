package Main.Controllers;

import Main.Entities.JobSite;
import Main.Services.EpamService;
import Main.Services.LuxoftService;
import Main.Services.SoftserveService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;


@Controller
public class FinderController {
    @Autowired
    EpamService epamService;
    @Autowired
    LuxoftService luxoftService;
    @Autowired
    SoftserveService softserveService;

    @RequestMapping("/")
    public String index(Model model) {
        List<JobSite> allJobs = new ArrayList<>();
        allJobs.addAll(epamService.collect());
        allJobs.addAll(luxoftService.collect());
        allJobs.addAll(softserveService.collect());

        model.addAttribute("jobs", allJobs);
        return "index";
    }
}
