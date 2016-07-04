log.debug("Starting the scheduler");

every 5: SECONDS execute({ -> println("Every 5 seconds, ${new Date()}"); })

every 1_000: MILLISECONDS execute { -> log.debug("Heartbeat log at ${new Date()}"); }

after 3: SECONDS execute { -> println("Single shot execution at ${new Date()}"); }
