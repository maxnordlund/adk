#!/usr/bin/env bash

# Make sure we are in the directory containing this file
cd "$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Fail fast if there are compilation errors
javac -Werror Main.java || exit $?

for t in testfall/testmedordlista{1,2}; do
  # echo $t
  # cat $t.indata | sed '1,/\#/d' | wc -l
  cat $t.indata | time java Main | diff - $t.utdata
done

for t in testfall/testord{1,2}; do
  # echo $t
  # cat $t.indata | wc -l
  cat ordlista.txt $t.indata | time java Main | diff - $t.utdata
done
