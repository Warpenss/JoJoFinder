package Main.Controllers;

import Main.Entities.JobSite;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
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
            webClient.getCurrentWindow().setInnerHeight(60000);
            String url = "https://www.epam.com/careers/job-listings?sort=best_match&query=java&department=all&city=all&country=all";
            page = webClient.getPage(url);
            Thread.sleep(3_0000);
        }
        catch (IOException | InterruptedException e) {
            //
        }


//        Elements vacancies = page.getElementsByAttributeValue("class", "search-result-list");
//
//        vacancies.forEach(vacancy -> {
//            String title = vacancy.getElementsByAttributeValue("class", "search-result-item").text();
//            String url = vacancy.getElementsByAttributeValue("link", "href").text();
//
//            System.out.println(title);
//            System.out.println(url);
//
//            jobSites.add(new JobSite(title, url));
//        });
//
//        jobSites.forEach(System.out::println);

        return page.asText();
    }

}
