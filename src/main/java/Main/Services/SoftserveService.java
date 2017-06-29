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

    private static final String URL = "https://softserve.ua/ru/vacancies/open-vacancies/?" +
            "tax-direction=21&" +
            "tax-lang=175&" +
            "tax-country=117&" +
            "tax-city=121";

    private static final String X_PATH = "//a[@class='card-vacancy-link']";

    public void collect()  {
        HtmlPage page = PageTool.getPage(URL);

        List<HtmlElement> vacancies = page.getByXPath(X_PATH);

        vacancyTool.addVacancies("Softserve", vacancies, page);
    }
}
