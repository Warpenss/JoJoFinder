package Main.Tools;

import Main.Entities.JobSite;
import Main.Repository.JobRepository;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.util.List;


//This tool can handle all kinds of site structures to get vacancy title and url. Also add it to the database
@Component
public class VacancyTool {
    @Autowired
    JobRepository jobRepository;

    public void addVacancies(String company, List<HtmlElement> vacancies, HtmlPage page) {
        for (HtmlElement vacancy : vacancies) {
            try {
                String fullURL = page.getFullyQualifiedUrl(vacancy.getAttribute("href")).toString();
                String title;
                //Softserve have different site structure
                if (!company.equalsIgnoreCase("Softserve")) {
                    title = vacancy.getTextContent();
                } else {
                    //Get inside the main vacancy element to get title of Softserve vacancy
                    title = vacancy.getFirstElementChild().getFirstElementChild().getTextContent();
                }
                //If there is no vacancy in database with such URL - create new
                if (jobRepository.findOne(fullURL) == null) {
                    jobRepository.save(new JobSite(title, fullURL, company, "Kiev", "Java"));
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }
}
