package Main.Services;

import Main.Entities.Company;
import Main.Entities.Vacancy;
import Main.Repository.VacancyRepository;
import Main.Tools.PageTool;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

public class Collector {
    @Autowired
    VacancyRepository vacancyRepository;

    public static void collect() {
        PageTool.initiateClient();

        List<Company> companies = CompanyList.getCompanies();
        System.out.println("Vacancies list variable created");

        for (Company company : companies) {
            System.out.println("Iteration through the list of companies");
            HtmlPage page = PageTool.getPage(company.getSearchUrl());
            System.out.println("Page variable created");
            List<HtmlElement> vacancies = page.getByXPath(company.getTitleSelector());
            System.out.println("Vacancies list variable created");
            for (HtmlElement htmlElement : vacancies) {
                LocalDateTime time;
                String title;
                String url;
                String companyName;
                String city;
                String type;

                time = LocalDateTime.now();
                System.out.println("Time variable created");
                title = htmlElement.getTextContent();
                System.out.println("Title variable created");
                url = htmlElement.getAttribute("href");
                System.out.println("Url variable created");
                companyName = company.getCompanyName();
                System.out.println("CompanyName variable created");
                city = htmlElement.getByXPath(company.getCitySelector()).get(0).toString();
                System.out.println("City variable created");
                type = htmlElement.getByXPath(company.getTypeSelector()).get(0).toString();
                System.out.println("type variable created");

                System.out.println(new Vacancy(time,title,url,companyName,city,type));
            }

        }
    }
}
