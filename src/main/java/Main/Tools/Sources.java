package Main.Tools;

import Main.Entities.Source;

import java.util.ArrayList;

public class Sources {
    public static ArrayList<Source> getSources() {
        ArrayList<Source> sources = new ArrayList<>();

        //IDEAs parameter hints are really useful here

        sources.add(new Source("EPAM",
                "FROM_SOURCE_NAME",
                "https://www.epam.com/careers/job-listings?sort=time",
                "..//a[@class='search-result__item-name']",
                "//a[@class='search-result__item-name']",
                "../../strong[@class='search-result__location']",
                "FROM_TITLE",
                "LOAD",
                "//a[@class='search-result__view-more']"
        ));

        sources.add(new Source("LUXOFT",
                "FROM_SOURCE_NAME",
                "https://career.luxoft.com/job-opportunities/",
                "..//a[@data-offers='title']",
                "//a[@data-offers='title']",
                "../..//span[@itemprop='jobLocation']",
                "../..//span[@class='label label-default']",
                "PAGE",
                "//a[@title='Next']"
        ));

        sources.add(new Source("SoftServe",
                "FROM_SOURCE_NAME",
                "https://career.softserveinc.com/en-us/vacancies",
                "../../../..//a[@class='unit-href']",
                "//h4[@class='card-courses_title']",
                "../..//dl[2]/dd",
                "FROM_TITLE",
                "PAGE",
                "//ul[@class='pagination']/li[last()]/a"
        ));

        sources.add(new Source("DOU",
                "..//a[@class='company']",
                "https://jobs.dou.ua/vacancies/?switch_lang=en",
                "..//a[@class='vt']",
                "//a[@class='vt']",
                "..//span[@class='cities']",
                "FROM_TITLE",
                "LOAD",
                "//div[@class='more-btn']/a"
        ));

        sources.add(new Source("djinni",
                "../..//div[@class='list-jobs__details']",
                "https://djinni.co/jobs/?lang=en",
                "..//a",
                "//div[@class='list-jobs__title']/a",
                "../..//div[@class='list-jobs__details']",
                "FROM_TITLE",
                "PAGE",
                "/"
        ));

        return sources;
    }
}
