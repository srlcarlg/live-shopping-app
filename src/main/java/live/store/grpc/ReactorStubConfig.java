package live.store.grpc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import live.store.grpc.chat.ReactorChatServiceGrpc.ReactorChatServiceStub;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.client.inject.GrpcClientBean;

@Configuration
@GrpcClientBean(
    clazz = ReactorChatServiceStub.class,
    beanName = "reactorStub",
    client = @GrpcClient("something")
)
public class ReactorStubConfig {

    @Bean
    ChatGrpcService chatGrpcService(@Autowired ReactorChatServiceStub reactorStub) {
        return new ChatGrpcService(reactorStub);
    }

}