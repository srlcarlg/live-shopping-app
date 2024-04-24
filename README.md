# live-chat

### All responsibilities
- Chat room of an existing live
- Keep all messages of each live

### gRPC

```
- Client
Get one live by slug (LiveService, FindOneBySlug)
Validate live(slug) by password (LiveService, Validate)

- Server
Get the broadcaster's ws-session ID (ChatService, GetBroadcaster)
Validate liveSlug (ChatService, Validate)
```

### WEBSOCKET
*onOpen:* /chat/{slug}

```
- validate slug via gRPC, sends "slug-not-found" text if no live is found.
- validate liveStatus via gRPC, sends "live-finished" text if LiveStatus.DONE.
- disconnect or keep connection
```

*onMessage:* raw-text or json

```
- receives { username: string, email: string, password: string } -> JoinChat
	- validate (live)password via gRPC **if exists**, 
		- set current session as broadcaster and sends its sessionID, else "incorrect-password" 
	- join the chat room of an existing live and send it's history (last 30 messages)

- receives { message: string } -> MessageChat
	- validate that the current session is joined, else "unauthorized"
	- sends *SendChat to all sessions of current room(slug) and save in database

- "finish-chat"
	- validate that the current session is the broadcaster, else sends "unauthorized"
	- sends "chat-finished" to all sessions of current room(slug)

*SendChat(json) = {
	username,
	email,
	message,
	isBroadcaster
}
```

### Tests
???

### Info
- Spring Boot 3.2.5
	- Spring Data
		- Apache Cassandra Reactive noSQL
    - Spring Webflux (Reactive)
  	  - Websockets
- gRPC (Reactive)
	- [Reactor-gRPC (salesforce)](https://github.com/salesforce/reactive-grpc/tree/master/reactor)
	- [gRPC Spring Boot Starter (grpc-ecosystem)](https://github.com/grpc-ecosystem/grpc-spring)


### References

[Spring Guides](https://spring.io/guides) 

[Project Reactor Docs](https://projectreactor.io/docs/core/release/reference/)

[ChatGPT - OpenAi](https://chat.openai.com/)

[How To Build a Chat App Using WebFlux, WebSockets & React - Johan Zietsman](https://johanzietsman.com/how-to-build-a-chat-app-using-webflux-websockets-react/) - A great start point.