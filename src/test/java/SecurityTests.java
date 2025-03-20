import com.example.constructionsystem.AESUtil;
import com.example.constructionsystem.LoginController;
import com.example.constructionsystem.SessionManager;
import com.mysql.cj.log.Log;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class SecurityTests {

    @Test
    public void testEncryptionAndDecryption() throws Exception {
        String originalText = "Test1234";
        String encryptedText = AESUtil.encrypt(originalText);
        String decryptedText = AESUtil.decrypt(encryptedText);

        assertNotNull(encryptedText);
        assertNotNull(decryptedText);
        assertEquals(originalText, decryptedText);
    }

    //dla BCrypt'a
    @Test
    void testPasswordHashing() {
        String password = "SecurePass123";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        assertNotNull(hashedPassword, "Zahashowane hasło nie powinno być null!");
        assertTrue(BCrypt.checkpw(password, hashedPassword), "Hasło powinno być poprawnie zweryfikowane!");
    }

    @Test
    void testUserCannotLoginDuringLockout() {
        // blokujemy użytkownika
        LoginController.failedAttempts = LoginController.MAX_ATTEMPTS;
        LoginController.blockEndTime = Instant.now().plusSeconds(600); // Blokada na 10 minut

        assertTrue(LoginController.isUserBlocked(), "Użytkownik powinien być nadal zablokowany!");
    }
    @Test
    void testUserCanLoginAfterLockoutExpires() {
        // symulacja blokady (czas blokady minął)
        LoginController.blockCount = LoginController.MAX_BLOCKS;
        LoginController.blockEndTime = Instant.now().minusSeconds(3600);

        assertTrue(LoginController.isUserBlocked(), "Użytkownik powinien być nadal zablokowany!");
    }


}
