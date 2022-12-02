package db61b;

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

    @Test
    public void test_Row_Equal_1 () {
        db61b.Row r = new db61b.Row(new String[] { "a", "b", "c", "d", "e" });
        db61b.Row d = new db61b.Row(new String[] { "a", "b", "c", "d", "f" });
        assertEquals(false, r.equals(d));
    }

    @Test
    public void test_Row_Equal_2 () {
        db61b.Row r = new db61b.Row(new String[] { "a", "b", "c", "d", "e" });
        db61b.Row d = new db61b.Row(new String[] { "a", "b", "c", "d", "e", "f"});
        assertEquals(false, r.equals(d));
    }

    @Test
    public void test_Row_Equal_3 () {
        db61b.Row r = new db61b.Row(new String[] { "a", "b", "c", "d", "e" });
        String[] d = new String[] { "a", "b", "c", "d", "e", "f"};
        assertEquals(false, r.equals(d));
    }


}