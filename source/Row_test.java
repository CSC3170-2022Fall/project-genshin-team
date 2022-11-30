package db61b;

import org.junit.Test;
import static org.junit.Assert.*;


public class Row_test {
    @Test
    public void tesetRow() {
        Row r = new Row(new String[] { "a", "b", "c", "d", "e" });
        assertEquals(5, r.size());
    }
    
    public static void main(String[] args){
        System.exit();
    }
}