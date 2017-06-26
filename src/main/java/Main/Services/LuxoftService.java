package Main.Services;


import Main.Controllers.FinderController;
import Main.Entities.JobSite;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LuxoftService extends JobPicker {

    @Override
    public List<JobSite> collect() throws IOException, InterruptedException{
        List<JobSite> result = new ArrayList<>();

        String url = "https://www.epam.com/careers/job-listings?" +
                "sort=best_match&" +
                "query=java&" +
                "department=Software+Engineering&" +
                "city=Kyiv&" +
                "country=Ukraine";

        String xPath = "//li[@class='search-result-item']/div[contains(@class, 'position-name')]/a";

        HtmlPage page = FinderController.getPage(url);

        List<HtmlElement> vacancies = page.getByXPath(xPath);

        FinderController.addVacancies("Epam", result, vacancies, page);

        return result;
    }
}
