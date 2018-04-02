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

    @Query("SELECT DISTINCT j.location FROM Vacancy j")
    List<String> findDistinctLocation();

    @Query("SELECT DISTINCT j.type FROM Vacancy j")
    List<String> findDistinctType();

    List<Vacancy> findByUrl(String url);

    List<Vacancy> findByTitleIgnoreCaseContaining(String url);

    List<Vacancy> findByLocation(String url);


}
