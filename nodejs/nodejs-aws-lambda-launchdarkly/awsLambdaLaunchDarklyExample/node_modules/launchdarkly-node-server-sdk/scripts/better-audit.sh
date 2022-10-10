#!/bin/bash

# This script processes the output of "npm audit" to make it more useful, as follows:
# - For each flagged vulnerability, it looks at the "path" field and extracts both the flagged
#   package (the last element in the path) and the topmost dependency that led to it (the first
#   element in the path).
# - It sorts these and eliminates duplicates.
# - It then compares each of the topmost dependencies to package.json to see if it is from
#   "dependencies", "peerDependencies", or "devDependencies". If it is either of the first two
#   then this is a real runtime vulnerability, and must be fixed by updating the topmost
#   dependency. If it is from devDependencies, then it can be safely fixed with "npm audit fix".

set -e

function readPackages() {
  inCategory=$1
  jq -r ".${inCategory} | keys | .[]" package.json 2>/dev/null || true
}

function isInList() {
  item=$1
  shift
  for x in $@; do
    if [ "$item" == "$x" ]; then
      true
      return
    fi
  done
  false
}

dependencies=$(readPackages dependencies)
devDependencies=$(readPackages devDependencies)
peerDependencies=$(readPackages peerDependencies)

function processItems() {
  flaggedRuntime=0
  flaggedDev=0
  while read -r badPackage topLevelDep; do
    echo -n "flagged package \"$badPackage\", referenced via \"$topLevelDep\" "
    for category in dependencies peerDependencies devDependencies; do
      if isInList $topLevelDep ${!category}; then
        if [ "$category" == "devDependencies" ]; then
          echo "-- from \"$category\""
          flaggedDev=1
        else
          echo "-- from \"$category\" (RUNTIME) ***"
          flaggedRuntime=1
        fi
        break
      fi
    done
  done
  echo
  if [ "$flaggedRuntime" == "1" ]; then
    echo "*** At least one runtime dependency was flagged. These must be fixed by updating package.json."
    echo "Do not use 'npm audit fix'."
    exit 1  # return an error, causing the build to fail
  elif [ "$flaggedDev" == "1" ]; then
    echo "Only development dependencies were flagged. You may safely run 'npm audit fix', which will"
    echo "fix these by adding overrides to package-lock.json."
  else
    echo "Congratulations! No dependencies were flagged by 'npm audit'."
  fi
}

echo "Running npm audit..."
echo

npm audit --json \
  | grep '"path":' \
  | sort | uniq \
  | sed -n -e 's#.*"path": "\([^"]*\)".*#\1#p' \
  | awk -F '>' '{ print $NF,$1 }' \
  | sort | uniq \
  | processItems
