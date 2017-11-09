package Main.Services;

import Main.Tools.PageTool;
import Main.Tools.VacancyTool;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EpamService implements SiteService {
    @Autowired
    VacancyTool vacancyTool;

    private static final String URL_KIEV_JAVA = "https://www.epam.com/careers/job-listings?" +
            "sort=best_match&" +
            "query=java&" +
            "department=Software+Engineering&" +
            "city=Kyiv&" +
            "country=Ukraine";

    private static final String URL_KRAKOW_JAVA = "https://www.epam.com/careers/job-listings?" +
            "sort=best_match&" +
            "query=java&" +
            "department=Software+Engineering&" +
            "country=Poland&" +
            "city=Krakow";

    private static final String X_PATH = "//li[@class='search-result-item']/div[contains(@class, 'position-name')]/a";

    public void collect(){
        //Get page from HtmlUnit WebClient
        HtmlPage pageKievJava = PageTool.getPage(URL_KIEV_JAVA);
        HtmlPage pageKrakowJava = PageTool.getPage(URL_KRAKOW_JAVA);

        //Get vacancies with exact xpath
        List<HtmlElement> vacanciesKievJava = pageKievJava.getByXPath(X_PATH);
        List<HtmlElement> vacanciesKrakowJava = pageKrakowJava.getByXPath(X_PATH);

        //Send vacancies to VacancyTool that adds them to the database
        vacancyTool.addVacancies("Epam", vacanciesKievJava, pageKievJava, "Kiev");
        vacancyTool.addVacancies("Epam", vacanciesKrakowJava, pageKrakowJava, "Krakow");
    }
}
