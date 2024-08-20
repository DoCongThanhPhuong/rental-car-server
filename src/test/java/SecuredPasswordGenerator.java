import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SecuredPasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "thong1111";

        System.out.println(encoder.encode(password));
    }

    @Test
    public void checkPassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String plainText = "JOpf!*4L0H";
        String cipherText = "$2a$10$tTEgtrRtC1iKgl9M3q866ORvNEWrsVfui8GfSaL9EyfkQUTgW0nHK";

        assertTrue(encoder.matches(plainText, cipherText));
    }
}
