package live.chat.grpc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import live.chat.grpc.live.ReactorLiveServiceGrpc.ReactorLiveServiceStub;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.client.inject.GrpcClientBean;

@Configuration
@GrpcClientBean(
    clazz = ReactorLiveServiceStub.class,
    beanName = "reactorStub",
    client = @GrpcClient("something")
)
public class ReactorStubConfig {

    @Bean
    LiveGrpcService liveGrpcService(@Autowired ReactorLiveServiceStub reactorStub) {
        return new LiveGrpcService(reactorStub);
    }

}