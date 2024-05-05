package live.manager.utils;

import java.util.concurrent.Executor;


public class SharedExecutor implements Executor {

	private final Executor executor;

    public SharedExecutor(Executor executor) {
        this.executor = executor;
    }
    
    @Override
    public void execute(Runnable command) {
        Runnable wrappedCommand = () -> {
            try {
                // Set up Mockito context or Mocks here
                command.run();
            } finally {
                // Cleaning will wipe all mocked objects from threads
            }
        };
        executor.execute(wrappedCommand);
    }
}