package Main.Repository;


import Main.Entities.JobSite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<JobSite, String> {
    List<JobSite> findByUrl(String url);

    List<JobSite> findByTitleIgnoreCaseContaining(String url);
}
