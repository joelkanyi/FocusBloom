#!/bin/bash
echo "*********************************************************"
echo "Running git pre-commit hook. Running Spotless Apply... "
echo "*********************************************************"

# Gather the staged files - to make sure changes are saved only for these files.
stagedFiles=$(git diff --staged --name-only)

# run spotless apply
./gradlew spotlessApply

status=$?

if [ "$status" = 0 ] ; then
    echo "Static analysis found no problems."
    # Add staged file changes to git
    for file in $stagedFiles; do
      if test -f "$file"; then
        git add $file
      fi
    done
    #Exit
    exit 0
else
    echo "*********************************************************"
    echo "       ********************************************      "
    echo 1>&2 "Spotless Apply found violations it could not fix."
    echo "Run spotless apply in your terminal and fix the issues before trying to commit again."
    echo "       ********************************************      "
    echo "*********************************************************"
    #Exit
    exit 1
fi