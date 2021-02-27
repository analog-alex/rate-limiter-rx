# Rate Limiter RX

Redisson based request limiter.

### How to run

Just use `docker-compose up rx-app`!

if you want to run locally, this should do it:

- `docker-compose up -d redis fastify`
- run with IntelliJ 


### Configurations for the rate limiting

Explanations inside the example `yaml`:

```yaml
rate:
  limit: 5 (how many per period)
  period: 10 (period in seconds)
  wait: 2 (wait time for acquisition in seconds)
```

### What is being rate limited?

The inbound calls! 

Just POST to `/call/inbound` something like:

```json
{
  "accountId": "{AN ACCOUNT ID}",
  "to": "{CALL RECEIVER}",
  "from": "{CALL CENTRAL}",
  "system": "{A MEDIATING SYSTEM, Values can be [CENTRAL|STUDIO]}"
}
```
so

```json
{
  "accountId": "POPING-RX-435",
  "to": "+123456789",
  "from": "+987654321",
  "system": "CENTRAL"
}
```

### Why the Fastify back end?

It is the stand in for the system being rate-limited. It is a server that stores all incoming "calls" in a **DynamoDB** instance,
that then can be queried for:

1. All the calls received (for any account) via `/api/v1/entity`
2. The CPS (calls per second) of a particular account via `/api/v1/entity/cps?accountId={}&interval={}&unit={}`

In the CPS endpoint, the arguments are:

- **accountId** -- the account identifier (duh!)
- **interval** --  a number interval representing the time slice to calculate the CPS
- **unit** --  the unit of time that the interval is in. Possible values are **min**, **sec** and **hour**.

For example, the call `/api/v1/entity/cps?accountId=LOLOL&interval=2&unit=sec` should return a JSON like:

```json
{
  "calls": 100,
  "CPS": 50
}
```

which indicates that the system received 100 calls in the last 2 seconds, with an average of 50 calls per second.