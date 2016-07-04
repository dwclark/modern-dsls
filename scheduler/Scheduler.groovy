import java.util.concurrent.*;
import groovy.transform.TypeChecked;
import groovy.util.logging.Slf4j;

@Slf4j @TypeChecked
class Scheduler {

    final ScheduledExecutorService service = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    final List<Task> tasks = [];
    
    static abstract class Task {
        long time;
        TimeUnits units;
        Closure toExecute;

        public Runnable safe() {
            return { ->
                try {
                    toExecute.call();
                }
                catch(Throwable t) {
                    log.error("Error in scheduled task");
                } } as Runnable;
        }
        
        abstract void execute(Closure closure);
        abstract protected void schedule(ScheduledExecutorService service);
    }
    
    static class RepeatingTask {
        void execute(Closure closure) {
            toExecute = closure;
        }

        protected void schedule(ScheduledExecutorService service) {
            service.scheduleAtFixedRate(safe(), time, time, units);
        }
    }

    static class SingleTask {
        void execute(Closure closure) {
            toExecute = closure;
        }

        protected void schedule(ScheduledExecutorService service) {
            service.schedule(safe(), time, units);
        }
    }

    public RepeatingTask every(Map interval) {
        long key = interval.keySet().iterator().next() as long;
        RepeatingTask t = new RepeatingTask(time: key, units: interval['units'] as TimeUnit);
        tasks << t;
        return t;
    }

    public SingleTask once(Map interval) {
        long key = interval.keySet().iterator().next() as long;
        SingleTask t = new SingleTask(time: key, units: interval['units'] as TimeUnit);
        tasks << t;
        return t;
    }

    public void run() {
        tasks.each { Task t -> t.schedule(service); };
    }
}
