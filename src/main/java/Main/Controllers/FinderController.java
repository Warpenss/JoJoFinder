package Main.Controllers;

import Main.Entities.JobSite;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


@RestController
public class FinderController {
    private List<JobSite> result = new ArrayList<>();

    @RequestMapping("/")
    public String run() {

        // EPAM stuff

        HtmlPage page = null;
        HtmlPage pageLuxoft = null;
        try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)){
            String url = "https://www.epam.com/careers/job-listings?" +
                    "sort=best_match&query=java&department=Software+Engineering&city=Kyiv&country=Ukraine";
            page = webClient.getPage(url);
            pageLuxoft = webClient.getPage("https://career.luxoft.com/job-opportunities/?arrFilter_ff%5BNAME%5D=&countryID%5B%5D=780&arrFilter_pf%5Bcities%5D%5B%5D=11&arrFilter_pf%5Bcategories%5D=95&set_filter=Y#filter-form");
            Thread.sleep(3_000);
        }
        catch (IOException | InterruptedException e) {
            //
        }

        List<HtmlElement> vacanciesEpam = page.getByXPath("//li[@class='search-result-item']" +
                "/div[contains(@class, 'position-name')]/a");
        for(HtmlElement vacancy : vacanciesEpam) {
            try {
                URL fullURL = page.getFullyQualifiedUrl(vacancy.getAttribute("href"));
                String title = vacancy.getTextContent();
                result.add(new JobSite(title, fullURL.toString()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }

        List<HtmlElement> vacanciesLuxoft = pageLuxoft.getByXPath("//a[@class='js-tracking']");

        for(HtmlElement vacancy : vacanciesLuxoft) {
            try {
                URL fullURL = pageLuxoft.getFullyQualifiedUrl(vacancy.getAttribute("href"));
                String title = vacancy.getTextContent();
                result.add(new JobSite(title, fullURL.toString()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        // Luxoft test

//        Document doc = null;
//        try {
//            doc = Jsoup.connect("https://career.luxoft.com/job-opportunities/?arrFilter_ff%5BNAME%5D=&countryID%5B%5D=780&arrFilter_pf%5Bcities%5D%5B%5D=11&arrFilter_pf%5Bcategories%5D=95&set_filter=Y#filter-form").get();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Elements vacanciesLuxoft = doc.getElementsByAttributeValue("class", "js-tracking");
//        for (Element vacancy : vacanciesLuxoft) {
//            String fullURL = "https://career.luxoft.com" + vacancy.attr("href");
//            String title = vacancy.text();
//
//            result.add(new JobSite(title, fullURL));
//
//        }


        return result.toString();
    }

}
