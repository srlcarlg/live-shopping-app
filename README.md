# live-shop

### All responsibilities
- Shop of an existing live
- Keep all products of each live
- Receives a payment request then sends its result
- Keep transactions to make some analysis

### gRPC
```
Client
- Get the broadcaster's ws-session ID (ChatService, GetBroadcaster)
- Validate liveSlug (ChatService, Validate)
```
### REST
```
- Product
Get all products(GET /store/all)
Get all products of a live (GET /store/{liveSlug})
*Create a new product (POST /store/{liveSlug})
  - return "broadcaster-not-found" if the given liveSlug doesn't have a broadcaster in live-chat
*Edit a product (PUT /store/{liveSlug}/{id})
  - return "slug-unauthorized" if the given productID is not assigned to the liveSlug 	
*Delete a product (DELETE /store/{liveSlug}/{sessionId}/{id})
  - return "slug-unauthorized"...

* -> Validate broadcaster's ID via gRPC
  - return "session-unauthorized" if the given sessionID is not the broadcaster

- Payment
Create a new payment request (POST /store/{liveSlug}/payment)
  - return "product-not-found" (productID not found)
  - return "slug-unauthorized"...

- Lives Statement
Get latest(5) transactions (GET /store/transactions
Get total amount per(all) live (GET /store/total
```

### Tests
???

### Info
- Spring Boot 3.2.5
	- Spring Data 
		- R2DBC
		- PostgresSQL
		- Flyway
    - Spring Webflux (Reactive)
  	 	- REST
- gRPC (Reactive)
	- [Reactor-gRPC (salesforce)](https://github.com/salesforce/reactive-grpc/tree/master/reactor)
	- [gRPC-Client Spring Boot Starter (grpc-ecosystem)](https://github.com/grpc-ecosystem/grpc-spring)


### References

[Spring Guides](https://spring.io/guides) 

[Project Reactor Docs](https://projectreactor.io/docs/core/release/reference/)

[ChatGPT - OpenAi](https://chat.openai.com/)


## <a name="usage"></a>Usage
The application can be run in:

<details>
<summary><i>Dev mode</i></summary>

**1º Start the PostgreSQL container**:
```shell script
docker compose -f cassandra-compose.yaml up -d
```
**2º Just run.**
```shell script
./mvnw compile spring-boot:run 
```
</details>

<details>
<summary><i>Packaged running locally (or Docker)</i></summary>

**1º package live-store.**
```shell script
./mvnw package -DskipTests=true
```
**2º Start the Cassandra database container**:
```shell script
docker compose -f postgres-compose.yaml up -d
```
**3º Run the package!**
```shell script
java -jar ./target/live-store-0.0.1-SNAPSHOT.jar
```
### on Docker
**Afte 1º just do:**
```shell script
docker compose up -d
```
</details>