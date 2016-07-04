log.debug("Starting the scheduler");

every 5: SECONDS execute { -> println("Every 5 seconds"); }

every 1_000: MILLISECONDS execute { -> log.debug("Heartbeat log"); }

once 3: SECONDS execute { -> println("Single shot execution"); }
