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
                "//a[@class='search-result__item-name']",
                "../../strong[@class='search-result__location']",
                "..//a[@class='search-result__item-name']"
        ));

        companies.add(new Company("LUXOFT",
                "https://career.luxoft.com/job-opportunities/",
                "//a[@class='search-result__item-name']",
                "../../strong[@class='search-result__location']",
                "..//a[@class='search-result__item-name']"
        ));

        return companies;
    }
}
