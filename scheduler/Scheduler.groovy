import java.util.concurrent.*;
import groovy.transform.TypeChecked;
import groovy.util.logging.Slf4j;

@Slf4j @TypeChecked
class Scheduler {

    //Main state that the scheduler needs to 
    final ScheduledExecutorService service = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    final List<Task> tasks = [];

    //The point of the thread local stuff here is a bit technical
    //The DSL is going to call the static methods below to configure the scheduler. This is needed
    //so that the DSL has no knowledge about how the scheduler is created, when it is created,
    //or even that it _is_ created. By setting this thread local variable the static
    //methods can get to the scheduler we are currently configuring as we are running
    //the DSL script.
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

    /*These next two methods will always be the first "method" called in the DSL.
      We arrange for them to return the correct task object which will
      then receive all of the later calls in the DSL
      for example: every 5 seconds { println("Every 5 seconds, ${new Date()}"); }
      First, every(5) will return a RepeatingTask
      Second, we call the seconds method on Task which taks a closure.
      In normal java code we would probably write this:
      
      Task task = new RepeatingTask();
      task.every(5, TimeUnit.SECONDS);
      task.execute { println("Every 5 seconds, ${new Date()}") }
      scheduler.tasks << task;
      scheduler.run();

    */
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

    //Actually run the tasks
    public void run() {
        tasks.each { Task t -> t.schedule(service); };
    }
}
