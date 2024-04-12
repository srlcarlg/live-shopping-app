package live.manager.utils;

import java.security.SecureRandom;

import jakarta.enterprise.context.Dependent;

@Dependent
public class RandomGenerator {

    private final String CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    protected SecureRandom secureRandom = new SecureRandom();

    public String randomString(int lenght) {
        StringBuilder stringBuilder = new StringBuilder(lenght);
        for (int i = 0; i < lenght; i++) {
        	stringBuilder.append(CHARS.charAt(secureRandom.nextInt(CHARS.length())));
        }
        return stringBuilder.toString();
    }
}
