package Main.Tools;

import Main.Entities.JobSite;
import Main.Repository.JobRepository;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.util.List;

@Component
public class VacancyTool {
    @Autowired
    JobRepository jobRepository;

    public void addVacancies(String company, List<HtmlElement> vacancies, HtmlPage page) {
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
