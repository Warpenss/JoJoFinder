package Main.Services;

import Main.Entities.Company;
import java.util.ArrayList;

class CompanyList {
    static ArrayList<Company> getCompanies() {
        ArrayList<Company> companies = new ArrayList<>();

        //IDEAs parameter hints are really useful here

//        companies.add(new Company("EPAM",
//                "https://www.epam.com/careers/job-listings?sort=time",
//                "..//a[@class='search-result__item-name']",
//                "//a[@class='search-result__item-name']",
//                "../../strong[@class='search-result__location']",
//                "FROM_TITLE",
//                "LOAD",
//                "//a[@class='search-result__view-more']"
//
//        ));
//
//        companies.add(new Company("LUXOFT",
//                "https://career.luxoft.com/job-opportunities/",
//                "..//a[@data-offers='title']",
//                "//a[@data-offers='title']",
//                "../..//span[@itemprop='jobLocation']",
//                "../..//span[@class='label label-default']",
//                "PAGE",
//                "//a[@title='Next']"
//        ));
//
//        companies.add(new Company("SoftServe",
//                "https://career.softserveinc.com/en-us/vacancies",
//                "../../../..//a[@class='unit-href']",
//                "//h4[@class='card-courses_title']",
//                "../..//dl[2]/dd",
//                "FROM_TITLE",
//                "PAGE",
//                "//ul[@class='pagination']/li[last()]/a"
//        ));
//
//        companies.add(new Company("DOU",
//                "https://jobs.dou.ua/vacancies/?switch_lang=en",
//                "..//a[@class='vt']",
//                "//a[@class='vt']",
//                "..//span[@class='cities']",
//                "FROM_TITLE",
//                "LOAD",
//                "//div[@class='more-btn']/a"
//        ));
//
//        companies.add(new Company("djinni",
//                "https://djinni.co/jobs/?lang=en",
//                "..//a",
//                "//div[@class='list-jobs__title']/a",
//                "../..//div[@class='list-jobs__details']",
//                "FROM_TITLE",
//                "PAGE",
//                "/"
//        ));

        return companies;
    }
}
