package Main.Services;

import Main.Entities.Company;
import Main.Entities.Vacancy;
import Main.Repository.VacancyRepository;
import Main.Tools.PageTool;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.geonames.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class Collector {

    private final VacancyRepository vacancyRepository;

    @Autowired
    public Collector(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

    public ArrayList<Vacancy> collect() {
        ArrayList<Vacancy> vacanciesReady = new ArrayList<>();
        ArrayList<Company> companies = CompanyList.getCompanies();

        for (Company company : companies) {
            PageTool.initiateClient();

            System.out.println(company.getCompanyName());
            List<HtmlElement> vacancies = new ArrayList<>();
            HtmlPage page = PageTool.getPage(company.getSearchUrl());

            if (company.getPaginationType().equals("LOAD")) {
                for (int i = 0; i < 50; i++) {
                    page = paginationClick(page, company.getPaginationSelector());
                }
                vacancies = page.getByXPath(company.getTitleSelector());
            } else if (company.getPaginationType().equals("PAGE")) {
                // You must be logged in to see pages on djinni
                if (company.getCompanyName().equals("djinni")) {
                    for (int i = 2; i < 10; i++) {
                        vacancies.addAll(page.getByXPath(company.getTitleSelector()));
                        page = PageTool.getPage("https://djinni.co/jobs/?lang=en&page=" + i);
                    }
                } else {
                    for (int i = 0; i < 10; i++) {
                        vacancies.addAll(page.getByXPath(company.getTitleSelector()));
                        page = paginationClick(page, company.getPaginationSelector());
                    }
                }
            }

            for (HtmlElement htmlElement : vacancies) {
                try {
                    LocalDateTime time;
                    String title;
                    String url;
                    String companyName;
                    String location;
                    String type;

                    url = page.getFullyQualifiedUrl(((HtmlElement) htmlElement.getByXPath(company.getUrlSelector())
                            .get(0)).getAttribute("href")).toString();
                    System.out.println(url);

                    if (vacancyRepository.findByUrl(url).size() == 0) {
                        time = LocalDateTime.now();
                        System.out.println(time);

                        title = htmlElement.getTextContent().trim();
                        if (containsNonEnglish(title)) {
                            String language = detectLanguage(title);
                            title = translateTitle(title, language);
                        }
                        System.out.println(title);

                        companyName = company.getCompanyName();
                        System.out.println(companyName);

                        location = "Undefined";
                        List<HtmlElement> list = htmlElement.getByXPath(company.getCitySelector());
                        if (!list.isEmpty()) {
                            location = getLocation(company, list);
                        }
                        System.out.println(location);

                        if (company.getTypeSelector().equals("FROM_TITLE")) {
                            type = title;
                        } else {
                            type = ((HtmlElement) htmlElement.getByXPath(company.getTypeSelector()).get(0)).getTextContent();
                        }
                        type = plainType(type);
                        System.out.println(type);

                        vacanciesReady.add(new Vacancy(time, title, url, companyName, location, type));
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            PageTool.closeClient();
        }

        return vacanciesReady;
    }

    private boolean containsNonEnglish(String text) {
        return !(text.matches("[\\p{Punct}\\p{Space}\\p{Digit}a-zA-Z –]+$"));
    }

    private String apiKey = "";

    private String detectLanguage(String text) {
        try {
            text = text.replaceAll("[()]", "");
            String nonEnglishPart = text.replaceAll("[\\p{Punct}\\p{Digit}a-zA-Z]", "");
            System.out.println(nonEnglishPart);
            String detectApiUrl = "https://translate.yandex.net/api/v1.5/tr.json/detect?";
            detectApiUrl += "&key=" + apiKey;
            nonEnglishPart = URLEncoder.encode(nonEnglishPart, "UTF-8");
            detectApiUrl += "&text=" + nonEnglishPart;
            String json = IOUtils.toString(new URL(detectApiUrl), "UTF-8");
            JSONObject jsonObject = (JSONObject) JSONValue.parseWithException(json);
            String language = jsonObject.get("lang").toString();
            if (language.isEmpty()) {
                return "en-";
            } else {
                return language + "-";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Can't parse");
        }
        return "";
    }

    private String translateTitle(String text, String language) {
        try {
            String translateApiUrl = "https://translate.yandex.net/api/v1.5/tr.json/translate?";
            translateApiUrl += "&key=" + apiKey;
            translateApiUrl += "&lang=" + language + "en";
            String titleEncoded = URLEncoder.encode(text, "UTF-8");
            translateApiUrl += "&text=" + titleEncoded;
            String json = IOUtils.toString(new URL(translateApiUrl), "UTF-8");
            JSONObject jsonObject = (JSONObject) JSONValue.parseWithException(json);
            text = ((JSONArray) jsonObject.get("text")).get(0).toString();
            return text;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Can't parse");
        }
        return text;
    }

    private HtmlPage paginationClick(HtmlPage page, String paginationSelector) {
        try {
            List<HtmlElement> list = page.getByXPath(paginationSelector);

            if (!list.isEmpty()) {
                System.out.println("Before click");
                page = list.get(0).click();
                System.out.println("After click");
            } else {
                System.out.println("Empty");
            }

            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO exception");
        }
        return page;
    }

    private String getLocation(Company company, List<HtmlElement> list) {
        String location;
        if (company.getCompanyName().equals("djinni")) {
            location = StringUtils.substringAfterLast((list.get(0)).getTextContent(), "\u00a0");
            location = StringUtils.substringBefore(location, ",").trim();
            if (location.contains(".")) {
                location = StringUtils.substringAfter(location, ".").trim();
            }
        } else {
            location = StringUtils.substringBefore((list.get(0)).getTextContent(), ",");
            if (location.contains(".")) {
                location = StringUtils.substringAfter(location, ".").trim();
            }
        }
        if (vacancyRepository.findByLocation(location).size() == 0) {
            location = plainCity(location);
        }

        return location;
    }

    private String plainCity(String rawCity) {
        String plainCity = rawCity;

        try {
            WebService.setUserName("warpenss"); // add your username here
            ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
            searchCriteria.setName(rawCity);
            ToponymSearchResult searchResult = WebService.search(searchCriteria);
            List<Toponym> searchResultToponyms = searchResult.getToponyms();
            if (!searchResultToponyms.isEmpty()) {
                plainCity = searchResultToponyms.get(0).getName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return plainCity;
    }

    private String plainType(String rawType) {
        String plainType = "Other";

        if (StringUtils.containsIgnoreCase(rawType, "JavaScript")) {
            plainType = "JavaScript";
        } else if (StringUtils.containsIgnoreCase(rawType, "Full Stack") ||
                StringUtils.containsIgnoreCase(rawType, "Fullstack") ||
                StringUtils.containsIgnoreCase(rawType, "Full-stack"))    {
            plainType = "Full Stack";
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
        } else if (StringUtils.containsIgnoreCase(rawType, "UI ")) {
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
        } else if (StringUtils.containsIgnoreCase(rawType, "Front-end") ||
                StringUtils.containsIgnoreCase(rawType, "Front end") ||
                StringUtils.containsIgnoreCase(rawType, "Frontend")) {
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
        } else if (StringUtils.containsIgnoreCase(rawType, "Node.js") ||
                StringUtils.containsIgnoreCase(rawType, "NodeJS")) {
            plainType = "Node.js";
        } else if (StringUtils.containsIgnoreCase(rawType, "C ")) {
            plainType = "C";
        } else if (StringUtils.containsIgnoreCase(rawType, "Scala")) {
            plainType = "Scala";
        } else if (StringUtils.containsIgnoreCase(rawType, "Ruby") ||
                StringUtils.containsIgnoreCase(rawType, "RoR")) {
            plainType = "Ruby";
        } else if (StringUtils.containsIgnoreCase(rawType, "Data Scientist")) {
            plainType = "Data Scientist";
        } else if (StringUtils.containsIgnoreCase(rawType, "TypeScript")) {
            plainType = "TypeScript";
        } else if (StringUtils.containsIgnoreCase(rawType, "React")) {
            plainType = "React";
        } else if (StringUtils.containsIgnoreCase(rawType, "Unity 3D") ||
                StringUtils.containsIgnoreCase(rawType, "Unity3D")) {
            plainType = "Unity3D";
        } else if (StringUtils.containsIgnoreCase(rawType, "1С") ||
                StringUtils.containsIgnoreCase(rawType, "1 С")) {
            plainType = "1С";
        } else if (StringUtils.containsIgnoreCase(rawType, "Blockchain")) {
            plainType = "Blockchain";
        } else if (StringUtils.containsIgnoreCase(rawType, "Back-end") ||
                StringUtils.containsIgnoreCase(rawType, "Back end") ||
                StringUtils.containsIgnoreCase(rawType, "Backend")) {
            plainType = "Back-end";
        } else if (StringUtils.containsIgnoreCase(rawType, "Service Desk") ||
                StringUtils.containsIgnoreCase(rawType, "HelpDesk")) {
            plainType = "HelpDesk";
        } else if (StringUtils.containsIgnoreCase(rawType, "Go ") ||
                StringUtils.containsIgnoreCase(rawType, "Golang")) {
            plainType = "Go";
        } else if (StringUtils.containsIgnoreCase(rawType, "R&D")) {
            plainType = "R&D";
        } else if (StringUtils.containsIgnoreCase(rawType, "Administrator")) {
            plainType = "Administrator";
        } else if (StringUtils.containsIgnoreCase(rawType, "ELT")) {
            plainType = "ELT";
        } else if (StringUtils.containsIgnoreCase(rawType, "Coordinator")) {
            plainType = "Coordinator";
        } else if (StringUtils.containsIgnoreCase(rawType, "Delphi")) {
            plainType = "Delphi";
        } else if (StringUtils.containsIgnoreCase(rawType, "Splunk")) {
            plainType = "Splunk";
        } else if (StringUtils.containsIgnoreCase(rawType, "Tableau")) {
            plainType = "Tableau";
        } else if (StringUtils.containsIgnoreCase(rawType, "Business Intelligence")) {
            plainType = "Business Intelligence";
        } else if (StringUtils.containsIgnoreCase(rawType, "Data Warehouse") ||
                StringUtils.containsIgnoreCase(rawType, "DWH")) {
            plainType = "Data Warehouse";
        } else if (StringUtils.containsIgnoreCase(rawType, "Teacher")) {
            plainType = "Teacher";
        } else if (StringUtils.containsIgnoreCase(rawType, "Training and Development")) {
            plainType = "Training and Development";
        } else if (StringUtils.containsIgnoreCase(rawType, "Experience Design")) {
            plainType = "Experience Design";
        } else if (StringUtils.containsIgnoreCase(rawType, "OPS")) {
            plainType = "OPS";
        } else if (StringUtils.containsIgnoreCase(rawType, "Support")) {
            plainType = "Support";
        } else if (StringUtils.containsIgnoreCase(rawType, "Quality Control") ||
                StringUtils.containsIgnoreCase(rawType, "QС")) {
            plainType = "Quality Control";
        } else if (StringUtils.containsIgnoreCase(rawType, "Procurement")) {
            plainType = "Procurement";
        } else if (StringUtils.containsIgnoreCase(rawType, "UX")) {
            plainType = "UX";
        } else if (StringUtils.containsIgnoreCase(rawType, "Hadoop")) {
            plainType = "Hadoop";
        } else if (StringUtils.containsIgnoreCase(rawType, "Database administrator") ||
                StringUtils.containsIgnoreCase(rawType, "DBA")) {
            plainType = "Database administrator";
        } else if (StringUtils.containsIgnoreCase(rawType, "Data Engineer")) {
            plainType = "Data Engineer";
        } else if (StringUtils.containsIgnoreCase(rawType, "Marketing")) {
            plainType = "Marketing";
        }


        return plainType;

    }
}

