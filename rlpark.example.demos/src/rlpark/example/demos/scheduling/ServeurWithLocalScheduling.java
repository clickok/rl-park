package rlpark.example.demos.scheduling;

import java.io.IOException;
import java.util.Iterator;

import rltoys.experiments.scheduling.interfaces.JobDoneEvent;
import rltoys.experiments.scheduling.network.ServerScheduler;
import zephyr.plugin.core.api.signals.Listener;

public class ServeurWithLocalScheduling {
  public static void main(String[] args) throws IOException {
    ServerScheduler scheduler = new ServerScheduler(ServerScheduler.DefaultPort, 2);
    Iterator<? extends Runnable> jobList = LocalScheduling.createJobList();
    Listener<JobDoneEvent> jobDoneListener = LocalScheduling.createJobDoneListener();
    scheduler.queue().add(jobList, jobDoneListener);
    scheduler.runAll();
  }
}
