# manager-backend

### All responsibilities
- Create a new live
- Get info or change live password
- Join/finish a live by broadcaster
- Join/leave from a live by viewer
- Handle quantity of viewers

### REST
```
Get all lives (GET /lives)
Get one live by slug (GET /lives/{slug})
Create a new Live (POST /lives)
Edit live password (PUT /lives/{slug})
```
### gRPC
```
Get one live by slug (LiveService, FindOneBySlug)
Validate live(slug) by password (LiveService, Validate)
```
### WEBSOCKET
*onOpen:* /live/{slug}

```
- validate slug, sends "slug-not-found" text if no live is found.
- validate liveStatus, sends "live-finished" text if LiveStatus.DONE.
- disconnect or keep connection
```

*onMessage:* raw-text or json

```
- "join"
	- sends {broadcaster-id: string} to current session if exists.
	- sends {users_count: int} to all sessions. 
	
- receives {peer_id: string, live_password: string} -> SetBroadcaster
	- validate livePassword, sends "incorrect-password" text if it doesn't match.
	- sends {broadcaster-id: string} to all sessions.

- receives {live_password: string} -> SetLiveStatus 
	- validate livePassword, sends "incorrect-password" text if it doesn't match.
	- set liveStatus to DONE.
	- sends "live-finished" text to all sessions.
```

*onClose:* 

```
- remove and sends {users_count: int} to all sessions.
- then disconnect.
```


### Tests
Quarkus Junit 5, Mockito (Core + Panache)

The tests cover most of the situations described above. 

### Info
- Quarkus 3.9.3
    - REST (Reactive)
    - REST Jackson
    - gRPC
    - Websockets
	- Hibernate Reactive Panache
	- Reactive PostgresSQL client
- JDK 21
	- Virtual Threads

## <a name="usage"></a>Usage
The application can be run in:

<details>
<summary><i>Dev mode</i></summary>

**1º Just run.**
```shell script
./mvnw compile quarkus:dev
```
**Thanks to [Quarkus Dev Services](https://quarkus.io/guides/dev-services)**, a PostgreSQL database is started automatically from a container.
</details>

<details>
<summary><i>Packaged running locally (or Docker)</i></summary>

**1º package manager-backend.**
```shell script
./mvnw package -DskipTests=true
```
**2º Start the PostgreSQL container**:
```shell script
docker compose -f postgres-compose.yaml up -d
```
**3º Run the package!**
```shell script
java -jar ./target/quarkus-app/quarkus-run.jar
```
### on Docker
**Afte 1º just do:**
```shell script
docker compose up -d
```
</details>


<details>
<summary><i>Native executable running locally (or Docker)</i></summary>

**1º package manager-backend.**
```shell script
./mvnw package -DskipTests=true -Dnative -Dquarkus.native.container-build=true
```
**2º Start the PostgreSQL container**:
```shell script
docker compose -f postgres-compose.yaml up -d
```
**3º Run the package!**
```shell script
./target/manager-backend-0.0.1-SNAPSHOT-runner
```
### on Docker
**Afte 1º just do:**
```shell script
docker compose -f native-compose.yaml up postgres -d
docker compose -f native-compose.yaml up -d
```
</details>