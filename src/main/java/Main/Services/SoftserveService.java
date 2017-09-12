package Main.Services;

import Main.Tools.PageTool;
import Main.Tools.VacancyTool;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SoftserveService implements SiteService {
    @Autowired
    VacancyTool vacancyTool;

    private static final String URL = "https://my.softserve.ua/vacancies";

    private static final String X_PATH = "//a[@class='unit-href']";

    public void collect()  {
        HtmlPage page = PageTool.getPage(URL);

        HtmlCheckBoxInput kievCheckBox = page.getHtmlElementById("city--Kyiv");
        kievCheckBox.setChecked(true);

        HtmlCheckBoxInput javaCheckBox = page.getHtmlElementById("technology--Java");
        javaCheckBox.setChecked(true);

        try {
            Thread.sleep(3_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<HtmlElement> vacancies = page.getByXPath(X_PATH);

        vacancyTool.addVacancies("Softserve", vacancies, page);
    }
}
