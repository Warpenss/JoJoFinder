package Main.Repository;


import Main.Entities.JobSite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<JobSite, String> {
    @Query("SELECT DISTINCT j.company FROM JobSite j")
    List<String> findDistinctCompany();

    @Query("SELECT DISTINCT j.city FROM JobSite j")
    List<String> findDistinctCity();

    @Query("SELECT DISTINCT j.language FROM JobSite j")
    List<String> findDistinctLanguage();

    List<JobSite> findByUrl(String url);

    List<JobSite> findByTitleIgnoreCaseContaining(String url);


}
