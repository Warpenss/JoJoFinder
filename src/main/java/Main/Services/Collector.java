package Main.Services;

import Main.Entities.Company;
import Main.Entities.Vacancy;
import Main.Repository.VacancyRepository;
import Main.Tools.PageTool;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Collector {
    @Autowired
    VacancyRepository vacancyRepository;

    public static ArrayList<Vacancy> collect() throws MalformedURLException {
        ArrayList<Vacancy> vacanciesReady = new ArrayList<>();
        PageTool.initiateClient();
        ArrayList<Company> companies = CompanyList.getCompanies();
        for (Company company : companies) {
            HtmlPage page = PageTool.getPage(company.getSearchUrl());
            List<HtmlElement> vacancies = page.getByXPath(company.getTitleSelector());
            for (HtmlElement htmlElement : vacancies) {
                LocalDateTime time;
                String title;
                String url;
                String companyName;
                String city;
                String type;

                time = LocalDateTime.now();
                title = htmlElement.getTextContent();
                url = page.getFullyQualifiedUrl(htmlElement.getAttribute("href")).toString();
                companyName = company.getCompanyName();
                city = ((HtmlElement) htmlElement.getByXPath(company.getCitySelector()).get(0)).getTextContent();
                type = ((HtmlElement) htmlElement.getByXPath(company.getTypeSelector()).get(0)).getTextContent();
                vacanciesReady.add(new Vacancy(time, title, url, companyName, city, type));
            }
        }

        return vacanciesReady;
    }
}
