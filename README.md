# Rate Limiter RX

Redisson based request limiter.

### How to run

- `docker-compose up -d redis`
- run with IntelliJ 

### Configurations

```yaml
rate:
  limit: 5 (how many per period)
  period: 10 (period in seconds)
  wait: 2 (wait time for acquisition in seconds)
```
