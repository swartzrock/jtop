#!/bin/bash

cd $(cd -P -- "$( dirname -- "${BASH_SOURCE[0]}")" && pwd -P)

cp -p target/scala-2.11/jtop-fastopt.js jtop.js

cat target/scala-2.11/jtop-fastopt.js | sed 's/ScalaJS.g\["require"\]/require/' > jtop.js

cat << EOF >> jtop.js
// Hack console log to duplicate double % signs
(function() {
  var oldLog = console.log;
  var newLog = function() {
    var args = arguments;
    if (args.length >= 1 && args[0] !== void 0 && args[0] !== null) {
      args[0] = args[0].toString().replace(/%/g, "%%");
    }
    oldLog.apply(console, args);
  };
  console.log = newLog;
})();

((typeof global === "object" && global &&
  global["Object"] === Object) ? global : this)["jtop"]["Main"]().main();
EOF

node jtop.js