package live.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import live.chat.grpc.LiveGrpcService;
import live.chat.websocket.ChatMessageRepository;

@RestController
public class HelloController {

	@Autowired
	LiveGrpcService clientService;

	@Autowired ChatMessageRepository repository;
	
	@GetMapping("/hello")
	public String handle() {
		
		//clientService.findBySlug("SkyMEjFArfSxw3");
		//clientService.validate("SkyMEjFArfSxw3", "123456");
		
		//repository.saveAll(Flux.just(
		//		new ChatMessage("randomSLug", "Some Message", "some", "unique@email.com", true))).subscribe();
		//repository.saveAll(Flux.just(
		//		new ChatMessage("randomSLug", "Another Message", "someUser", "email@email.com", false))).subscribe();
		
		repository.findByLiveSlug("randomSLug")
			.doOnNext(System.out::println)
			.count()
			.doOnNext(System.out::println)
			.subscribe();
		
		repository.findAll()
		.doOnNext(System.out::println)
		.count()
		.doOnNext(System.out::println)
        .subscribe();
		
		return "done";
	}
}
