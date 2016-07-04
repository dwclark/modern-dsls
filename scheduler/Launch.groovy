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

@TypeChecked
class Launch {

    private static final String TYPE_CHECKING_SCRIPT = 'schedulertypechecker.groovy';
    
    public static Scheduler configure(File file) {
        //object needed to run the script
        Scheduler scheduler = new Scheduler();
        Scheduler.current(new Scheduler());
        
        Logger log = LoggerFactory.getLogger(Launch);

        CompilerConfiguration compilerConfig = new CompilerConfiguration();
        ImportCustomizer icustom = new ImportCustomizer();

        //add implicit "import static java.util.concurrent.TimeUnit.*" and "Scheduler.*" to script
        icustom.addStaticStars("java.util.concurrent.TimeUnit");
        icustom.addStaticStars("Scheduler");

        //force the script to be type checked
        Map<String,String> map = Collections.singletonMap("extensions", TYPE_CHECKING_SCRIPT);
        ASTTransformationCustomizer ast = new ASTTransformationCustomizer(map, TypeChecked);

        //run the script
        compilerConfig.addCompilationCustomizers(icustom, ast);
        GroovyShell shell = new GroovyShell(Launch.classLoader, compilerConfig);
        shell.setVariable("log", log);
        shell.evaluate(file);

        return Scheduler.current();
    }
    
    public static void main(String[] args) {
        configure(new File("schedule.groovy")).run();
    }
}
