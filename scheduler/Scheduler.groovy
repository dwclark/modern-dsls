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

        public Task seconds(Closure toExecute) {
            this.units = TimeUnit.SECONDS;
            this.toExecute = toExecute;
            return this;
        }

        public Task minutes(Closure toExecute) {
            this.units = TimeUnit.MINUTES;
            this.toExecute = toExecute;
            return this;
        }

        public Task milliseconds(Closure toExecute) {
            this.units = TimeUnit.MILLISECONDS;
            this.toExecute = toExecute;
            return this;
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

    public static RepeatingTask every(Number number) {
        RepeatingTask t = new RepeatingTask(time: number as long);
        current().tasks << t;
        return t;
    }

    public static SingleTask after(Number number) {
        SingleTask t = new SingleTask(time: number as long);
        current().tasks << t;
        return t;
    }

    public void run() {
        tasks.each { Task t -> t.schedule(service); };
    }
}
