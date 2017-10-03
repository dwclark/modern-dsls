@Grapes(
    [ @Grab(group='org.slf4j', module='slf4j-api', version='1.7.21'),
      @Grab(group='ch.qos.logback', module='logback-classic', version='1.1.7') ] )
import groovy.grape.*;
import groovy.transform.TypeChecked;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
  This is the main entry point for running the scheduler.
  It sets up the scheduler, configures the groovy shell,
  sets up the logger, and finally launches the scheduling engine
 */
@TypeChecked
class Launch {

    private static final String TYPE_CHECKING_SCRIPT = 'schedulertypechecker.groovy';
    
    public static Scheduler configure(File file) {
        //We need the actual scheduler to "run" the script
        //In actuality the script merely configures the scheduler
        Scheduler scheduler = new Scheduler();
        Scheduler.current(new Scheduler());
        
        Logger log = LoggerFactory.getLogger(Launch);

        //add implicit "import static java.util.concurrent.TimeUnit.*" and "Scheduler.*" to script
        //this is what allows schedule.groovy to not have any imports
        CompilerConfiguration compilerConfig = new CompilerConfiguration();
        ImportCustomizer icustom = new ImportCustomizer();
        icustom.addStaticStars("java.util.concurrent.TimeUnit");
        icustom.addStaticStars("Scheduler");

        //force the script to be type checked, mainly used to tell the type checker that
        //it's fine that it cannot find the log variable we are injecting
        Map<String,String> map = Collections.singletonMap("extensions", TYPE_CHECKING_SCRIPT);
        ASTTransformationCustomizer ast = new ASTTransformationCustomizer(map, TypeChecked);

        //run the script and inject the logger
        compilerConfig.addCompilationCustomizers(icustom, ast);
        GroovyShell shell = new GroovyShell(Launch.classLoader, compilerConfig);
        shell.setVariable("log", log);
        shell.evaluate(file);

        //the scheduler is now configured and ready to go.
        //Because we have forced type checking we should have caught any
        //syntax errors. All errors will now be true runtime errors.
        //Return the scheduler and the main method will run it
        return Scheduler.current();
    }
    
    public static void main(String[] args) {
        //In a real application this would be done during application configuration
        //and we could set up the environment to pass the name of the script to the method
        configure(new File("schedule.groovy")).run();
    }
}
