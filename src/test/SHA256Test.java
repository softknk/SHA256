package test;

import de.softknk.InputSizeTooLargeException;
import de.softknk.SHA256;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SHA256Test {

    private SHA256 sha;

    private static final String a = "abcdefghijklmnopqrstuvwxyz";
    private static final String b = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor " +
            "invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam " +
            "et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem " +
            "ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy " +
            "eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos " +
            "et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
    private static final String c = "This is a random message\tI just want to test my SHA-256 algorithm!";

    @BeforeAll
    public void init() {
        sha = new SHA256();
    }

    @Test
    public void hash_test() throws InputSizeTooLargeException {
        Assertions.assertEquals("71c480df93d6ae2f1efad1447c66c9525e316218cf51fc8d9ed832f2daf18b73", sha.hash(a));
        Assertions.assertEquals("ff4ef4245da5b09786e3d3de8b430292fa081984db272d2b13ed404b45353d28", sha.hash(b));
        Assertions.assertEquals("f1f2cc55a31f74e1d08296ba6e1ae0596b2777b8ccc6b65c0b32a6823c443768", sha.hash(c));
    }
}
