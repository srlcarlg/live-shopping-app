package live.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import live.chat.grpc.LiveGrpcService;

@RestController
public class HelloController {

	@Autowired
	LiveGrpcService clientService;

	@GetMapping("/hello")
	public String handle() {
		clientService.findBySlug("SkyMEjFArfSxw3");
		clientService.validate("SkyMEjFArfSxw3", "123456");
		return "done";
	}
}
