import org.slf4j.Logger;

unresolvedVariable { var ->
    if('log' == var.name) {
        storeType(var, classNodeFor(Logger));
        handled = true;
    }
}

methodNotFound { receiver, name, argList, argTypes, call ->
    if(name == 'every') { 
        handled = true;
        return newMethod('every', classNodeFor(Scheduler));
    }

    if(name == 'once') {
        handled = true;
        return newMethod('once', classNodeFor(Scheduler));
    }
}
