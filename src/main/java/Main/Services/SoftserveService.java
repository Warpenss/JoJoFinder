package Main.Services;

import Main.Entities.JobSite;
import Main.Tools.Tools;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class SoftserveService implements SiteService {
    @Autowired
    Tools tools;

    public void collect()  {
        List<JobSite> result = new ArrayList<>();

        String url = "https://softserve.ua/ru/vacancies/open-vacancies/?" +
                "tax-direction=21&" +
                "tax-lang=175&" +
                "tax-country=117&" +
                "tax-city=121";

        String xPath = "//a[@class='card-vacancy-link']";

        HtmlPage page = Tools.getPage(url);

        List<HtmlElement> vacancies = page.getByXPath(xPath);

        tools.addVacancies("Softserve", result, vacancies, page);
    }
}
