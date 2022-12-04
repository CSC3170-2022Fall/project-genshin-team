package db61b;

import org.junit.Test;

import static org.junit.Assert.*;

public class Database_test {
    @Test
    public void test_Table_Database_Construct() {
        db61b.Database db = new db61b.Database();
        assertNotEquals(null, db);
    }

    @Test
    public void test_Table_Database_Put1() {
        db61b.Table table = new db61b.Table(new String[] { "student", "id", "college", "school", "Year" });
        db61b.Database db = new db61b.Database();
        //db.put("Students",null);
        db61b.DBException thrown = assertThrows(db61b.DBException.class, () -> db.put("Students",null));
        assertEquals("null argument", thrown.getMessage());
    }

    @Test
    public void test_Table_Database_Put2() {
        db61b.Table table = new db61b.Table(new String[] { "student", "id", "college", "school", "Year" });
        db61b.Database db = new db61b.Database();
        //db.put(null,table);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> db.put(null, table));
        assertEquals("null argument", thrown.getMessage());
    }

    @Test
    public void test_Table_Database_Get() {
        db61b.Table table = new db61b.Table(new String[] { "student", "id", "college", "school", "Year" });
        db61b.Database db = new db61b.Database();
        db.put("Students",table);
        assertEquals(table, db.get("Students"));
    }
    @Test
    public void test_Table_Database_Get_Null() {
        db61b.Table table = new db61b.Table(new String[] { "student", "id", "college", "school", "Year" });
        db61b.Database db = new db61b.Database();
        db.put("Students",table);
        assertEquals(null, db.get("Student"));  // type a wrong table name
    }


}
