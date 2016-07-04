import groovy.grape.*;
import groovy.transform.TypeChecked;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer;
import org.codehaus.groovy.control.customizers.ImportCustomizer;

@TypeChecked
class Launch {

    private static final String TYPE_CHECKING_SCRIPT = 'schedulertypechecker.groovy';
    
    public static Scheduler configure(File file) {
        //object needed to run the script
        Scheduler scheduler = new Scheduler();
        Logger log = Logger.getLogger(Launch);

        CompilerConfiguration compilerConfig = new CompilerConfiguration();
        ImportCustomizer icustom = new ImportCustomizer();

        //add implicit "import static java.util.concurrent.TimeUnit.*" to script
        icustom.addStaticStars("java.util.concurrent.TimeUnit");

        //force the script to be type checked
        Map<String,String> map = Collections.singletonMap("extensions", TYPE_CHECKING_SCRIPT);
        ASTTransformationCustomizer ast = new ASTTransformationCustomizer(map, TypeChecked);

        //run the script
        compilerConfig.addCompilationCustomizers(icustom, ast);
        GroovyShell shell = new GroovyShell(Launch.classLoader, compilerConfig);
        shell.setVariable("log", log);
        shell.setVariable("scheduler", scheduler);
        shell.evaluate(file);

        return scheduler;
    }
    
    public static void main(String[] args) {
        Grape.grab(group: 'org.slf4j', module: 'slf4j-api', version: '1.7.21');
        configure(new File("schedule.groovy")).run();
    }
}
