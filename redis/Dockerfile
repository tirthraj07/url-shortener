# Use the official Redis image from Docker Hub
FROM redis:latest

# Expose the default Redis port
EXPOSE 6379

# Set the data directory to a volume
VOLUME /data

# Run Redis server with append-only file (AOF) enabled for data persistence
CMD ["redis-server", "--appendonly", "yes"]