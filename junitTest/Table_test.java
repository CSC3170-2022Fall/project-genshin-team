package db61b;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    @Test
    public void test_print() {
        db61b.Table table = db61b.Table.readTable("./testing/enrolled");
        table.print();
        // assert???
    }

    @Test
    public void test_select_1(){
        Table table = new Table(new String[] { "student", "id", "college", "school", "Year" });

        Row r1 = new Row(new String[] { "Luca", "460", "Muse", "SDS", "2020" });
        Row r2 = new Row(new String[] { "Philip", "590", "Muse", "SSE", "2020" });
        table.add(r1);
        table.add(r2);
        List<String> ColumnName1 = new ArrayList<String>();
        ColumnName1.add("student");
        Column col1 = new Column("student", table);
        // Condition condition1 = new Condition(col1, "=", col1);
        List<Condition> conditionList = new ArrayList<Condition>();
        // conditionList.add(condition1);
        Table table3 = table.select(ColumnName1, conditionList);
        table3.print();
    }

    @Test
    public void test_select_2(){
        Table table_sample_1 = new Table(new String[] { "student", "id", "college", "school", "Year" });
        Table table_sample_2 = new Table(new String[] { "id", "grade" });

        Row r1 = new Row(new String[] { "Luca", "460", "Muse", "SDS", "2020" });
        Row r2 = new Row(new String[] { "Philip", "590", "Muse", "SSE", "2020" });
        table_sample_1.add(r1);
        table_sample_1.add(r2);

        Row r3 = new Row(new String[] { "460", "A"});
        Row r4 = new Row(new String[] { "590", "B"});
        table_sample_2.add(r4);
        table_sample_2.add(r3);


        Column col1 = new Column("student", table_sample_1, table_sample_2);
        Column col2 = new Column("grade", table_sample_1, table_sample_2);
        Column col3 = new Column("id", table_sample_1);
        Column col4 = new Column("id", table_sample_2);
        List<Column> columnList = new ArrayList<Column>();
        columnList.add(col1);
        columnList.add(col2);
        List<String> colNames = new ArrayList<String>();
        colNames.add("student");
        colNames.add("grade");
        Condition cond3 = new Condition(col3, "=", col4);
        List<Condition> conditionList = new ArrayList<Condition>();
        conditionList.add(cond3);
        Table table_result = table_sample_1.select(table_sample_2, colNames, conditionList);
        for (int i = 0; i < table_result.columns(); i++){
            System.out.printf("%s\t", table_result.getTitle(i));
        }
        System.out.println();

        Iterator<Row> it = table_result.iterator();
        while(it.hasNext()){
            Row temp_row = it.next();
            for (int i = 0; i < temp_row.size(); i++){
                System.out.printf("%s\t", temp_row.get(i));
            }
            System.out.println();
        }
    }

    @Test
    public void test_equijoin(){
        Table table_sample_1 = new Table(new String[] { "student", "id", "college", "school", "Year" });
        Table table_sample_2 = new Table(new String[] { "id", "grade" });

        Row r1 = new Row(new String[] { "Luca", "460", "Muse", "SDS", "2020" });
        Row r2 = new Row(new String[] { "Philip", "590", "Muse", "SSE", "2020" });
        table_sample_1.add(r1);
        table_sample_1.add(r2);

        Row r3 = new Row(new String[] { "460", "A"});
        Row r4 = new Row(new String[] { "590", "B"});
        table_sample_2.add(r4);
        table_sample_2.add(r3);

        Column col1 = new Column("id", table_sample_1);
        Column col2 = new Column("id", table_sample_2);
        Column col3 = new Column("student", table_sample_1);

        List<Column> colList1 = new ArrayList<Column>();
        List<Column> colList2 = new ArrayList<Column>();
        List<Column> colList3 = new ArrayList<Column>();

        colList1.add(col1);
        colList2.add(col2);

        assertEquals(true, Table.equijoin_test(colList1, colList2, r1, r3));
        assertEquals(false, Table.equijoin_test(colList1, colList2, r1, r4));

    }
}
