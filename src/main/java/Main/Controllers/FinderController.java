package Main.Controllers;

import Main.Entities.Vacancy;

import Main.Repository.VacancyRepository;
import Main.Services.Collector;
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
    VacancyRepository vacancyRepository;

    //Catches the "/" request, adds jobs attribute and redirect to index.html
    @RequestMapping("/")
    public String index(Model model) {
        //Get all vacancies from database
        List<Vacancy> allJobs = vacancyRepository.findAll();

        allJobs.sort(Comparator.comparing(Vacancy::getTime).reversed());
        //Access list of jobs in html with this attribute
        model.addAttribute("jobs", allJobs);

        model.addAttribute("companies", vacancyRepository.findDistinctCompany());
        model.addAttribute("cities", vacancyRepository.findDistinctCity());
        model.addAttribute("languages", vacancyRepository.findDistinctLanguage());

        return "index";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String submit(@RequestParam MultiValueMap<String, String> params, Model model) {

        List<Vacancy> jobsList = vacancyRepository.findByTitleIgnoreCaseContaining(params.getFirst("title"));

        HashSet<Vacancy> resultCompany = new HashSet<>();
        HashSet<Vacancy> resultCity = new HashSet<>();
        HashSet<Vacancy> resultLanguage = new HashSet<>();

        boolean resultFlag = false;



        System.out.println(params);

        for (Map.Entry<String, List<String>> entry : params.entrySet())
        {
            if (entry.getKey().equals("company")) {
                for (String parameter : entry.getValue()) {
                    for (Vacancy job : jobsList) {
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
                    for (Vacancy job : resultCompany) {
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
                    for (Vacancy job : resultCity) {
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

        jobsList.sort(Comparator.comparing(Vacancy::getTime).reversed());
        model.addAttribute("jobs", jobsList);
        model.addAttribute("companies", vacancyRepository.findDistinctCompany());
        model.addAttribute("cities", vacancyRepository.findDistinctCity());
        model.addAttribute("languages", vacancyRepository.findDistinctLanguage());

        return "index";
    }

    @RequestMapping("/test")
    public String test(Model model) {
        Collector.collect();
        return "test";
    }
}
