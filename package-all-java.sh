#!/bin/bash

directories=("live-manager" "live-chat" "live-store")

for dir in "${directories[@]}"; do
  if [ -d "$dir" ]; then
    echo "Running 'mvnw package -DskipTests=true' in $dir"
    (cd "$dir" && ./mvnw package -DskipTests=true)
  else
    echo "Directory $dir does not exist."
  fi
done
