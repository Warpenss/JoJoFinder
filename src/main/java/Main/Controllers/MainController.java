package Main.Controllers;

import Main.Entities.Company;
import Main.Entities.Vacancy;

import Main.Repository.VacancyRepository;
import Main.Services.CompanyList;
import Main.Tools.PageCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView index(@RequestParam(value = "page", required = false) Integer page) {
        final int PAGE_SIZE = 40;
        ModelAndView modelAndView = new ModelAndView("index");
        int currentPageNumber = (page == null || page < 1) ? 0 : page - 1;
        Pageable pageable = new PageRequest(currentPageNumber, PAGE_SIZE, Sort.Direction.DESC, "time");

        Page<Vacancy> vacancies = vacancyRepository.findAll(pageable);
        PageCounter pageCounter = new PageCounter(vacancies.getTotalPages(), vacancies.getNumber() + 1);
        List<String> companies = vacancyRepository.findDistinctCompany();
        List<String> locations = vacancyRepository.findDistinctLocation();
        List<String> types = vacancyRepository.findDistinctType();

        Collections.sort(companies);
        Collections.sort(locations);
        Collections.sort(types);

        modelAndView.addObject("vacancies", vacancies);
        modelAndView.addObject("companies", companies);
        modelAndView.addObject("locations", locations);
        modelAndView.addObject("types", types);
        modelAndView.addObject("pageCounter", pageCounter);

        return modelAndView;
    }

//    @RequestMapping(value = "/search", method = RequestMethod.GET)
//    public String submit(@RequestParam MultiValueMap<String, String> params, Model model) {
//        List<Vacancy> vacancies;
//        if (params.getFirst("title") == null){
//            vacancies = vacancyRepository.findAll();
//        } else {
//            vacancies = vacancyRepository.findByTitleIgnoreCaseContaining(params.getFirst("title"), null);
//        }
//
//        HashSet<Vacancy> resultCompany = new HashSet<>();
//        HashSet<Vacancy> resultLocation = new HashSet<>();
//        HashSet<Vacancy> resultType = new HashSet<>();
//
//        boolean resultFlag = false;
//
//        System.out.println(params);
//
//        for (Map.Entry<String, List<String>> entry : params.entrySet())
//        {
//            if (entry.getKey().equals("company")) {
//                for (String parameter : entry.getValue()) {
//                    for (Vacancy vacancy : vacancies) {
//                        if (vacancy.getCompany().equals(parameter)) {
//                            resultCompany.add(vacancy);
//                        }
//                    }
//                }
//                if (resultCompany.isEmpty()) {
//                    resultFlag = true;
//                }
//            }
//
//            if (entry.getKey().equals("location")) {
//                for (String parameter : entry.getValue()) {
//                    if (resultCompany.isEmpty()) {
//                        resultCompany.addAll(vacancies);
//                    }
//                    for (Vacancy vacancy : resultCompany) {
//                        if (vacancy.getLocation().equals(parameter)) {
//                            resultLocation.add(vacancy);
//                        }
//                    }
//                }
//                if (resultLocation.isEmpty()) {
//                    resultFlag = true;
//                }
//            }
//
//
//            if (entry.getKey().equals("type")) {
//                for (String parameter : entry.getValue()) {
//                    if (resultLocation.isEmpty()) {
//                        resultLocation.addAll(vacancies);
//                    }
//                    for (Vacancy vacancy : resultLocation) {
//                        if (vacancy.getType().equals(parameter)) {
//                            resultType.add(vacancy);
//                        }
//                    }
//
//                }
//                if (resultType.isEmpty()) {
//                    resultFlag = true;
//                }
//            }
//        }
//
//        if (resultType.isEmpty() && !resultFlag) {
//            resultType.addAll(resultLocation);
//        }
//        if (resultType.isEmpty() && !resultFlag) {
//            resultType.addAll(resultCompany);
//        }
//        if (resultType.isEmpty() && !resultFlag) {
//            resultType.addAll(vacancies);
//        }
//
//        vacancies.clear();
//        vacancies.addAll(resultType);
//        List<String> companies = vacancyRepository.findDistinctCompany();
//        List<String> locations = vacancyRepository.findDistinctLocation();
//        List<String> types = vacancyRepository.findDistinctType();
//
//        vacancies.sort(Comparator.comparing(Vacancy::getTime).reversed());
//        Collections.sort(companies);
//        Collections.sort(locations);
//        Collections.sort(types);
//
//        model.addAttribute("vacancies", vacancies);
//        model.addAttribute("companies", companies);
//        model.addAttribute("locations", locations);
//        model.addAttribute("types", types);
//
//        return "index";
//    }

//    @RequestMapping("/test")
//    public String test(Model model) {
//        ArrayList<Company> companies = CompanyList.getCompanies();
//        ArrayList<Vacancy> vacancies = new Collector(vacancyRepository).collect(companies);
//        model.addAttribute("vacancies", vacancies);
//        return "test";
//    }
}
