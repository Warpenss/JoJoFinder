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

        sources.add(new Source("WORK.ua",
                "../../span[1]",
                "https://www.work.ua/jobs-it/",
                "..//a",
                "//h2[@class='add-bottom-sm']/a",
                "../../span[contains(text(),'· ')]/following-sibling::span[1]",
                "FROM_TITLE",
                "PAGE",
                "//ul[@class='pagination hidden-xs']/li[last()]/a"
        ));

        sources.add(new Source("rabota.ua",
                "../../p[@class='f-vacancylist-companyname fd-merchant f-text-dark-bluegray']/a",
                "https://rabota.ua/вакансии/в_интернете/украина",
                "..//a",
                "//h3[@class='fd-beefy-gunso f-vacancylist-vacancytitle']/a",
                "../../div/p[@class='fd-merchant']",
                "FROM_TITLE",
                "PAGE",
                "//dd[@class='nextbtn']/a"
        ));

        sources.add(new Source("glassdoor",
                "../../../div[@class='flexbox empLoc']/div",
                "https://www.glassdoor.com/Job/ukraine-jobs-SRCH_IL.0,7_IN244.htm",
                "..//a",
                "//li[@class='jl']/div/div[@class='flexbox']/div/a[@class='jobLink']",
                "../../../div[@class='flexbox empLoc']/div/span[@class='subtle loc']",
                "FROM_TITLE",
                "PAGE",
                "//div[@class='pagingControls cell middle']/ul/li[@class='next']/a"
        ));

        //Will fix this mess later
        sources.add(new Source("glassdoor",
                "../../../div[@class='flexbox empLoc']/div",
                "https://www.glassdoor.com/Job/poland-jobs-SRCH_IL.0,6_IN193.htm",
                "..//a",
                "//li[@class='jl']/div/div[@class='flexbox']/div/a[@class='jobLink']",
                "../../../div[@class='flexbox empLoc']/div/span[@class='subtle loc']",
                "FROM_TITLE",
                "PAGE",
                "//div[@class='pagingControls cell middle']/ul/li[@class='next']/a"
        ));
        sources.add(new Source("glassdoor",
                "../../../div[@class='flexbox empLoc']/div",
                "https://www.glassdoor.com/Job/norway-jobs-SRCH_IL.0,6_IN180.htm",
                "..//a",
                "//li[@class='jl']/div/div[@class='flexbox']/div/a[@class='jobLink']",
                "../../../div[@class='flexbox empLoc']/div/span[@class='subtle loc']",
                "FROM_TITLE",
                "PAGE",
                "//div[@class='pagingControls cell middle']/ul/li[@class='next']/a"
        ));
        sources.add(new Source("glassdoor",
                "../../../div[@class='flexbox empLoc']/div",
                "https://www.glassdoor.com/Job/singapore-jobs-SRCH_IL.0,9_IN217.htm",
                "..//a",
                "//li[@class='jl']/div/div[@class='flexbox']/div/a[@class='jobLink']",
                "../../../div[@class='flexbox empLoc']/div/span[@class='subtle loc']",
                "FROM_TITLE",
                "PAGE",
                "//div[@class='pagingControls cell middle']/ul/li[@class='next']/a"
        ));
        sources.add(new Source("glassdoor",
                "../../../div[@class='flexbox empLoc']/div",
                "https://www.glassdoor.com/Job/australia-jobs-SRCH_IL.0,9_IN16.htm",
                "..//a",
                "//li[@class='jl']/div/div[@class='flexbox']/div/a[@class='jobLink']",
                "../../../div[@class='flexbox empLoc']/div/span[@class='subtle loc']",
                "FROM_TITLE",
                "PAGE",
                "//div[@class='pagingControls cell middle']/ul/li[@class='next']/a"
        ));

        return sources;
    }
}
