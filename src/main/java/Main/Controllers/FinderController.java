package Main.Controllers;

import Main.Entities.JobSite;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


@RestController
public class FinderController {

    @RequestMapping("/")
    public String run() throws IOException, InterruptedException {
        List<JobSite> result = new ArrayList<>();

        String urlEpam = "https://www.epam.com/careers/job-listings?" +
                "sort=best_match&" +
                "query=java&" +
                "department=Software+Engineering&" +
                "city=Kyiv&" +
                "country=Ukraine";
        String urlLuxoft = "https://career.luxoft.com/job-opportunities/?" +
                "arrFilter_ff%5BNAME%5D=&" +
                "countryID%5B%5D=780&" +
                "arrFilter_pf%5Bcities%5D%5B%5D=11&" +
                "arrFilter_pf%5Bcategories%5D=95&" +
                "set_filter=Y#filter-form";
        String urlSoftserve = "https://softserve.ua/ru/vacancies/open-vacancies/?" +
                "tax-direction=21&" +
                "tax-lang=175&" +
                "tax-country=117&" +
                "tax-city=121";

        String xPathEpam = "//li[@class='search-result-item']/div[contains(@class, 'position-name')]/a";
        String xPathLuxoft = "//a[@class='js-tracking']";
        String xPathSoftserve = "//a[@class='card-vacancy-link']";

        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        HtmlPage pageEpam = webClient.getPage(urlEpam);
        HtmlPage pageLuxoft = webClient.getPage(urlLuxoft);
        HtmlPage pageSoftServe = webClient.getPage(urlSoftserve);
        Thread.sleep(3_000);

        List<HtmlElement> vacanciesEpam = pageEpam.getByXPath(xPathEpam);
        List<HtmlElement> vacanciesLuxoft = pageLuxoft.getByXPath(xPathLuxoft);
        List<HtmlElement> vacanciesSoftserve = pageSoftServe.getByXPath(xPathSoftserve);

        addVacancies("Epam", result, vacanciesEpam, pageEpam);
        addVacancies("Luxoft", result, vacanciesLuxoft, pageLuxoft);
        addVacancies("Softserve", result, vacanciesSoftserve, pageLuxoft);

        return result.toString();
    }

    private void addVacancies(String company, List<JobSite> result, List<HtmlElement> vacancies, HtmlPage page) {
        for (HtmlElement vacancy : vacancies) {
            try {
                URL fullURL = page.getFullyQualifiedUrl(vacancy.getAttribute("href"));
                String title;
                if (!company.equalsIgnoreCase("Softserve")) {
                    title = vacancy.getTextContent();
                } else {
                    title = vacancy.getFirstElementChild().getFirstElementChild().getTextContent();
                }
                result.add(new JobSite(title, fullURL.toString()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

}
