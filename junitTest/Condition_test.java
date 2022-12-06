package db61b;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class Condition_test {

    @Test
    public void condition_test_test() {
        Table table = new Table(new String[] { "student", "id", "college", "school", "Year" });

        Row r1 = new Row(new String[] { "Luca", "460", "Muse", "SDS", "2020" });
        Row r2 = new Row(new String[] { "Philip", "590", "Muse", "SSE", "2020" });
        Row r3 = new Row(new String[] {"Luca", "111"});
        table.add(r1);
        table.add(r2);
        Column col1 = new Column("student", table);
        Column col2 = new Column("id", table);
        Condition cond1 = new Condition(col1, "=", "Luca");
        Condition cond2 = new Condition(col2, "=", "460");

        // test student = Luca
        List<Condition> condList1 = new ArrayList<Condition>();
        condList1.add(cond1);
        assertTrue(Condition.test(condList1, r3));
        assertTrue(Condition.test(condList1, r1));
        assertNotEquals(true, Condition.test(condList1, r2));

        // test id = 590
        condList1.add(cond2);
        assertNotEquals(true, Condition.test(condList1, r3));
        assertTrue(Condition.test(condList1, r1));
    }
}
