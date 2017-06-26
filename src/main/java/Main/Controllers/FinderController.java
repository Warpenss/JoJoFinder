package Main.Controllers;

import Main.Entities.JobSite;
import Main.Services.EpamService;
import Main.Services.LuxoftService;
import Main.Services.SoftserveService;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


@RestController
public class FinderController {
    @RequestMapping("/")
    public String run() throws IOException, InterruptedException {
        String result = "";
        result += EpamService.collect().toString()
                + LuxoftService.collect().toString()
                + SoftserveService.collect().toString();
        return result;
    }

    static public HtmlPage getPage(String url) throws IOException, InterruptedException {
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        HtmlPage page = webClient.getPage(url);
        Thread.sleep(3_000);
        return page;
    }

    public static void addVacancies(String company, List<JobSite> result, List<HtmlElement> vacancies, HtmlPage page) {
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
