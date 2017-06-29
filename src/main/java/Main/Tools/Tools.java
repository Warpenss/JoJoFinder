package Main.Tools;

import Main.Entities.JobSite;
import Main.Repository.JobRepository;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Component
public class Tools {
    @Autowired
    JobRepository jobRepository;

    static public HtmlPage getPage(String url) {
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        HtmlPage page = null;
        try {
            page = webClient.getPage(url);
            Thread.sleep(3_000);
        } catch (IOException e) {
            System.out.println("IOException while loading the page");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("InterruptedException while thread sleep");
            e.printStackTrace();
        }
        return page;
    }

    public void addVacancies(String company, List<JobSite> result, List<HtmlElement> vacancies, HtmlPage page) {
        for (HtmlElement vacancy : vacancies) {
            try {
                String fullURL = page.getFullyQualifiedUrl(vacancy.getAttribute("href")).toString();
                String title;
                if (!company.equalsIgnoreCase("Softserve")) {
                    title = vacancy.getTextContent();
                } else {
                    title = vacancy.getFirstElementChild().getFirstElementChild().getTextContent();
                }
                if (jobRepository.findOne(fullURL) == null) {
                    jobRepository.save(new JobSite(title, fullURL));
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }
}
