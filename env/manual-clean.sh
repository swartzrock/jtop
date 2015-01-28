#!/bin/bash -e

modules="."

toplevel=".idea .idea_modules"
moduleSubdirs="target project/project project/target"

restore=".idea/runConfigurations"

curr=$(pwd -P)
cd $(cd -P -- "$( dirname -- "${BASH_SOURCE[0]}")" && pwd -P)
cd ..

echo "Manually cleaning this project..."

echo "  Backing up designated directories..."
mkdir -p $restore
tar cf ".backup.tar" $restore

echo "  Removing the following directories:"

find . -name ".DS_Store" -exec rm -rf {} \;

for dir in $toplevel ; do
  test -d "$dir" && {
    echo "    $(cd "$dir" && pwd -P)"
    rm -rf "$dir"
  } || true
done

for module in $modules ; do
  for subdir in $moduleSubdirs ; do
    dir=$module/$subdir
    test -d "$dir" && {
      echo "    $(cd "$dir" && pwd -P)"
      rm -rf "$dir"
    } || true
  done
done

echo "  Restoring designated directories..."
tar xf ".backup.tar"
rm -f ".backup.tar"

cd "$curr"

