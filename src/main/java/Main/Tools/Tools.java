package Main.Tools;

import Main.Entities.JobSite;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Tools {
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
