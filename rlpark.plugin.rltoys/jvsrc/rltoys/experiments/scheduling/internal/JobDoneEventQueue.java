package rltoys.experiments.scheduling.internal;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import rltoys.experiments.scheduling.interfaces.JobDoneEvent;
import zephyr.plugin.core.api.signals.Listener;
import zephyr.plugin.core.api.signals.Signal;

public class JobDoneEventQueue {
  class JobEventInternal {
    final Listener<JobDoneEvent> listener;
    final JobDoneEvent jobDoneEvent;

    JobEventInternal(Listener<JobDoneEvent> listener, JobDoneEvent event) {
      this.listener = listener;
      this.jobDoneEvent = event;
    }
  }

  public final Signal<JobDoneEvent> onJobDone = new Signal<JobDoneEvent>();
  private final BlockingQueue<JobEventInternal> queue = new LinkedBlockingQueue<JobEventInternal>();
  private final Thread currentThread = new Thread(new Runnable() {
    @Override
    public void run() {
      processEvents();
    }
  });
  private boolean terminated = false;

  public JobDoneEventQueue() {
    currentThread.setDaemon(true);
    currentThread.setName("JobDoneEventQueue");
    currentThread.start();
  }

  public void onJobDone(JobDoneEvent event, Listener<JobDoneEvent> listener) {
    try {
      queue.put(new JobEventInternal(listener, event));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  protected void processEvents() {
    while (!queue.isEmpty() || !terminated) {
      try {
        JobEventInternal event = queue.take();
        if (event.listener != null)
          event.listener.listen(event.jobDoneEvent);
        onJobDone.fire(event.jobDoneEvent);
      } catch (InterruptedException e) {
      }
    }
  }

  public boolean isEmpty() {
    return queue.isEmpty();
  }

  public void dispose() {
    terminated = true;
    currentThread.interrupt();
  }
}
