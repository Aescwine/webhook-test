Spring Boot service that sends the current time to registered services at the given frequency.

The service exposes three APIs for managing webhook subscriptions:

`POST /clocks/subscriptions` - register a new subscription for the given URL, with the given frequency. Returns 400 `Bad Request` if URL is already registered.

**Body**
```
{
    "url" : "http://localhost:8081/webhook",
    "frequency" : "PT20S"
}
```

`PUT /clocks/subscriptions`  - update the frequency of an existing subscription. Returns 400 `Bad Request` if URL is not registered.

**Body**
```
{
    "url" : "http://localhost:8081/webhook",
    "frequency" : "PT20S"
}
```

`DELETE /clocks/subscriptions` - deregister a subscription for the given URL. Returns 400 `Bad Request` if URL is not registered.

**Body**
```
{
    "url" : "http://localhost:8081/webhook",
}
```

