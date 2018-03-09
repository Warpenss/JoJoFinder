package Main.Services;

import Main.Entities.Company;

import java.util.ArrayList;

public class CompanyList {
    static public ArrayList<Company> getCompanies() {
        ArrayList<Company> companies = new ArrayList<>();

        String companyName;
        String searchUrl;
        String titleSelector;
        String citySelector;
        String typeSelector;

        companyName = "EPAM";
        searchUrl = "https://www.epam.com/careers/job-listings";
        titleSelector = "search-result__item-name";
        citySelector = "search-result__location";
        typeSelector = "search-result__item-name";

        companies.add(new Company(companyName, searchUrl, titleSelector, citySelector, typeSelector));

        return companies;
    }
}
