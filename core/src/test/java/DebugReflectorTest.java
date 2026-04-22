import io.github.some_example_name.DebugReflector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DebugReflectorTest {

    static class Foo {
        private int a = 1;
        private String b = "x";
    }

    @Test
    void dumpNull() {
        assertEquals("null", DebugReflector.dump(null));
    }

    @Test
    void dumpObjectContainsClassNameAndFields() {
        String s = DebugReflector.dump(new Foo());
        assertTrue(s.contains(Foo.class.getName()));
        assertTrue(s.contains("a = 1"));
        assertTrue(s.contains("b = x"));
    }
}
