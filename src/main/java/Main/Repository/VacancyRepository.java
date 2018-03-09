package Main.Repository;


import Main.Entities.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, String> {
    @Query("SELECT DISTINCT j.company FROM Vacancy j")
    List<String> findDistinctCompany();

    @Query("SELECT DISTINCT j.city FROM Vacancy j")
    List<String> findDistinctCity();

    @Query("SELECT DISTINCT j.language FROM Vacancy j")
    List<String> findDistinctLanguage();

    List<Vacancy> findByUrl(String url);

    List<Vacancy> findByTitleIgnoreCaseContaining(String url);


}
