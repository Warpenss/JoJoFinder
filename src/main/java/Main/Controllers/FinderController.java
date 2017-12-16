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

import java.util.*;

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

        HashSet<JobSite> resultCompany = new HashSet<>();
        HashSet<JobSite> resultCity = new HashSet<>();
        HashSet<JobSite> resultLanguage = new HashSet<>();

        boolean resultFlag = false;



        System.out.println(params);

        for (Map.Entry<String, List<String>> entry : params.entrySet())
        {
            if (entry.getKey().equals("company")) {
                for (String parameter : entry.getValue()) {
                    for (JobSite job : jobsList) {
                        if (job.getCompany().equals(parameter)) {
                            resultCompany.add(job);
                        }
                    }
                }
                if (resultCompany.isEmpty()) {
                    resultFlag = true;
                }
            }

            if (entry.getKey().equals("city")) {
                for (String parameter : entry.getValue()) {
                    if (resultCompany.isEmpty()) {
                        resultCompany.addAll(jobsList);
                    }
                    for (JobSite job : resultCompany) {
                        if (job.getCity().equals(parameter)) {
                            resultCity.add(job);
                        }
                    }
                }
                if (resultCity.isEmpty()) {
                    resultFlag = true;
                }
            }


            if (entry.getKey().equals("language")) {
                for (String parameter : entry.getValue()) {
                    if (resultCity.isEmpty()) {
                        resultCity.addAll(jobsList);
                    }
                    for (JobSite job : resultCity) {
                        if (job.getLanguage().equals(parameter)) {
                            resultLanguage.add(job);
                        }
                    }

                }
                if (resultLanguage.isEmpty()) {
                    resultFlag = true;
                }
            }
        }

        if (resultLanguage.isEmpty() && !resultFlag) {
            resultLanguage.addAll(resultCity);
        }
        if (resultLanguage.isEmpty() && !resultFlag) {
            resultLanguage.addAll(resultCompany);
        }
        if (resultLanguage.isEmpty() && !resultFlag) {
            resultLanguage.addAll(jobsList);
        }

        jobsList.clear();
        jobsList.addAll(resultLanguage);

        jobsList.sort(Comparator.comparing(JobSite::getTime).reversed());
        model.addAttribute("jobs", jobsList);

        return "index";
    }
}
