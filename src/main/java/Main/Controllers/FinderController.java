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
@SuppressWarnings("unused")
public class FinderController {

    private static List<JobSite> jobSites = new ArrayList<>();
    private HtmlPage page = null;

    @RequestMapping("/")
    public String run() {
        try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)){
            String url = "https://www.epam.com/careers/job-listings?" +
                    "sort=best_match&query=java&department=Software+Engineering&city=Kyiv&country=Ukraine";
            page = webClient.getPage(url);
            Thread.sleep(3_0000);
        }
        catch (IOException | InterruptedException e) {
            //
        }

        List<JobSite> result = new ArrayList<>();

        List<HtmlElement> vacancies = page.getByXPath("//li[@class='search-result-item']" +
                "/div[contains(@class, 'position-name')]/a");
        for(HtmlElement vacancy : vacancies) {
            try {
                URL fullURL = page.getFullyQualifiedUrl(vacancy.getAttribute("href"));
                String title = vacancy.getTextContent();
                result.add(new JobSite(title, fullURL.toString()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }


        return result.toString();
    }

}
