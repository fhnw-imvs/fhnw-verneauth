# VerneAuth

Application that provides authentication/authorization for VerneMQ.

User credentials and ACLs are stored in a file-based H2 database and an API is available to update the configuration conveniently.


## VerneMQ-Webhooks
The following webhooks are exposed on port `8080` (default):

| Endpoint              | Method | Corresponding VerneMQ-Webhook |
|-----------------------|--------|-------------------------------|
| `/webhooks/register`  | `POST` | `auth_on_register`            |
| `/webhooks/subscribe` | `POST` | `auth_on_subscribe`           |
| `/webhooks/publish`   | `POST` | `auth_on_publish`             |

See the official [VerneMQ documentation](https://docs.vernemq.com/plugindevelopment/webhookplugins#configuring-webhooks) on how to configure the webhooks properly. 
Below is a basic example using `fhnw-verneauth-prod` as hostname within a Docker environment:
```
plugins.vmq_webhooks = on

vmq_webhooks.authreg.hook = auth_on_register
vmq_webhooks.authreg.no_payload = on
vmq_webhooks.authreg.endpoint = http://fhnw-verneauth-prod:8080/webhooks/register

vmq_webhooks.authsub.hook = auth_on_subscribe
vmq_webhooks.authsub.no_payload = on
vmq_webhooks.authsub.endpoint = http://fhnw-verneauth-prod:8080/webhooks/subscribe

vmq_webhooks.authpub.hook = auth_on_publish
vmq_webhooks.authpub.no_payload = on
vmq_webhooks.authpub.endpoint = http://fhnw-verneauth-prod:8080/webhooks/publish
```


## REST-API
The following Management-API is exposed on port `8080` (default):

| Endpoint                | Method   | Payload         | Description                  |
|-------------------------|----------|-----------------|------------------------------|
| `/api/user`             | `GET`    |                 | Returns all users            |
| `/api/user`             | `POST`   | *see below (1)* | Creates and returns the user |
| `/api/user/<username>`  | `GET`    |                 | Returns the user             |
| `/api/user/<username>`  | `PUT`    | *see below (2)* | Updates and returns the user |
| `/api/user/<username>`  | `DELETE` |                 | Removes the user             |
| `/api/stats/auth`       | `GET`    |                 | Returns auth-statistics      |
| `/api/stats/auth`       | `DELETE` |                 | Clears auth-statistics       |

**(1)** To *create a new user*, `POST` the following payload to `/api/user` (example):
```json
{
  "username": "xyz",
  "password": "yk2Nyd3Kma73Endb",
  "aclSubscribe": [ "dt/measurement/example/+","dt/status/example/+", "cmd/example"],
  "aclPublish": [ "dt/measurement/example/+","dt/status/example/+"]
}
```

**(2)** To *update* a user, `PUT` the following payload to `/api/user/<username>` (example):
```json
{
  "username": "abc",
  "password": "7uc2uSrYafmSPVv2",
  "aclSubscribe": [ "dt/measurement/test/+","dt/status/test/+", "cmd/test"],
  "aclPublish": [ "dt/measurement/pub/+","dt/status/pub/+"]
}
```

**Remarks:**
* The placeholder `%u` is supported for ACLs and is replaced by the corresponding username before processing.
* Statistics are preserved in-memory and thus get cleared automatically when the application is restarted.
* Requests for subscribe and publish topics of non-registered users are not included in the statistics.

## Build
Build an executable JAR with Maven:
```
mvn clean package
```

Build the Docker image after building the JAR:
```
docker build --no-cache -t <tag-name> -f ./src/main/docker/Dockerfile .
```

## Run with Docker
* Add port-mapping for port 8080 if necessary.
* The H2-database is persisted in the directory `/opt/verneauth/data` and can be mapped as a volume.

*Example:*
```
docker run -v verneauth-data-volume:/opt/verneauth/data -p 8080:8080 <tag-name>
```


## License
This project is licensed under the Apache 2.0 license, see [LICENSE](LICENSE).
