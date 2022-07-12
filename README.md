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

### Running the application
- Build project with `mvn clean install`
- Run application with `java -jar <path_to_jar>/webhook-0.0.1-SNAPSHOT.jar com.deanhayden.webhook.WebhookApplication`

Example cURL for registering a webhook:

```
curl --location --request POST 'http://localhost:8080/clocks/subscriptions' \
--header 'Content-Type: application/json' \
--data-raw '{
    "url" : "http://localhost:8081/webhook",
    "frequency" : "PT10S"
}'
```

### TODO

- Better exception handling in ClockSubscriptionWebClient
- Testing of threadPoolTaskScheduler task creation
- Better testing of edges cases i.e. validation
