# manager-backend

### All responsabilities
- Create a new live
- Get info or change live password
- Join/start/finish a live by broadcaster
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
Get one live by slug (LiveGrpc, FindOneBySlug)
Login/Validate live(slug) by password (LiveGrpc, Login)
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
	- sends {users_count: int} to all sessions except current. 

- "leave"
	- remove and sends "users_count" message {int} to all sessions except current.
	- then disconnect.

- receives {js_peer_id: string, live_password: string} -> SetBroadcaster
	- validate livePassword, sends "incorrect-password" text if it doesn't match.
	- sends {broadcaster-id: string} to all sessions, include current.

- receives {live_password: string} -> SetLiveStatus 
	- validate livePassword, sends "incorrect-password" text if it doesn't match.
	- set liveStatus to DONE.
	- sends "live-finished" text to all sessions, except current.
```

*onClose:* same as "leave"

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

