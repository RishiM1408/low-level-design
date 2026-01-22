#!/bin/sh
MODULE=$1

case "$MODULE" in
  "chess")
    java -jar chess.jar
    ;;
  "vending")
    java -jar vending-machine.jar
    ;;
  "parking")
    java -jar parking-lot.jar
    ;;
  "ratelimiter")
    java -jar rate-limiter.jar
    ;;
  *)
    echo "Usage: docker run <image> [chess|vending|parking|ratelimiter]"
    echo "Available modules: chess, vending, parking, ratelimiter"
    ;;
esac
