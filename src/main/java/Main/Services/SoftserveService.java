package Main.Services;

import Main.Controllers.FinderController;
import Main.Entities.JobSite;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SoftserveService {
    public static List<JobSite> collect() throws IOException, InterruptedException{
        List<JobSite> result = new ArrayList<>();

        String url = "https://softserve.ua/ru/vacancies/open-vacancies/?" +
                "tax-direction=21&" +
                "tax-lang=175&" +
                "tax-country=117&" +
                "tax-city=121";

        String xPath = "//a[@class='card-vacancy-link']";

        HtmlPage page = FinderController.getPage(url);

        List<HtmlElement> vacancies = page.getByXPath(xPath);

        FinderController.addVacancies("Softserve", result, vacancies, page);

        return result;
    }
}
