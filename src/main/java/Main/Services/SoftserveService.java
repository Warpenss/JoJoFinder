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
        HtmlPage pageKievJava = PageTool.getPage(URL);

        HtmlCheckBoxInput kievCheckBox = pageKievJava.getHtmlElementById("city--Kyiv");
        kievCheckBox.setChecked(true);

        HtmlCheckBoxInput javaCheckBoxKiev = pageKievJava.getHtmlElementById("technology--Java");
        javaCheckBoxKiev.setChecked(true);

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<HtmlElement> vacanciesKievJava = pageKievJava.getByXPath(X_PATH);

        vacancyTool.addVacancies("Softserve", vacanciesKievJava, pageKievJava, "Kiev");


        HtmlPage pagePolandJava = PageTool.getPage(URL);

        HtmlCheckBoxInput polandCheckBox = pagePolandJava.getHtmlElementById("country--Poland");
        polandCheckBox.setChecked(true);

        HtmlCheckBoxInput javaCheckBoxPoland = pagePolandJava.getHtmlElementById("technology--Java");
        javaCheckBoxPoland.setChecked(true);

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<HtmlElement> vacanciesKrakowJava = pagePolandJava.getByXPath(X_PATH);

        vacancyTool.addVacancies("Softserve", vacanciesKrakowJava, pagePolandJava, "Poland");
    }
}
