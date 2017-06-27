package Main.Services;

import Main.Entities.JobSite;
import Main.Tools.Tools;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.ArrayList;
import java.util.List;

public class LuxoftService implements SiteService {
    public List<JobSite> collect(){
        List<JobSite> result = new ArrayList<>();

        String url = "https://career.luxoft.com/job-opportunities/?" +
                "arrFilter_ff%5BNAME%5D=&" +
                "countryID%5B%5D=780&" +
                "arrFilter_pf%5Bcities%5D%5B%5D=11&" +
                "arrFilter_pf%5Bcategories%5D=95&" +
                "set_filter=Y#filter-form";

        String xPath = "//a[@class='js-tracking']";

        HtmlPage page = Tools.getPage(url);

        List<HtmlElement> vacancies = page.getByXPath(xPath);

        Tools.addVacancies("Luxoft", result, vacancies, page);

        return result;
    }
}
