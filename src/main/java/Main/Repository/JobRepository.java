package Main.Repository;

import Main.Entities.JobSite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<JobSite, String> {
}
