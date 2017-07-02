package Main.Tools;


import Main.Services.EpamService;
import Main.Services.LuxoftService;
import Main.Services.SoftserveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {
    @Autowired
    EpamService epamService;
    @Autowired
    LuxoftService luxoftService;
    @Autowired
    SoftserveService softserveService;

    @Scheduled (cron = "0 27 23 * * *")
    public void schedule() {
        epamService.collect();
        luxoftService.collect();
        softserveService.collect();
    }
}
