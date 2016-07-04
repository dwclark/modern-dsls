import java.util.concurrent.*;
import groovy.transform.TypeChecked;
import groovy.util.logging.Slf4j;

@Slf4j @TypeChecked
class Scheduler {

    final ScheduledExecutorService service = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    final List<Task> tasks = [];
    private static final ThreadLocal<Scheduler> _current = new ThreadLocal();

    public static void current(final Scheduler scheduler) {
        _current.set(scheduler);
    }
    
    public static Scheduler current() {
        return _current.get();
    }
    
    static abstract class Task {
        long time;
        TimeUnit units;
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
        
        void execute(Closure closure) {
            this.toExecute = closure;
        }
        
        abstract protected void schedule(ScheduledExecutorService service);
    }
    
    static class RepeatingTask extends Task {
        protected void schedule(ScheduledExecutorService service) {
            service.scheduleAtFixedRate(safe(), time, time, units);
        }
    }

    static class SingleTask extends Task {
        protected void schedule(ScheduledExecutorService service) {
            service.schedule(safe(), time, units);
        }
    }

    public static RepeatingTask every(Map interval) {
        Object key = interval.keySet().iterator().next();
        Object value = interval[key];
        RepeatingTask t = new RepeatingTask(time: key as long, units: value as TimeUnit);
        current().tasks << t;
        return t;
    }

    public static SingleTask once(Map interval) {
        Object key = interval.keySet().iterator().next();
        Object value = interval[key];
        SingleTask t = new SingleTask(time: key as long, units: value as TimeUnit);
        current().tasks << t;
        return t;
    }

    public void run() {
        tasks.each { Task t -> t.schedule(service); };
    }
}
