# Modern Groovy DSL's

In this project are 5 sub projects, plus the presentation itself. A description of each project is as follows:

## email - Simple Closure Based DSL

This project simulates a simple email dsl. The Email.groovy file defines the DSL and basic.groovy uses the DSL. To see the DSL in action simply run basic.groovy from inside groovysh or groovyConsole.

## emailForIde - Simple Closure Based DSL with IDE Enhancements

This project is virtually identical to the email project. The only differences are an IntelliJ project file and a slight modification to the DSL in Email.groovy. The DSL now uses the @DelegatesTo annotation which should allow IntelliJ to provide code completion. Run the project in the same way as the email project.

## emailBaseScript - Simple Script Based DSL

Again, this is virtually the same DSL as in email and emailForIde. The difference is that the DSL is defined as a custom base script. This means that basic.groovy now uses the @BaseScript annotation to tell groovy to run this instead of the default base shell script. Run it in the same way as email and emailForIde.

## scheduler - A More Complete DSL

The DSL provides a simple scheduler DSL to demonstrate compilation customizers and type checking extensions. To run the demo, execute "groovy Launch.groovy".

* `schedule.groovy` - This script uses the DSL to schedule events at intervals. The syntax should indicate exactly what the DSL provides and should be more or less self describing
* `Scheduler.groovy` - Provides the functionality to actually execute the scheduled tasks.
* `schedulertypechecker.groovy` - Gives type checking hints to Groovy so that `schedule.groovy` can be type checked before it is run. This allows groovy to catch errors at compile time instead of at runtime. It also enables much better error reporting to people who want to write scheduler scripts.
* `Launch.groovy` - This is the script that should be run to launch the scheduler. The main thing this script does is set up the environment. It defines the type checking extension to be used, forces the scheduler script to be type checked, adds implicit imports, and creates the Scheduler object.

## currency - Demonstrates Extension Modules and Operator Overloading

This project isn't technically a DSL, but it does show two techniques that are used when making DSL's: extension modules and operator overloading. Because extension modules must be compiled before being used, this is an actual gradle project. The easiest way to see this in action is the following:

1. Build the project using `gradle build`.
2. Launch `groovyConsole` and add the built jar to the classpath.
3. Load `src/main/resources/sample.groovy` into groovyConsole.
4. Run the script from there.

The main functionality for the operator overload is in the `com.github.dclark` package. It contains the following files:

* `Currency.groovy` - This is the main currency type and defined basic operations with currencies.
* `ExchangeRates.groovy` - A simple simulation for calculating exchange rates, used by the currency type for converting between currencies.
* `CurrencyExtension.groovy` - This is the actual extension, it adds properties and methods to `java.lang.Number`.
* `CurrencyId.groovy` - A simple enum for identifying currencies.

The other important file in this project is the file the tells groovy that the compiled jar contains an extension module. This is located at `src/main/resources/META-INF/services/org.codehaus.groovy.runtime.ExtensionModule`.