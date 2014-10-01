#!/usr/bin/env bash

# Make sure we are in the directory containing this file
cd "$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Fail fast if there are compilation errors
javac -Werror Main.java || exit $?

diff <(cat ordlista.txt testfall/testord*.indata | \
  time java -agentlib:hprof=cpu=times,monitor=y Main) \
  <(cat testfall/testord*.utdata)

gprof2dot -f hprof -o java.hprof.dot -n 2.0 < java.hprof.txt
cat java.hprof.dot | dot -Tpng > profile.png 2> /dev/null
