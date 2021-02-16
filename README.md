# Rate Limiter RX

Redisson based request limiter.

### How to run

- `docker-compose up -d redis mongo`
- run with IntelliJ 

### Configurations

```yaml
rate:
  limit: 5 (how many per period)
  period: 10 (period in seconds)
  wait: 2 (wait time for acquisition in seconds)
```

### Why Mongo?

Storing a call to a persistence data store is the stand-in for redirecting the call. That way we can tabulate what calls
where actually sent.