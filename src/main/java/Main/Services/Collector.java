package Main.Services;

import Main.Entities.Company;
import Main.Entities.Vacancy;
import Main.Tools.PageTool;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.lang3.StringUtils;
import org.geonames.*;

import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.*;

public class Collector {
    public ArrayList<Vacancy> collect() throws MalformedURLException {
        ArrayList<Vacancy> vacanciesReady = new ArrayList<>();
        PageTool.initiateClient();
        ArrayList<Company> companies = CompanyList.getCompanies();
        for (Company company : companies) {
            System.out.println(company.getCompanyName());
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
            if (vacancies.isEmpty()) {
                System.out.println("Empty");
            }
            for (HtmlElement htmlElement : vacancies) {
                LocalDateTime time;
                String title;
                String url;
                String companyName;
                String location;
                String type;

                time = LocalDateTime.now();
                System.out.println(time);

                title = htmlElement.getTextContent().trim();
                System.out.println(title);

                url = page.getFullyQualifiedUrl(((HtmlElement) htmlElement.getByXPath(company.getUrlSelector())
                        .get(0)).getAttribute("href")).toString();
                System.out.println(url);

                companyName = company.getCompanyName();
                System.out.println(companyName);

                if (company.getCompanyName().equals("djinni")) {
                    location = StringUtils.substringAfterLast(((HtmlElement) htmlElement.getByXPath(company.getCitySelector())
                            .get(0)).getTextContent(), "\u00a0").trim();
                } else {
                    location = StringUtils.substringBefore(((HtmlElement) htmlElement.getByXPath(company.getCitySelector())
                            .get(0)).getTextContent(), ",").trim();
                }
                location = plainCity(location);
                System.out.println(location);

                type = ((HtmlElement) htmlElement.getByXPath(company.getTypeSelector()).get(0)).getTextContent();
                type = plainType(type);
                System.out.println(type);

                vacanciesReady.add(new Vacancy(time, title, url, companyName, location, type));
            }
        }

        return vacanciesReady;
    }

    private String plainCity(String rawCity) {
        String plainCity = rawCity;

        try {
            WebService.setUserName("warpenss"); // add your username here
            ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
            searchCriteria.setName(rawCity);
            ToponymSearchResult searchResult = WebService.search(searchCriteria);
            plainCity = searchResult.getToponyms().get(0).getName();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return plainCity;
    }

    private String plainType(String rawType) {
        String plainType = "Other";

        if (StringUtils.containsIgnoreCase(rawType, "JavaScript")) {
            plainType = "JavaScript";
        } else if (StringUtils.containsIgnoreCase(rawType, "Java")) {
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
        } else if (StringUtils.containsIgnoreCase(rawType, "Sales")) {
            plainType = "Sales";
        } else if (StringUtils.containsIgnoreCase(rawType, "Angular")) {
            plainType = "Angular";
        } else if (StringUtils.containsIgnoreCase(rawType, "Node.js")) {
            plainType = "Node.js";
        } else if (StringUtils.containsIgnoreCase(rawType, " C ")) {
            plainType = "C";
        } else if (StringUtils.containsIgnoreCase(rawType, "Scala")) {
            plainType = "Scala";
        }

        return plainType;

    }
}
