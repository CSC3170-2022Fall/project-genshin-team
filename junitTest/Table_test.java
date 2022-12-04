package db61b;

import org.junit.Test;
import static org.junit.Assert.*;

public class Table_test {
    @Test
    public void test_Table_Column_Size() {
        db61b.Table table = new db61b.Table(new String[] { "student", "id", "college", "school", "Year" });
        assertEquals(5, table.columns());
    }

    @Test
    public void test_Table_Column_Title_1() {
        db61b.DBException thrown = assertThrows(db61b.DBException.class,
                () -> {
                        db61b.Table table = new db61b.Table(new String[] { "college", "id", "college", "school", "Year" });
                    }
                );
        assertEquals("duplicate column name: college", thrown.getMessage());
    }

    @Test
    public void test_Table_Column_Title_2() {
        db61b.Table table = new db61b.Table(new String[] { "Student", "id", "college", "school", "Year" });
        assertEquals("Student", table.getTitle(0));
        assertNotEquals("Student", table.getTitle(1));
        db61b.DBException thrown = assertThrows(db61b.DBException.class, () -> table.getTitle(-1));
        assertEquals("The index : -1 is out of range", thrown.getMessage());
        assertNotEquals("The index : -2 is out of range", thrown.getMessage());

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
    public void test_read_Table() {
        db61b.Table table = db61b.Table.readTable("./testing/enrolled");
        table.writeTable("./TestResult/outputTable2");
    }

}
