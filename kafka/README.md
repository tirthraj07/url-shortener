### How to run


```bash
docker-compose up -d
```

> Start Spring Boot application to create the topic

```bash
docker exec -it kafka /usr/bin/kafka-console-consumer  --bootstrap-server localhost:9092  --topic url-access-events  --from-beginning
```

You should see events in this.