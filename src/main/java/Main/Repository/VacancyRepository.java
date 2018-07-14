package Main.Repository;


import Main.Entities.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, String> {
    @Query("SELECT DISTINCT v.company FROM Vacancy v")
    List<String> findDistinctCompany();

    @Query("SELECT DISTINCT v.location FROM Vacancy v")
    List<String> findDistinctLocation();

    @Query(value = "SELECT DISTINCT t.type FROM Types t", nativeQuery = true)
    List<String> findDistinctType();

    List<Vacancy> findByUrl(String url);

    List<Vacancy> findByLocation(String url);

    List<Vacancy> findByTitleIgnoreCaseContaining(String url);

    List<Vacancy> findByTitleIgnoreCaseContainingAndCompanyIn(String title, List<String> companies);
    List<Vacancy> findByTitleIgnoreCaseContainingAndLocationIn(String title, List<String> location);
    List<Vacancy> findByTitleIgnoreCaseContainingAndTypeIn(String title, List<String> types);

    List<Vacancy> findByTitleIgnoreCaseContainingAndCompanyInAndLocationIn(String title, List<String> companies, List<String> location);
    List<Vacancy> findByTitleIgnoreCaseContainingAndCompanyInAndTypeIn(String title, List<String> companies, List<String> types);
    List<Vacancy> findByTitleIgnoreCaseContainingAndLocationInAndTypeIn(String title, List<String> location, List<String> types);

    List<Vacancy> findByTitleIgnoreCaseContainingAndCompanyInAndLocationInAndTypeIn(String title, List<String> companies, List<String> location, List<String> types);
}
