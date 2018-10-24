start() {
  mvn clean package && docker run --name=api -d -p 8080:8080 com.volmar/tweetflock
}

stop() {
  docker stop api && docker rm api
}

case "$1" in
  start)
    start
    ;;
  stop)
    stop
    ;;
  *)
    echo "Usage: $0 {start|stop}" >&2
    exit 1
    ;;
esac
