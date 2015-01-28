jtop
=============

A terminal application that monitors Java applications over JMX. Uses NCURSES to graphically monitor activity.

Written in Scala.js for Node.js.

![screenshot](screenshot.png)


## Requirements

jtop requires Node.js and certain modules. To run the demo you will also need the `scala` command installed.

Here are the command to install Node (requires Homebrew) and the necessary modules.

    brew install node
    npm install jmx blessed blessed-contrib

You will also need a terminal supporting full xterm colors.

## Running jtop

To start, you'll need a Java application to monitor. Run the following commands in the terminal to enable JMX monitoring and then start a Scala REPL that will be busy sorting strings and juggling threads.

    export JAVA_OPTS="-Dcom.sun.management.jmxremote.port=8855 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"

    scala -e 'import scala.concurrent.ExecutionContext.Implicits.global; import scala.concurrent.Future; while(true) { Future { for (i <- 1 to util.Random.nextInt(20)) { println("processing"); val l = (1 to 100000).map(_.toString).toList;  util.Random.shuffle(l).sorted; Thread.sleep(util.Random.nextInt(2000)); }; }; Thread.sleep(util.Random.nextInt(4000)) }'

Then, in a separate (and colorful!) terminal execute `run.sh`.

