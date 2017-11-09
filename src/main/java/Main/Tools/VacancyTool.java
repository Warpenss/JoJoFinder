package Main.Tools;

import Main.Entities.JobSite;
import Main.Repository.JobRepository;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.List;


//This tool can handle all kinds of site structures to get vacancy title and url. Also add it to the database
@Component
public class VacancyTool {
    @Autowired
    JobRepository jobRepository;


    public void addVacancies(String company, List<HtmlElement> vacancies, HtmlPage page, String city) {
        for (HtmlElement vacancy : vacancies) {
            try {
                String fullURL = page.getFullyQualifiedUrl(vacancy.getAttribute("href")).toString();
                String title;
                //Softserve have different site structure
                if (!company.equalsIgnoreCase("Softserve")) {
                    title = vacancy.getTextContent();
                } else {
                    //Get inside the main vacancy element to get title of Softserve vacancy
                    title = ((HtmlElement) vacancy.getByXPath("div[@class='vacancy-container']" +
                            "/div[@class='vacancy-title clearfix']" +
                            "/h4[@class='card-courses_title']").get(0)).asText();
                }

                if (jobRepository.findByUrl(fullURL).size() == 0) {
                    LocalDateTime time = LocalDateTime.now();
                    jobRepository.save(new JobSite(time, title, fullURL, company, city, "Java"));
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }
}
