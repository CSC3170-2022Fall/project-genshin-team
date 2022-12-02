package db61b;

import org.junit.Test;
import static org.junit.Assert.*;
import db61b.Table;
import db61b.Row;

public class Table_test {
    @Test
    public void test_Table_Column_Size() {
        db61b.Table table = new db61b.Table(new String[] { "student", "id", "college", "school", "Year" });
        assertEquals(5, table.columns());
    }

    @Test
    public void test_Table_Column_Title() {
        db61b.Table table = new db61b.Table(new String[] { "Student", "id", "college", "school", "Year" });
        assertEquals("Student", table.getTitle(0));
        assertNotEquals("Student", table.getTitle(1));

        assertEquals(1, table.findColumn("id"));
        assertNotEquals(-1, table.findColumn("college"));
    }

    @Test
    public void test_Table_add_Row_1() {
        db61b.Table table = new db61b.Table(new String[] { "student", "id", "college", "school", "Year" });

        db61b.Row r = new db61b.Row(new String[] { "Luca", "460", "Muse", "SDS", "2020" });
        assertTrue(table.add(r));
        assertFalse(table.add(r));
    }

    @Test
    public void test_Table_add_Row_2() {
        db61b.Table table = new db61b.Table(new String[] { "student", "id", "college", "school", "Year" });

        db61b.Row r1 = new db61b.Row(new String[] { "Luca", "460", "Muse", "SDS", "2020" });
        db61b.Row r2 = new db61b.Row(new String[] { "Philip", "590", "Muse", "SSE", "2020" });
        assertTrue(table.add(r1));
        assertTrue(table.add(r2));
    }

    @Test
    public void test_Table_row_size() {
        db61b.Table table = new db61b.Table(new String[] { "student", "id", "college", "school", "Year" });

        db61b.Row r1 = new db61b.Row(new String[] { "Luca", "460", "Muse", "SDS", "2020" });
        db61b.Row r2 = new db61b.Row(new String[] { "Philip", "590", "Muse", "SSE", "2020" });
        table.add(r1);
        table.add(r2);

        assertEquals(2, table.size());
    }
}
