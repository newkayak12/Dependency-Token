package tokenManagerTest;

import exception.TokenException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledIf;
import tokenManager.TokenControl;

import java.util.HashMap;
import java.util.Map;

public class TokenControlTest {
    private TokenControl tokenControl;

    @BeforeEach
    public void setUp() {
         tokenControl = new TokenControl("/Users/sanghyeonkim/Downloads/port/netflixClone/jwt", "test", "test");
    }
    @Test
    @DisplayName("Encrypt")
    public void encryptTest () {
        Assertions.assertNotNull(
                tokenControl.encrypt(
                        new HashMap<String, Integer>(){{
                            put("a", 1);
                            put("b", 2);
                            put("c", 3);
                        }}
                )
        );

    }
    @Test
    @DisplayName("Decrypt")
    public void decryptTest () throws TokenException {
        String token = "Bearer eyJ0eXAiOiJ0ZXN0IiwiYWxnIjoiSFM1MTIifQ.eyJhIjoxLCJiIjoyLCJpc3MiOiJ0ZXN0IiwiaWF0IjoxNjg1ODgwNzU3fQ.OV627PEYQ4b5BISiYfZBxo3C35VIcsM-Pjuky-2kni8lQ-Ah0ftc1gOsyIywEDlCbBrqewtn0hp9nRt-4A9OLQ";
        Assertions.assertNotNull(
                tokenControl.decrypt(token, HashMap.class)
        );
    }
}
