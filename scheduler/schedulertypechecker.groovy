import org.slf4j.Logger;

unresolvedVariable { var ->
    if('log' == var.name) {
        storeType(var, classNodeFor(Logger));
        handled = true;
    }
}
