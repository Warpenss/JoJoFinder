package Main.Tools;

import Main.Repository.VacancyRepository;
import Main.Services.Collector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class Scheduler {
    private final VacancyRepository vacancyRepository;

    @Autowired
    public Scheduler(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

    @Scheduled (cron = "0 0/30 * * * *")
    public void schedule() {
        new Collector(vacancyRepository).collect();
    }
}
