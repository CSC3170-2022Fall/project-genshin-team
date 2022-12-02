package db61b;

import org.junit.Test;
import static org.junit.Assert.*;


public class Row_test {
    @Test
    public void test_Row_Size() {
        db61b.Row r = new db61b.Row(new String[] { "a", "b", "c", "d", "e" });
        assertEquals(5, r.size());
    }

    @Test
    public void test_Row_Get() {
        db61b.Row r = new db61b.Row(new String[] { "a", "b", "c", "d", "e" });
        assertEquals("c", r.get(2));
    }

    @Test
    public void test_Row_Equal_1 () {
        db61b.Row r1 = new db61b.Row(new String[] { "a", "b", "c", "d", "e" });
        db61b.Row r2 = new db61b.Row(new String[] { "a", "b", "c", "d", "f" });
        db61b.Row r3 = new db61b.Row(new String[] { "a", "b", "c", "d", "e", "f"});
        db61b.Row r4 = new db61b.Row(new String[] { "a", "b", "c", "d", "e" });
        String[] rowContent = new String[] { "a", "b", "c", "d", "e"};

//    * IF there is multiple test in one function
//    * All the tests will be tested

//    * But I still recommend to chunk it to different test function
        assertNotEquals(r1, r2);
        assertFalse(r1.equals(r3));
//    * The junit test will stop at the next line if next line is not commented
//        assertNotEquals(r1, r4);
        assertFalse(r1.equals(rowContent));
    }

    @Test
    public void test_Row_Equal_2 () {
        db61b.Row r = new db61b.Row(new String[] { "a", "b", "c", "d", "e" });
        db61b.Row d = new db61b.Row(new String[] { "a", "b", "c", "d", "e", "f"});
        assertFalse(r.equals(d));
    }

    @Test
    public void test_Row_Equal_3 () {
        db61b.Row r = new db61b.Row(new String[] { "a", "b", "c", "d", "e" });
        String[] rowContent = new String[] { "a", "b", "c", "d", "e"};
        assertFalse(r.equals(rowContent));
    }


}