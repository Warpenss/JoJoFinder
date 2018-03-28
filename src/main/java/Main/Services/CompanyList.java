package Main.Services;

import Main.Entities.Company;
import Main.Entities.Vacancy;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class CompanyList {
    static public ArrayList<Company> getCompanies() {
        ArrayList<Company> companies = new ArrayList<>();

        //IDEAs parameter hints are really useful here

        companies.add(new Company("EPAM",
                "https://www.epam.com/careers/job-listings?sort=time",
                "..//a[@class='search-result__item-name']",
                "//a[@class='search-result__item-name']",
                "../../strong[@class='search-result__location']",
                "..//a[@class='search-result__item-name']"
        ));

        companies.add(new Company("LUXOFT",
                "https://career.luxoft.com/job-opportunities/",
                "..//a[@data-offers='title']",
                "//a[@data-offers='title']",
                "../..//span[@itemprop='jobLocation']",
                "../..//span[@class='label label-default']"
        ));

        companies.add(new Company("SoftServe",
                "https://career.softserveinc.com/en-us/vacancies",
                "../../../..//a[@class='unit-href']",
                "//h4[@class='card-courses_title']",
                "../..//dl[2]/dd",
                "..//h4[@class='card-courses_title']"
        ));

        companies.add(new Company("DOU",
                "https://jobs.dou.ua/vacancies/?switch_lang=en",
                "..//a[@class='vt']",
                "//a[@class='vt']",
                "..//span[@class='cities']",
                "..//a[@class='vt']"
        ));

        companies.add(new Company("djinni",
                "https://djinni.co/jobs/?lang=en",
                "..//a",
                "//div[@class='list-jobs__title']/a",
                "../..//div[@class='list-jobs__details']",
                "..//a"
        ));

        return companies;
    }
}
