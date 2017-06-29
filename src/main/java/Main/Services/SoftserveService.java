package Main.Services;

import Main.Tools.PageTool;
import Main.Tools.VacancyTool;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class SoftserveService implements SiteService {
    @Autowired
    VacancyTool vacancyTool;

    public void collect()  {
        String url = "https://softserve.ua/ru/vacancies/open-vacancies/?" +
                "tax-direction=21&" +
                "tax-lang=175&" +
                "tax-country=117&" +
                "tax-city=121";

        String xPath = "//a[@class='card-vacancy-link']";

        HtmlPage page = PageTool.getPage(url);

        List<HtmlElement> vacancies = page.getByXPath(xPath);

        vacancyTool.addVacancies("Softserve", vacancies, page);
    }
}
