package Main.Services;

import Main.Entities.Company;
import Main.Entities.Vacancy;
import Main.Repository.VacancyRepository;
import Main.Tools.PageTool;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.*;

public class Collector {
    @Autowired
    VacancyRepository vacancyRepository;

    public ArrayList<Vacancy> collect() throws MalformedURLException {
        ArrayList<Vacancy> vacanciesReady = new ArrayList<>();
        PageTool.initiateClient();
        ArrayList<Company> companies = CompanyList.getCompanies();
        for (Company company : companies) {
            HtmlPage page = PageTool.getPage(company.getSearchUrl());

            // In development
//            if (company.getCompanyName().equals("EPAM")) {
//                while (true) {
//                    try {
//                        page = ((HtmlElement) page.getByXPath("//a[@class='search-result__view-more']").get(0)).click();
//                        System.out.println("Clicked");
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        Thread.sleep(2000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    boolean pageEnd = page.getByXPath("//a[@class='search-result__view-more']" +
//                            "[contains(@style,'display: none')]").size() > 0;
//                    if (pageEnd) {
//                        System.out.println("end");
//                        break;
//                    }
//                }
//            }

            List<HtmlElement> vacancies = page.getByXPath(company.getTitleSelector());
            for (HtmlElement htmlElement : vacancies) {
                LocalDateTime time;
                String title;
                String url;
                String companyName;
                String location;
                String typeSelector;
                String type;

                time = LocalDateTime.now();
                title = htmlElement.getTextContent();
                url = page.getFullyQualifiedUrl(htmlElement.getAttribute("href")).toString();
                companyName = company.getCompanyName();
                location = StringUtils.substringBefore(((HtmlElement) htmlElement.getByXPath(company.getCitySelector())
                        .get(0)).getTextContent(), ",");
                typeSelector = ((HtmlElement) htmlElement.getByXPath(company.getTypeSelector()).get(0)).getTextContent();
                type = plainType(typeSelector);

                vacanciesReady.add(new Vacancy(time, title, url, companyName, location, type));
            }
        }

        return vacanciesReady;
    }


    private String plainType(String rawType) {
        String plainType = "Other";

        if (StringUtils.containsIgnoreCase(rawType, "JavaScript")) {
            plainType = "JavaScript";
        } else if (StringUtils.containsIgnoreCase(rawType, "Java") ||
                (StringUtils.containsIgnoreCase(rawType, "Android"))) {
            plainType = "Java";
        } else if (StringUtils.containsIgnoreCase(rawType, "C++")) {
            plainType = "C++";
        } else if (StringUtils.containsIgnoreCase(rawType, "C#")) {
            plainType = "C#";
        } else if (StringUtils.containsIgnoreCase(rawType, ".NET")) {
            plainType = ".NET";
        } else if (StringUtils.containsIgnoreCase(rawType, "Python")) {
            plainType = "Python";
        } else if (StringUtils.containsIgnoreCase(rawType, "UI")) {
            plainType = "UI";
        } else if (StringUtils.containsIgnoreCase(rawType, "iOS")) {
            plainType = "iOS";
        } else if (StringUtils.containsIgnoreCase(rawType, "Manager")) {
            plainType = "Manager";
        } else if (StringUtils.containsIgnoreCase(rawType, "Test")) {
            plainType = "Testing";
        } else if (StringUtils.containsIgnoreCase(rawType, "Consultant")) {
            plainType = "Consultant";
        } else if (StringUtils.containsIgnoreCase(rawType, "Assistant")) {
            plainType = "Assistant";
        } else if (StringUtils.containsIgnoreCase(rawType, "SQL")) {
            plainType = "SQL ";
        } else if (StringUtils.containsIgnoreCase(rawType, "Analyst")) {
            plainType = "Analyst ";
        } else if (StringUtils.containsIgnoreCase(rawType, "Big Data")) {
            plainType = "Big Data";
        } else if (StringUtils.containsIgnoreCase(rawType, "Recruiter")) {
            plainType = "Recruiter";
        } else if (StringUtils.containsIgnoreCase(rawType, "Solution")) {
            plainType = "Solution Engineer";
        } else if (StringUtils.containsIgnoreCase(rawType, "Director")) {
            plainType = "Director";
        } else if (StringUtils.containsIgnoreCase(rawType, "Designer")) {
            plainType = "Designer";
        } else if (StringUtils.containsIgnoreCase(rawType, "DevOps")) {
            plainType = "DevOps";
        } else if (StringUtils.containsIgnoreCase(rawType, "Front-end")) {
            plainType = "Front-end";
        } else if (StringUtils.containsIgnoreCase(rawType, "Automation")) {
            plainType = "Automation";
        } else if (StringUtils.containsIgnoreCase(rawType, "Accountant")) {
            plainType = "Accountant";
        } else if (StringUtils.containsIgnoreCase(rawType, "HR") ||
                StringUtils.containsIgnoreCase(rawType, "Human Resources")) {
            plainType = "HR";
        } else if (StringUtils.containsIgnoreCase(rawType, "Security")) {
            plainType = "Security";
        } else if (StringUtils.containsIgnoreCase(rawType, "QA")) {
            plainType = "QA";
        } else if (StringUtils.containsIgnoreCase(rawType, "Android")) {
            plainType = "Android";
        } else if (StringUtils.containsIgnoreCase(rawType, "PHP")) {
            plainType = "PHP";
        }


        return plainType;

    }
}
