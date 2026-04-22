import io.github.some_example_name.Holder;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HolderTest {
    @Test
    void getSetWorks() {
        Holder<Integer> h = new Holder<>(1);
        assertEquals(1, h.get());
        h.set(2);
        assertEquals(2, h.get());
    }
}
