package Main.Controllers;

import Main.Entities.Source;
import Main.Entities.Vacancy;
import Main.Repository.VacancyRepository;
import Main.Services.Collector;
import Main.Tools.PageCounter;

import Main.Tools.Sources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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
        ModelAndView modelAndView = new ModelAndView("index");

        final int PAGE_SIZE = 40;
        int page = 0;
        try {
            page = Integer.parseInt(params.getFirst("page"));
        } catch (NumberFormatException e) {
            //
        }
        int currentPageNumber = page < 1 ? 0 : page - 1;

        String title = params.getFirst("title");
        List<String> companies = params.get("company");
        List<String> locations = params.get("location");
        List<String> types = params.get("type");

        List<Vacancy> vacancyList = new ArrayList<>();
        if (companies == null && locations == null && types == null) {
            vacancyList = vacancyRepository.findByTitleIgnoreCaseContaining(title);
        }
        if (companies != null && locations == null && types == null) {
            vacancyList = vacancyRepository.findByTitleIgnoreCaseContainingAndCompanyIn(title, companies);
        }
        if (companies == null && locations != null && types == null) {
            vacancyList = vacancyRepository.findByTitleIgnoreCaseContainingAndLocationIn(title, locations);
        }
        if (companies == null && locations == null && types != null) {
            vacancyList = vacancyRepository.findByTitleIgnoreCaseContainingAndTypeIn(title, types);
        }
        if (companies != null && locations != null && types == null) {
            vacancyList = vacancyRepository.findByTitleIgnoreCaseContainingAndCompanyInAndLocationIn(title, companies, locations);
        }
        if (companies != null && locations == null && types != null) {
            vacancyList = vacancyRepository.findByTitleIgnoreCaseContainingAndCompanyInAndTypeIn(title, companies, types);
        }
        if (companies == null && locations != null && types != null) {
            vacancyList = vacancyRepository.findByTitleIgnoreCaseContainingAndLocationInAndTypeIn(title, locations, types);
        }
        if (companies != null && locations != null && types != null) {
            vacancyList = vacancyRepository.findByTitleIgnoreCaseContainingAndCompanyInAndLocationInAndTypeIn(title, companies, locations, types);
        }

        List<String> allCompanies = vacancyRepository.findDistinctCompany();
        List<String> allLocations = vacancyRepository.findDistinctLocation();
        List<String> allTypes = vacancyRepository.findDistinctType();

        vacancyList.sort(Comparator.comparing(Vacancy::getTime).reversed());
        Collections.sort(allCompanies);
        Collections.sort(allLocations);
        Collections.sort(allTypes);

        int minimum = currentPageNumber * PAGE_SIZE;
        int maximum = (minimum + PAGE_SIZE) > vacancyList.size() ? vacancyList.size() : minimum + PAGE_SIZE;

        Page<Vacancy> vacancies = new PageImpl<>(vacancyList.subList(minimum, maximum), new PageRequest(currentPageNumber, PAGE_SIZE), vacancyList.size());
        PageCounter pageCounter = new PageCounter(vacancies.getTotalPages(), vacancies.getNumber() + 1);

        modelAndView.addObject("vacancies", vacancies);
        modelAndView.addObject("companies", allCompanies);
        modelAndView.addObject("locations", allLocations);
        modelAndView.addObject("types", allTypes);
        modelAndView.addObject("pageCounter", pageCounter);

        return modelAndView;
    }

    @RequestMapping("/test")
    public String test(Model model) {
//
//        for (int i = 1; i < 1000; i++) {
//            try {
//                WebClient webClient = new WebClient();
//
//                webClient = new WebClient(BrowserVersion.BEST_SUPPORTED);
//                webClient.getOptions().setUseInsecureSSL(true);
//                webClient.getOptions().setCssEnabled(false);
//                webClient.getOptions().setThrowExceptionOnScriptError(false);
//                webClient.getOptions().setPopupBlockerEnabled(true);
//                webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
//                webClient.getOptions().setJavaScriptEnabled(false);
//                System.out.println("New webClient is created");
//
//                HtmlPage page = webClient.getPage("https://www.glassdoor.com/Job/poland-jobs-SRCH_IL.0,6_IN193.htm");
//                List<HtmlElement> vacancies = page.getByXPath("//li[@class='jl']/div/div[@class='flexbox']/div/a[@class='jobLink']");
//                System.out.println(i);
//
//                if (vacancies.isEmpty()) {
//                    System.out.println(page.asXml());
//                    System.out.println(i);
//                    break;
//                }
//
//                final List<WebWindow> windows = webClient.getWebWindows();
//                for (final WebWindow wd : windows) {
//                    wd.getJobManager().removeAllJobs();
//                }
//
//                for (TopLevelWindow topLevelWindow : webClient.getTopLevelWindows()) {
//                    topLevelWindow.close();
//                    System.out.println(topLevelWindow.toString() + " - window is closed");
//                }
//                webClient.close();
//                System.gc();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }


        ArrayList<Source> sources = Sources.getSources();
        new Collector(vacancyRepository).collect(sources);


        return "index";
    }
}
