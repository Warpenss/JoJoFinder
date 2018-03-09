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
    private EpamService epamService;
    @Autowired
    private LuxoftService luxoftService;
    @Autowired
    private SoftserveService softserveService;

    @Scheduled (cron = "0 0/30 * * * *")
    public void schedule() {
        PageTool.initiateClient();

        epamService.collect();
        System.out.println("Epam collected");
        luxoftService.collect();
        System.out.println("Luxoft collected");
        softserveService.collect();
        System.out.println("Softserve collected");

        PageTool.closeClient();
    }
}
