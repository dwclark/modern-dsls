//This is the main DSL file for the scheduler

log.debug("Starting the scheduler");

every 5 seconds { println("Every 5 seconds, ${new Date()}"); }
//same as:
//every(5).seconds({ println("Every 5 seconds, ${new Date()}"); })

every 1_000 milliseconds { log.debug("Heartbeat log at ${new Date()}"); }

after 3 seconds { println("Single shot execution at ${new Date()}"); }
