package Main.Services;

import Main.Entities.JobSite;
import Main.Tools.PageTool;
import Main.Tools.VacancyTool;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EpamService implements SiteService {
    @Autowired
    VacancyTool vacancyTool;

    public void collect(){
        List<JobSite> result = new ArrayList<>();

        String url = "https://www.epam.com/careers/job-listings?" +
                "sort=best_match&" +
                "query=java&" +
                "department=Software+Engineering&" +
                "city=Kyiv&" +
                "country=Ukraine";

        String xPath = "//li[@class='search-result-item']/div[contains(@class, 'position-name')]/a";

        HtmlPage page = PageTool.getPage(url);

        List<HtmlElement> vacancies = page.getByXPath(xPath);

        vacancyTool.addVacancies("Epam", result, vacancies, page);
    }
}
