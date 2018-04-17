package Main.Controllers;

import Main.Entities.Vacancy;
import Main.Repository.VacancyRepository;
import Main.Tools.PageCounter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
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
    @RequestMapping(value = "/", method = RequestMethod.GET)
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

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView index(@RequestParam MultiValueMap<String, String> params) {

        final int PAGE_SIZE = 40;
        ModelAndView modelAndView = new ModelAndView("index");

        Integer page = null;
        if (params.getFirst("page") != null) {
            page = Integer.parseInt(params.getFirst("page"));
        }
        int currentPageNumber = ( page == null || page < 1) ? 0 : page - 1;

        List<Vacancy> vacancyList;
        if (params.getFirst("title") == null){
            vacancyList = vacancyRepository.findAll();
        } else {
            vacancyList = vacancyRepository.findByTitleIgnoreCaseContaining(params.getFirst("title"));
        }

        HashSet<Vacancy> resultCompany = new HashSet<>();
        HashSet<Vacancy> resultLocation = new HashSet<>();
        HashSet<Vacancy> resultType = new HashSet<>();

        boolean resultFlag = false;

        System.out.println(params);

        for (Map.Entry<String, List<String>> entry : params.entrySet())
        {
            if (entry.getKey().equals("company")) {
                for (String parameter : entry.getValue()) {
                    for (Vacancy vacancy : vacancyList) {
                        if (vacancy.getCompany().equalsIgnoreCase(parameter)) {
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
                        resultCompany.addAll(vacancyList);
                    }
                    for (Vacancy vacancy : resultCompany) {
                        if (vacancy.getLocation().equalsIgnoreCase(parameter)) {
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
                        resultLocation.addAll(vacancyList);
                    }
                    for (Vacancy vacancy : resultLocation) {
                        if (vacancy.getType().equalsIgnoreCase(parameter)) {
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
            resultType.addAll(vacancyList);
        }

        vacancyList.clear();
        vacancyList.addAll(resultType);
        List<String> companies = vacancyRepository.findDistinctCompany();
        List<String> locations = vacancyRepository.findDistinctLocation();
        List<String> types = vacancyRepository.findDistinctType();

        vacancyList.sort(Comparator.comparing(Vacancy::getTime).reversed());
        Collections.sort(companies);
        Collections.sort(locations);
        Collections.sort(types);

        int minimum = currentPageNumber * PAGE_SIZE;
        int maximum = (minimum + PAGE_SIZE) > vacancyList.size() ? vacancyList.size() : minimum + PAGE_SIZE;

        Page<Vacancy> vacancies = new PageImpl<>(vacancyList.subList(minimum, maximum), new PageRequest(currentPageNumber, PAGE_SIZE), vacancyList.size());
        PageCounter pageCounter = new PageCounter(vacancies.getTotalPages(), vacancies.getNumber() + 1);

        modelAndView.addObject("vacancies", vacancies);
        modelAndView.addObject("companies", companies);
        modelAndView.addObject("locations", locations);
        modelAndView.addObject("types", types);
        modelAndView.addObject("pageCounter", pageCounter);

        return modelAndView;
    }

//    @RequestMapping("/test")
//    public String test(Model model) {
//        ArrayList<Source> sourses = Sources.getSources();
//        ArrayList<Vacancy> vacancies = new Collector(vacancyRepository).collect(sourses);
//        model.addAttribute("vacancies", vacancies);
//        return "test";
//    }
}
