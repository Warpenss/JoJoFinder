package Main.Controllers;

import Main.Entities.JobSite;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
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
    private Element page = null;

    @RequestMapping("/")
    public String run() {
        try {
            page = Jsoup.connect("https://www.epam.com/careers/job-listings?sort=best_match&query=java&department=all&city=all&country=Poland").get();
        }
        catch (IOException e) {
            //
        }

        Elements vacancies = page.getElementsByAttributeValue("class", "search-result-list");

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

        return page.text();
    }

}
