package Main.Services;

import Main.Tools.PageTool;
import Main.Tools.VacancyTool;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class LuxoftService implements SiteService {
    @Autowired
    VacancyTool vacancyTool;

    private static final String URL_KIEV_JAVA = "https://career.luxoft.com/job-opportunities/?" +
            "arrFilter_ff%5BNAME%5D=&" +
            "countryID%5B%5D=780&" +
            "arrFilter_pf%5Bcities%5D%5B%5D=11&" +
            "arrFilter_pf%5Bcategories%5D=95&" +
            "set_filter=Y#filter-form";

    private static final String URL_KRAKOW_JAVA = "https://career.luxoft.com/job-opportunities/?" +
            "arrFilter_ff%5BNAME%5D=&" +
            "countryID%5B%5D=778&" +
            "arrFilter_pf%5Bcities%5D%5B%5D=13368&" +
            "arrFilter_pf%5Bcategories%5D=95&" +
            "set_filter=Y#filter-form";

    private static final String X_PATH = "//a[@class='js-tracking']";

    public void collect(){
        HtmlPage pageKievJava = PageTool.getPage(URL_KIEV_JAVA);
        HtmlPage pageKrakowJava = PageTool.getPage(URL_KRAKOW_JAVA);

        List<HtmlElement> vacanciesKievJava = pageKievJava.getByXPath(X_PATH);
        List<HtmlElement> vacanciesKrakowJava = pageKrakowJava.getByXPath(X_PATH);

        vacancyTool.addVacancies("Luxoft", vacanciesKievJava, pageKievJava, "Kiev");
        vacancyTool.addVacancies("Luxoft", vacanciesKrakowJava, pageKrakowJava, "Krakow");
    }
}
