package Main.Services;

import Main.Entities.Company;
import Main.Entities.Vacancy;
import Main.Repository.VacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

public class Collector {

    @Autowired
    VacancyRepository vacancyRepository;

    public void collect() {
        List<Company> companies = CompanyList.getCompanies();
        LocalDateTime time;
        String title;
        String url;
        String compan;
        String city;
        String language;

        for (Company company : companies) {
            vacancyRepository.save(new Vacancy());
        }
    }
}
