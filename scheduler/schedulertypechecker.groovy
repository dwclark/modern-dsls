//This file is used to type check any code that will be missed by
//Launch.groovy and Scheduler.groovy
//It basically just makes sure that if someone uses a 'log' variable
//then we should tell the type checker to NOT flag it as an
//error, we know that we will inject it at runtime and it will be there

import org.slf4j.Logger;

unresolvedVariable { var ->
    if('log' == var.name) {
        storeType(var, classNodeFor(Logger));
        handled = true;
    }
}
