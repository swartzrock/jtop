#!/bin/bash

cd $(cd -P -- "$( dirname -- "${BASH_SOURCE[0]}")" && pwd -P)

cp -p target/scala-2.11/jtop-fastopt.js jtop.js

cat << EOF >> jtop.js
var jmx = require('jmx');
require('blessed');
require('blessed-contrib');

ScalaJS.g["require"] = function(v) {
  if (v === "jmx") return jmx;
};

//ScalaJS.g["require"]["jmx"] = require('jmx');
//ScalaJS.g["require"]["blessed"] = require('blessed');
//ScalaJS.g["require"]["blessed_contrib"] = require('blessed-contrib');

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
