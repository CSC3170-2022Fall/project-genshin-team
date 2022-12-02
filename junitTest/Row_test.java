package db61b;

import org.junit.Test;
import static org.junit.Assert.*;
import db61b.Row;


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
        Row r1 = new Row(new String[] { "a", "b", "c", "d", "e" });
        Row r2 = new Row(new String[] { "a", "b", "c", "d", "f" });
        Row r3 = new Row(new String[] { "a", "b", "c", "d", "e", "f"});
        Row r4 = new Row(new String[] { "a", "b", "c", "d", "e" });
        String[] rowContent = new String[] { "a", "b", "c", "d", "e"};

//    * IF there is multiple test in one function
//    * All the tests will be tested

//    * But I still recommend to chunk it to different test function
        assertNotEquals(r1, r2);
        assertFalse(r1.equals(r3));
//    * The junit test will stop at the next line if next line is not commented
//        assertNotEquals(r1, r4);
        assertFalse(r1.equals(rowContent));
        assertEquals(r1, r4);
    }

    @Test
    public void test_Row_Equal_2 () {
        Row r = new Row(new String[] { "a", "b", "c", "d", "e" });
        Row d = new Row(new String[] { "a", "b", "c", "d", "e", "f"});
        assertFalse(r.equals(d));
    }

    @Test
    public void test_Row_Equal_3 () {
        Row r = new Row(new String[] { "a", "b", "c", "d", "e" });
        String[] rowContent = new String[] { "a", "b", "c", "d", "e"};
        assertFalse(r.equals(rowContent));
    }


}