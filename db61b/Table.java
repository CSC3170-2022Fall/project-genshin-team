// This is a SUGGESTED skeleton for a class that represents a single
// Table.  You can throw this away if you want, but it is a good
// idea to try to understand it first.  Our solution changes or adds
// about 100 lines in this skeleton.

// Comments that start with "//" are intended to be removed from your
// solutions.
package db61b;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static db61b.Utils.*;
import db61b.Row;
import db61b.Column;
import db61b.Condition;
import db61b.DBException;

/** A single table in a database.
 *  @author P. N. Hilfinger
 */
class Table implements Iterable<Row> {
    /** A new Table whose columns are given by COLUMNTITLES, which may
     *  not contain duplicate names. */
    Table(String[] columnTitles) {
        for (int i = columnTitles.length - 1; i >= 1; i -= 1) {
            for (int j = i - 1; j >= 0; j -= 1) {
                if (columnTitles[i].equals(columnTitles[j])) {
                    throw db61b.Utils.error("duplicate column name: %s",
                                columnTitles[i]);
                }
            }
        }
        // FILL IN
//        TODO FINISH
        _columnTitle = columnTitles;
    }

    /** A new Table whose columns are give by COLUMNTITLES. */
    Table(List<String> columnTitles) {
        this(columnTitles.toArray(new String[columnTitles.size()]));
    }

    /** Return the number of columns in this table. */
    public int columns() {
//        TODO FINISH
        return _columnTitle.length;
        // REPLACE WITH SOLUTION
    }

    /** Return the title of the Kth column.  Requires 0 <= K < columns(). */
    public String getTitle(int k) {
//        TODO FINISH
        if (k < 0 || k >= this.columns()) {
            throw db61b.Utils.error("The index : %d is out of range", k);
        }

        return _columnTitle[k];
        // REPLACE WITH SOLUTION
    }

    /** Return the index of the intended title of certain column, or -1 if
     *  there isn't one. */
    public int findColumn(String title) {
//        TODO FINISH
        int count = 0;
        for (String element: _columnTitle) {
            if (title.equals(element)) {
                return count;
            }
            count += 1;
        }
        return -1;  // REPLACE WITH SOLUTION
    }

    /** Return the number of Rows in this table. */
    public int size() {
//        TODO FINISH
        return _rows.size();  // REPLACE WITH SOLUTION
    }

    /** Returns an iterator that returns my rows in an unspecified order. */
    @Override
    public Iterator<Row> iterator() {
        return _rows.iterator();
    }

    /** Add ROW to THIS if no equal row already exists.  Return true if anything
     *  was added, false otherwise. */
    public boolean add(Row row) {
//        TODO FINISH
        if (!_rows.contains(row)) {
            _rows.add(row);
            return true;
        }
        return false;   // REPLACE WITH SOLUTION
    }

    /** Read the contents of the file NAME.db, and return as a Table.
     *  Format errors in the .db file cause a DBException. */
    static Table readTable(String name) {
        BufferedReader input;
        Table table;
        input = null;
        table = null;
        try {
            input = new BufferedReader(new FileReader(name + ".db"));
            String header = input.readLine();
            if (header == null) {
                throw db61b.Utils.error("missing header in DB file");
            }
            String[] columnNames = header.split(",");
            // FILL IN
//            TODO FINISH
            table = new Table(columnNames);

            String data = input.readLine();
            while (data != null) {
                table.add(new Row(data.split(",")));
                data = input.readLine();
            }
        } catch (FileNotFoundException e) {
            throw db61b.Utils.error("could not find %s.db", name);
        } catch (IOException e) {
            throw db61b.Utils.error("problem reading from %s.db", name);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    /* Ignore IOException */
                }
            }
        }
        return table;
    }

    /** Write the contents of TABLE into the file NAME.db. Any I/O errors
     *  cause a DBException. */
    void writeTable(String name) {
        PrintStream output;
        output = null;
        try {
            String sep;
            sep = "";
            output = new PrintStream(name + ".db");
            // FILL THIS IN
//            TODO FINISH
            for (int i = 0; i < this.columns() - 1; i++) {
                output.print(this.getTitle(i));
                output.print(',');
            }

            output.print(this.getTitle(this.columns() - 1) + '\n');

            for (Row eachRow : _rows) {
                for (int i = 0; i < this.columns() - 1; i++) {
                    output.print(eachRow.get(i));
                    output.print(',');
                }
                output.print(eachRow.get(this.columns() - 1) + '\n');
            }
        } catch (IOException e) {
            throw db61b.Utils.error("trouble writing to %s.db", name);
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

    /** Print my contents on the standard output. */
    void print() {
//        TODO FINISH
        for (int i = 0; i < this.columns() - 1; i++) {
            System.out.print(this.getTitle(i));
            System.out.print(',');
        }

        System.out.print(this.getTitle(this.columns() - 1) + '\n');

        for (Row eachRow : _rows) {
            for (int i = 0; i < this.columns() - 1; i++) {
                System.out.print(eachRow.get(i));
                System.out.print(',');
            }
            System.out.print(eachRow.get(this.columns() - 1) + '\n');
        }
    }

    /** Return a new Table whose columns are COLUMNNAMES, selected from
     *  rows of this table that satisfy CONDITIONS. */
    Table select(List<String> columnNames, List<Condition> conditions) {
        Table result = new Table(columnNames);
//        TODO FINISH

        // create List<Column> variable
        List<Column> temp_columns = new ArrayList<Column>();
        Iterator<String> it_columnNames = columnNames.iterator();
        while(it_columnNames.hasNext()){
            String temp_name = it_columnNames.next();
            temp_columns.add(new Column(temp_name, this));
        }

        Iterator<Row> it_rows = this.iterator();
        while(it_rows.hasNext()){
            /* if the content of certain row achieve the requirement,
             * add it into the result*/
            Row temp_row = it_rows.next();

            // if nothing after "where", add all rows of column in result
            if (conditions.size() == 0){
                result.add(new Row(temp_columns, temp_row));
            }
            else{
                if(Condition.test(conditions, temp_row)){
                    result.add(new Row(temp_columns, temp_row));
                }
            }

        }
        return result;
    }

    /** Return a new Table whose columns are COLUMNNAMES, selected
     *  from pairs of rows from this table and from TABLE2 that match
     *  on all columns with identical names and satisfy CONDITIONS. */
    Table select(Table table2, List<String> columnNames,
                 List<Condition> conditions) {
        Table result = new Table(columnNames);
//        TODO FINISH
        // create List<Column> variable
        List<Column> temp_columns = new ArrayList<Column>();
        Iterator<String> it_columnNames = columnNames.iterator();
        while(it_columnNames.hasNext()){
            String temp_name = it_columnNames.next();
            if (conditions.size() == 0){
                // natural join situation
                temp_columns.add(new Column(temp_name, this));
            }
            else{
                temp_columns.add(new Column(temp_name, this, table2));
                System.out.println(temp_columns.get(0).getName());
            }

        }

        Iterator<Row> it_rows_1 = this.iterator();
        while(it_rows_1.hasNext()){
            Iterator<Row> it_rows_2 = table2.iterator();
            Row temp_row_1 = it_rows_1.next();
            while(it_rows_2.hasNext()){
                Row temp_row_2 = it_rows_2.next();

                // if nothing after "where", take equijoin as the condition
                if(conditions.size() == 0){
                    String colName = "";
                    boolean flag = false;
                    for(int i = 0; i < this.columns(); i++){
                        for(int j = 0; j < table2.columns(); j++){
                            if (this.getTitle(i).equals(table2.getTitle(j))){
                                colName = this.getTitle(i);
                                flag = true;
                                break;
                            }
                        }
                        if (flag){
                            break;
                        }
                    }
                    Column col1 = new Column(colName, this);
                    Column col2 = new Column(colName, table2);
                    List<Column> common1 = new ArrayList<Column>();
                    List<Column> common2 = new ArrayList<Column>();
                    common1.add(col1);
                    common2.add(col2);
                    if(equijoin(common1, common2, temp_row_1, temp_row_2)){
                        result.add(new Row(temp_columns, temp_row_1, temp_row_2));
                    }
                }
                else{
                    if (temp_row_1.get(1).compareTo(temp_row_2.get(0)) == 0){
                        if(temp_row_2.get(2).compareTo("EECS") == 0){
                            if(temp_row_2.get(1).compareTo("61A") == 0){
                                System.out.printf("%s, %s :: %s\n", temp_row_1.get(0), temp_row_1.get(1), temp_row_2.get(0));
                            }
                        }
                    }
                    // System.out.printf("%s, %s :: %s\n", temp_row_1.get(0), temp_row_1.get(1), temp_row_2.get(0));
                    if(Condition.test(conditions, temp_row_1, temp_row_2)){
                        System.out.println("Ever reach here");
                        result.add(new Row(temp_columns, temp_row_1, temp_row_2));
                    }
                }
            }
        }
        return result;
    }

    /** Return true if the columns COMMON1 from ROW1 and COMMON2 from
     *  ROW2 all have identical values.  Assumes that COMMON1 and
     *  COMMON2 have the same number of elements and the same names,
     *  that the columns in COMMON1 apply to this table, those in
     *  COMMON2 to another, and that ROW1 and ROW2 come, respectively,
     *  from those tables. */
    private static boolean equijoin(List<Column> common1, List<Column> common2,
                                    Row row1, Row row2) {
//        TODO
        Iterator<Column> it1 = common1.iterator();
        Iterator<Column> it2 = common2.iterator();
        while(it1.hasNext()){
            Column col1 = it1.next();
            while(it2.hasNext()){
                Column col2 = it2.next();
                if(!col1.getFrom(row1).equals(col2.getFrom(row2))){
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean equijoin_test(List<Column> common1, List<Column> common2,
                                Row row1, Row row2){
        return equijoin(common1, common2, row1, row2);
    }

    /** My rows. */
    private HashSet<Row> _rows = new HashSet<>();
    // FILL IN
//    TODO FINISH
    private String[] _columnTitle;
}

