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
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static db61b.Utils.*;
import static org.junit.Assert.assertEquals;

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
        this._columnTitle = columnTitles;
    }

    /** A new Table whose columns are give by COLUMNTITLES. */
    Table(List<String> columnTitles) {
        this(columnTitles.toArray(new String[columnTitles.size()]));
    }

    /** Return the number of columns in this table. */
    public int columns() {
//        TODO FINISH
        return this._columnTitle.length;
        // REPLACE WITH SOLUTION
    }

    /** Return the title of the Kth column.  Requires 0 <= K < columns(). */
    public String getTitle(int k) {
//        TODO FINISH
        if (k < 0 || k >= this.columns()) {
            throw db61b.Utils.error("The index : %d is out of range", k);
        }

        return this._columnTitle[k];
        // REPLACE WITH SOLUTION
    }

    /** Return the index of the intended title of certain column, or -1 if
     *  there isn't one. */
    public int findColumn(String title) {
//        TODO FINISH
        int count = 0;
        for (String element: this._columnTitle) {
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
        return this._rows.size();  // REPLACE WITH SOLUTION
    }

    /** Returns an iterator that returns my rows in an unspecified order. */
    @Override
    public Iterator<Row> iterator() {
        return this._rows.iterator();
    }

    /** Add ROW to THIS if no equal row already exists.  Return true if anything
     *  was added, false otherwise. */
    public boolean add(Row row) {
//        TODO FINISH
        if (!this._rows.contains(row)) {
            this._rows.add(row);
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

            for (Row eachRow : this._rows) {
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

        // row content, default block size = 8, extend if needed by 8
        int[] length_index = getLengthIndex();

        // horizontal divide line
        this.printTitle(length_index);

        // horizontal divide line

        for (Row eachRow : this._rows) {
            eachRow.printRow(length_index);
        }

        // horizontal divide line
        System.out.print("+");
        for (int i = 0; i < this.columns(); i++) {
            int block_size = length_index[i]/7;
            while(block_size >= 0){
                System.out.print("-------");
                block_size -= 1;
            }
            System.out.print("-+");
        }
        System.out.println();
    }

    int[] getLengthIndex() {
        int max_length = 0;
        int temp_length = 0;
        int[] length_index = new int[this.columns()];

        //get max length of all data that needs to be printed
        for (Row eachRow : this._rows) {
            // init max_length for every column
            for (int i = 0; i < this.columns(); i++) {
                max_length = length_index[i];
                temp_length = eachRow.get(i).length();
                if (temp_length >= max_length){
                    max_length = temp_length;
                    length_index[i] = max_length;
                }
            }
        }

        for (int i = 0; i < this.columns(); i++) {
            max_length = length_index[i];
            temp_length = this.getTitle(i).length();
            if (temp_length >= max_length){
                max_length = temp_length;
                length_index[i] = max_length;
            }
        }
        return length_index;
    }

    void sortAndPrint(String columnName, boolean order) {
//                TODO FINISH
//      * sort the rows
        ArrayList<Row> rows = new ArrayList<>(){};
        rows.addAll(this._rows);

        int columnNumber = this.findColumn(columnName);
        if (columnNumber == -1) {
            throw error("cannot order by an non-existing column %s\n", columnName);
        }
        rows.sort((row1, row2) -> {
            Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
            if (pattern.matcher(row1.get(columnNumber)).matches()) {
                return Integer.compare(Integer.parseInt(row1.get(columnNumber)), Integer.parseInt(row2.get(columnNumber)));
            } else {
                return row1.get(columnNumber).compareTo(row2.get(columnNumber));
            }
        });

        if (!order) {
            Collections.reverse(rows);
        }
//      * print the sorted rows
        this.printArray(rows);
    }

    void printArray(ArrayList<Row> sortedTable) {
        // row content, default block size = 8, extend if needed by 8
        int max_length = 0;
        int temp_length = 0;
        int[] length_index = this.getLengthIndex();

        // horizontal divide line
        printTitle(length_index);

        for (Row eachRow : sortedTable) {
            eachRow.printRow(length_index);
        }

        // horizontal divide line
        System.out.print("+");
        for (int i = 0; i < this.columns(); i++) {
            int block_size = length_index[i]/7;
            while(block_size >= 0){
                System.out.print("-------");
                block_size -= 1;
            }
            System.out.print("-+");
        }
        System.out.println();
    }

    void printTitle(int[] length_index) {
        System.out.print("+");
        for (int i = 0; i < this.columns(); i++) {
            int block_size = length_index[i]/7;
            while(block_size >= 0){
                System.out.print("-------");
                block_size -= 1;
            }
            System.out.print("-");
            System.out.print("+");
        }
        System.out.println();

        for (int i = 0; i < this.columns(); i++) {
            int max_block_number = length_index[i]/7 + 1;
            int current_block_number = this.getTitle(i).length()/7;
            if(this.getTitle(i).length()%7 != 0){
                current_block_number += 1;       // length
            }
            int size_diff_block = max_block_number - current_block_number;
            int size_diff_str = length_index[i] - this.getTitle(i).length();

            System.out.printf("| %-7s", this.getTitle(i));
            while(size_diff_block != 0){
                System.out.printf("       ");   //7 empty space
                size_diff_block -= 1;
            }

            if (this.getTitle(i).length() > 7){
                int size_offset = 7 - this.getTitle(i).length() % 7;
                if (this.getTitle(i).length() % 7 == 0){
                    size_offset -= 7;
                }
                while(size_offset > 0){
                    System.out.printf(" ");
                    size_offset -= 1;
                }
            }
        }
        System.out.println("|");

        // horizontal divide line
        System.out.print("+");
        for (int i = 0; i < this.columns(); i++) {
            int block_size = length_index[i]/7;
            while(block_size >= 0){
                System.out.print("-------");
                block_size -= 1;
            }
            System.out.print("-");
            System.out.print("+");
        }
        System.out.println();
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
            }

        }

        Iterator<Row> it_rows_1 = this.iterator();
        while(it_rows_1.hasNext()){
            Iterator<Row> it_rows_2 = table2.iterator();
            Row temp_row_1 = it_rows_1.next();
            while(it_rows_2.hasNext()){
                Row temp_row_2 = it_rows_2.next();

                // if nothing after "where", take equijoin as the condition
                String colName = "";
                boolean flag = false;
                for(int i = 0; i < this.columns(); i++){
                    for(int j = 0; j < table2.columns(); j++){
                        if (this.getTitle(i).compareTo((table2.getTitle(j))) == 0){
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
                    if (conditions.size() == 0){
                        result.add(new Row(temp_columns, temp_row_1, temp_row_2));
                    }
                    else{
//                        System.out.printf("%s, %s :: %s\n", temp_row_1.get(0), temp_row_1.get(1), temp_row_2.get(0));
//                        assertEquals(true, Condition.test(conditions, temp_row_1, temp_row_2));
                        if(Condition.test(conditions, temp_row_1, temp_row_2)){
                            // System.out.println("Ever reach here");
                            result.add(new Row(temp_columns, temp_row_1, temp_row_2));
                        }
                    }
                }

            }
        }
        return result;
    }

    ArrayList<LinkedHashSet<Row>> group(String groupColumnName) {
        ArrayList<LinkedHashSet<Row>> groupRow = new ArrayList<LinkedHashSet<Row>>();

        int column = this.findColumn(groupColumnName);
        if (column == -1) {
            throw error("cannot group by an non-existing column %s\n", groupColumnName);
        }

        HashMap<String, Integer> columnValue = new HashMap<>();

        for (Row rowElement : this._rows) {
            if (columnValue.containsKey(rowElement.get(column))) {
                groupRow.get(columnValue.get(rowElement.get(column))).add(rowElement);
            } else {
                columnValue.put(rowElement.get(column), groupRow.size());
                LinkedHashSet<Row> sameValueRow = new LinkedHashSet<>();
                sameValueRow.add(rowElement);
                groupRow.add(sameValueRow);
            }
        }
        return groupRow;
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


