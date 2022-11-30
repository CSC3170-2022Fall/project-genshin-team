package source;

import org.junit.Test;
import static org.junit.Assert.*;


public class Row_test {
    @Test
    public void test_Row_Size() {
        Row r = new Row(new String[] { "a", "b", "c", "d", "e" });
        assertEquals(5, r.size());
    }

    @Test
    public void test_Row_Get() {
        Row r = new Row(new String[] { "a", "b", "c", "d", "e" });
        assertEquals("c", r.get(2));
    }
}