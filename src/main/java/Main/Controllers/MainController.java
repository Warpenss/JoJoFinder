package Main.Controllers;

import Main.Entities.Vacancy;

import Main.Repository.VacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class MainController {
    private final VacancyRepository vacancyRepository;

    @Autowired
    public MainController(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

    //Catches the "/" request, adds vacancies attribute and redirect to index.html
    @RequestMapping("/")
    public String index(Model model) {
        //Get all vacancies from database
        List<Vacancy> vacancies = vacancyRepository.findAll();
        List<String> companies = vacancyRepository.findDistinctCompany();
        List<String> locations = vacancyRepository.findDistinctLocation();
        List<String> types = vacancyRepository.findDistinctType();

        vacancies.sort(Comparator.comparing(Vacancy::getTime).reversed());
        Collections.sort(companies);
        Collections.sort(locations);
        Collections.sort(types);

        model.addAttribute("vacancies", vacancies);
        model.addAttribute("companies", companies);
        model.addAttribute("locations", locations);
        model.addAttribute("types", types);

        return "index";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String submit(@RequestParam MultiValueMap<String, String> params, Model model) {

        List<Vacancy> vacancies = vacancyRepository.findByTitleIgnoreCaseContaining(params.getFirst("title"));

        HashSet<Vacancy> resultCompany = new HashSet<>();
        HashSet<Vacancy> resultLocation = new HashSet<>();
        HashSet<Vacancy> resultType = new HashSet<>();

        boolean resultFlag = false;

        System.out.println(params);

        for (Map.Entry<String, List<String>> entry : params.entrySet())
        {
            if (entry.getKey().equals("company")) {
                for (String parameter : entry.getValue()) {
                    for (Vacancy vacancy : vacancies) {
                        if (vacancy.getCompany().equals(parameter)) {
                            resultCompany.add(vacancy);
                        }
                    }
                }
                if (resultCompany.isEmpty()) {
                    resultFlag = true;
                }
            }

            if (entry.getKey().equals("location")) {
                for (String parameter : entry.getValue()) {
                    if (resultCompany.isEmpty()) {
                        resultCompany.addAll(vacancies);
                    }
                    for (Vacancy vacancy : resultCompany) {
                        if (vacancy.getLocation().equals(parameter)) {
                            resultLocation.add(vacancy);
                        }
                    }
                }
                if (resultLocation.isEmpty()) {
                    resultFlag = true;
                }
            }


            if (entry.getKey().equals("type")) {
                for (String parameter : entry.getValue()) {
                    if (resultLocation.isEmpty()) {
                        resultLocation.addAll(vacancies);
                    }
                    for (Vacancy vacancy : resultLocation) {
                        if (vacancy.getType().equals(parameter)) {
                            resultType.add(vacancy);
                        }
                    }

                }
                if (resultType.isEmpty()) {
                    resultFlag = true;
                }
            }
        }

        if (resultType.isEmpty() && !resultFlag) {
            resultType.addAll(resultLocation);
        }
        if (resultType.isEmpty() && !resultFlag) {
            resultType.addAll(resultCompany);
        }
        if (resultType.isEmpty() && !resultFlag) {
            resultType.addAll(vacancies);
        }

        vacancies.clear();
        vacancies.addAll(resultType);
        List<String> companies = vacancyRepository.findDistinctCompany();
        List<String> locations = vacancyRepository.findDistinctLocation();
        List<String> types = vacancyRepository.findDistinctType();

        vacancies.sort(Comparator.comparing(Vacancy::getTime).reversed());
        Collections.sort(companies);
        Collections.sort(locations);
        Collections.sort(types);

        model.addAttribute("vacancies", vacancies);
        model.addAttribute("companies", companies);
        model.addAttribute("locations", locations);
        model.addAttribute("types", types);

        return "index";
    }

//    @RequestMapping("/test")
//    public String test(Model model) {
//        ArrayList<Vacancy> vacancies = new Collector(vacancyRepository).collect();
//        model.addAttribute("vacancies", vacancies);
//        return "test";
//    }
}
