### How to start

```bash
docker build -t redis-server .
docker run -d -p 6379:6379 --name redis-cache -v redis_data:/data redis-server
```

### Verify if it is working

```bash
docker exec -it redis-cache redis-cli

127.0.0.1:6379> SET testkey "Hello, Redis!"
OK

127.0.0.1:6379> GET testkey
"Hello, Redis!"
```