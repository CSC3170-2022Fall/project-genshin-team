// This is a SUGGESTED skeleton for a class that parses and executes database
// statements.  Be sure to read the STRATEGY section, and ask us if you have any
// questions about it.  You can throw this away if you want, but it is a good
// idea to try to understand it first.  Our solution adds or changes about 50
// lines in this skeleton.

// Comments that start with "//" are intended to be removed from your
// solutions.
package db61b;

import java.io.PrintStream;

import java.util.*;
import java.util.regex.Pattern;


import static db61b.Utils.*;
import static db61b.Tokenizer.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import db61b.Tokenizer;
import db61b.Database;
import db61b.Table;
import db61b.Row;
import db61b.Condition;
import db61b.DBException;
import db61b.Column;

/** An object that reads and interprets a sequence of commands from an
 *  input source.
 *  @author */
class CommandInterpreter {

    /* STRATEGY.
     *
     *   This interpreter parses commands using a technique called
     * "recursive descent." The idea is simple: we convert the BNF grammar,
     * as given in the specification document, into a program.
     *
     * First, we break up the input into "tokens": strings that correspond
     * to the "base case" symbols used in the BNF grammar.  These are
     * keywords, such as "select" or "create"; punctuation and relation
     * symbols such as ";", ",", ">="; and other names (of columns or tables).
     * All whitespace and comments get discarded in this process, so that the
     * rest of the program can deal just with things mentioned in the BNF.
     * The class Tokenizer performs this breaking-up task, known as
     * "tokenizing" or "lexical analysis."
     *
     * The rest of the parser consists of a set of functions that call each
     * other (possibly recursively, although that isn't needed for this
     * particular grammar) to operate on the sequence of tokens, one function
     * for each BNF rule. Consider a rule such as
     *
     *    <create statement> ::= create table <table name> <table definition> ;
     *
     * We can treat this as a definition for a function named (say)
     * createStatement.  The purpose of this function is to consume the
     * tokens for one create statement from the remaining token sequence,
     * to perform the required actions, and to return the resulting value,
     * if any (a create statement has no value, just side-effects, but a
     * select clause is supposed to produce a table, according to the spec.)
     *
     * The body of createStatement is dictated by the right-hand side of the
     * rule.  For each token (like create), we check that the next item in
     * the token stream is "create" (and report an error otherwise), and then
     * advance to the next token.  For a metavariable, like <table definition>,
     * we consume the tokens for <table definition>, and do whatever is
     * appropriate with the resulting value.  We do so by calling the
     * tableDefinition function, which is constructed (as is createStatement)
     * to do exactly this.
     *
     * Thus, the body of createStatement would look like this (_input is
     * the sequence of tokens):
     *
     *    _input.next("create");
     *    _input.next("table");
     *    String name = name();
     *    Table table = tableDefinition();
     *    _input.next(";");
     *
     * plus other code that operates on name and table to perform the function
     * of the create statement.  The .next method of Tokenizer is set up to
     * throw an exception (DBException) if the next token does not match its
     * argument.  Thus, any syntax error will cause an exception, which your
     * program can catch to do error reporting.
     *
     * This leaves the issue of what to do with rules that have alternatives
     * (the "|" symbol in the BNF grammar).  Fortunately, our grammar has
     * been written with this problem in mind.  When there are multiple
     * alternatives, you can always tell which to pick based on the next
     * unconsumed token.  For example, <table definition> has two alternative
     * right-hand sides, one of which starts with "(", and one with "as".
     * So all you have to do is test:
     *
     *     if (_input.nextIs("(")) {
     *         _input.next("(");
     *         // code to process "<column name>,  )"
     *     } else {
     *         // code to process "as <select clause>"
     *     }
     *
     * As a convenience, you can also write this as
     *
     *     if (_input.nextIf("(")) {
     *         // code to process "<column name>,  )"
     *     } else {
     *         // code to process "as <select clause>"
     *     }
     *
     * combining the calls to .nextIs and .next.
     *
     * You can handle the list of <column name>s in the preceding in a number
     * of ways, but personally, I suggest a simple loop:
     *
     *     ... = columnName();
     *     while (_input.nextIs(",")) {
     *         _input.next(",");
     *         ... = columnName();
     *     }
     *
     * or if you prefer even greater concision:
     *
     *     ... = columnName();
     *     while (_input.nextIf(",")) {
     *         ... = columnName();
     *     }
     *
     * (You'll have to figure out what do with the names you accumulate, of
     * course).
     */


    /** A new CommandInterpreter executing commands read from INP, writing
     *  prompts on PROMPTER, if it is non-null. */
    CommandInterpreter(Scanner inp, PrintStream prompter) {
        _input = new Tokenizer(inp, prompter);
        _database = new Database();
        _funcCalls = new ArrayList<String>();
    }

    /** Parse and execute one statement from the token stream.  Return true
     *  iff the command is something other than quit or exit. */
    boolean statement() {
        switch (_input.peek()) {
            case "create":
                createStatement();
                break;
            case "load":             //no problem
                loadStatement();
                break;
            case "exit": case "quit"://no problem
                exitStatement();
                return false;
            case "*EOF*":            //no problem
                return false;
            case "insert":
                insertStatement();
                break;
            case "print":            //no problem
                printStatement();
                break;
            case "select":
                selectStatement();
                break;
            case "store":
                storeStatement();
                break;
            case "union":
                unionStatement();
                break;
            default:
                throw error("unrecognizable command");
        }
        return true;
    }

    void unionStatement() {
        _input.next("union");
        Table table0 = this.tableName();
        while (_input.nextIf(",")) {
            Table tableNext = this.tableName();
            table0 = table0.union(tableNext);
        }
        _input.next(";");
    }

    /** Parse and execute a create statement from the token stream. */
    void createStatement() {
        _input.next("create");
        _input.next("table");
        String name = this.name();//name of the newly created table
        Table table = tableDefinition();//the newly created table
//        TODO FINISH
        _database.put(name, table);
        _input.next(";");
    }

    /** Parse and execute an exit or quit statement. Actually does nothing
     *  except check syntax, since statement() handles the actual exiting. */
    void exitStatement() {
        _input.next();
        if (!_input.nextIf(";")) {
//            _input.next("exit");
            throw error ("the exit statement should be 'quit;' or 'exit;'");
        }
//        _input.next(";");
    }

    /** Parse and execute an insert statement from the token stream. */
    void insertStatement() {
        _input.next("insert");
        _input.next("into");
        Table table = this.tableName();
        _input.next("values");

        ArrayList<String> values = new ArrayList<>();
        values.add(this.literal());
        while (_input.nextIf(",")) {
            values.add(this.literal());
        }

        if (values.size() == table.columns()) {
            table.add(new Row(values.toArray(new String[values.size()])));
        } else {
            throw error("the input size should be the same as the column number which is %d", table.columns());
        }

        _input.next(";");
    }

    /** Parse and execute a load statement from the token stream. */
    void loadStatement() {
        // FILL THIS IN
//        TODO FINISH
        _input.next("load");
        String nameOfTable = "";
        String lastNext = _input.peek();
        while (!_input.nextIf(";")) {
            lastNext = _input.peek();
            nameOfTable = nameOfTable + _input.next();
        }
        String name_buffer = lastNext;
        Table table_buffer = Table.readTable(nameOfTable);
        _database.put(name_buffer, table_buffer);
        System.out.printf("Loaded %s.db%n", name_buffer);
    }

    /** Parse and execute a store statement from the token stream. */
    void storeStatement() {
        _input.next("store");
        String lastNext = _input.peek();
        String nameOfTable = "";
        Table table;
        while (true) {
            if (!_input.nextIs(";")) {
                lastNext = _input.peek();
                nameOfTable = nameOfTable + _input.next();
            } else {
                table = this.prevTokenTable();
                break;
            }
        }
        // FILL THIS IN
//        TODO FINISH
        String name = lastNext;
        table.writeTable(nameOfTable);
        System.out.printf("Stored %s.db%n", name);
        _input.next(";");
    }

    /** Parse and execute a print statement from the token stream. */
    void printStatement() {
        // FILL THIS IN
//        TODO FINISH
        _input.next("print");
        String tableName = _input.peek();
        Table table_buffer=tableName();
        _input.next(";");
        System.out.printf("Contents of %s:%n", tableName);
        table_buffer.print();
    }

    /** Parse and execute a select statement from the token stream. */
    void selectStatement() {
        // FILL THIS IN
//        TODO FINISH
        System.out.println("Search results:");
        Table table = selectClause();
        ArrayList<LinkedHashSet<Row>> groupRow = new ArrayList<LinkedHashSet<Row>>();
        boolean whetherGroup = false;
        String groupColumnName = "";
        if (_input.nextIf("group")) {
            if (_input.nextIf("by")) {
                groupColumnName = _input.next();
                whetherGroup = true;
                groupRow = table.group(groupColumnName);
            } else {
                throw error("The correct syntax should group by <attr>");
            }
        }


        if (_funcCalls.size() != 0){
            ArrayList<ArrayList<String>> groupRowForFunc = new ArrayList<ArrayList<String>>();
            if (whetherGroup){
//                TODO Here is the problem
                int rowSize = groupRow.get(0).iterator().next().size();
                for (int i = 0; i < rowSize; i++) {
                    if (_funcPos.contains(i)) {
                        switch (_funcCalls.get(i)) {
                            case "max":
                                groupRowForFunc.add(maxCall(groupRow, i));
                                break;
                            case "min":
                                groupRowForFunc.add(minCall(groupRow, i));
                                break;
                            case "avg":
                                groupRowForFunc.add(avgCall(groupRow, i));
                                break;
                            case "sum":
                                groupRowForFunc.add(sumCall(groupRow, i));
                                break;
                            case "count":
                                groupRowForFunc.add(countCall(groupRow, i));
                                break;
                        }
                    } else {
                        String[] groupColumn = new String[groupRow.size()];
                        Arrays.fill(groupColumn, groupRow.get(0).iterator().next().get(i));

                        groupRowForFunc.add(new ArrayList<>(Arrays.asList(groupColumn)));
//                        Directly add the column
                    }
                }
//                convert the array array to array hashset

                for (int i = 0; i < groupRowForFunc.get(0).size(); i++) {
                    String[] rowString = new String[groupRowForFunc.size()];
                    for (int j = 0; j < groupRowForFunc.size(); j++) {
                        ArrayList<String> rowArray = new ArrayList<>();
                        rowArray.add(groupRowForFunc.get(j).get(i));
                        rowArray.toArray(rowString);
                    }

                    groupRow.get(i).clear();
                    groupRow.get(i).add(new Row(rowString));
                }
            } else {
                ;
            }

        }    // need perform function call
//
////                ! Here we should loop the functionCalls
//                int i = 0;
//
//                switch (_funcCalls.get(i)
//                ){
//                    case "max":
//                        groupRow = maxCall(groupRow);
//                        break;
//                    case "min":
//                        groupRow = minCall(groupRow);
//                        break;
//                    case "avg":
////                        groupRow = avgCall(groupRow, i);
//                        break;
//                    case "sum":
////                        groupRow = sumCall(groupRow, i);
//                        break;
//                    case "count":
////                        groupRow = countCall(groupRow,i);
//                        break;
//                    default:
//                        throw error("unknown function name!");
//
//                }
//
//
////                groupRow = maxCall(groupRow);          // return a arraylist
//            }else {
////                table = maxCall(table);            // return a Table
//                switch (_funcCalls.get(0)
//                ){
//                    case "max":
//                        table = maxCall(table);
//                        break;
//                    case "min":
//                        table = minCall(table);
//                        break;
//                    case "avg":
////                        table = avgCall(table);
//                        break;
//                    case "sum":
////                        table = sumCall(table);
//                        break;
//                    case "count":
////                        table = countCall(table);
//                        break;
//                    default:
//                        throw error("unknown function name!");
//
//                }
//            }
//        }

        if (_input.nextIf("order")){
            if (_input.nextIf("by")) {
                String columnTitle = _input.next();
                boolean order = !_input.nextIf("desc");
                int[] lengthIndex = table.getLengthIndex();

                table.printTitle(lengthIndex);
                if (whetherGroup) {
//                    TODO if order and group
                    this.sortAndPrint(groupRow, table.findColumn(columnTitle), order, table.findColumn(groupColumnName), lengthIndex);
                } else {
                    table.sortAndPrint(columnTitle, order);
                }
            } else {
                throw error("The correct syntax should order by <attr>");
            }
        } else {
            if (whetherGroup) {
                int[] lengthIndex = table.getLengthIndex();
                table.printTitle(lengthIndex);
                this.printArraySet(groupRow, lengthIndex);
            } else {
                table.print();
            }
        }
        _input.next(";");
    }

    private void sortAndPrint(ArrayList<LinkedHashSet<db61b.Row>> groupRow, int columnNumber, boolean order, int groupColumn, int[]lengthIndex) {
        if (columnNumber == groupColumn) {
//            * thus we need to sort the group column which is defined by included as arrayList
            groupRow.sort((set1, set2) -> {
                Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
                Iterator<db61b.Row> rowIterator1 = set1.iterator();
                Iterator<db61b.Row> rowIterator2 = set2.iterator();
                Row row1 = rowIterator1.next();
                Row row2 = rowIterator2.next();
                if (pattern.matcher(row1.get(columnNumber)).matches()) {
                    if (row1.get(columnNumber).contains(".") || row2.get(columnNumber).contains(".")){
                        return Double.compare(Double.parseDouble(row1.get(columnNumber)), Double.parseDouble(row2.get(columnNumber)));
                    }
                    return Integer.compare(Integer.parseInt(row1.get(columnNumber)), Integer.parseInt(row2.get(columnNumber)));
                } else {
                    return row1.get(columnNumber).compareTo(row2.get(columnNumber));
                }
            });
            if (!order) {
                Collections.reverse(groupRow);
            }
            this.printArraySet(groupRow, lengthIndex);
        } else {
            int rowLength = groupRow.get(0).iterator().next().size();
            for (LinkedHashSet<Row> arrayElement : groupRow) {
                arrayElement.stream().sorted((row1, row2) -> {
                    Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
                    if (pattern.matcher(row1.get(columnNumber)).matches()) {
                        int compareResult = -1;
                        if (row1.get(columnNumber).contains(".") || row2.get(columnNumber).contains(".")){
                            compareResult = Double.compare(Double.parseDouble(row1.get(columnNumber)), Double.parseDouble(row2.get(columnNumber)));
                        } else {
                            compareResult = Integer.compare(Integer.parseInt(row1.get(columnNumber)), Integer.parseInt(row2.get(columnNumber)));
                        }
                        return (order)? compareResult:(-compareResult);
                    } else {
                        int compareResult =  row1.get(columnNumber).compareTo(row2.get(columnNumber));
                        return (order)? compareResult:(-compareResult);
                    }
                }).forEach(
                        (eachRow)->{
                            eachRow.printRow(lengthIndex);
                        }
                );
            }

            System.out.print("+");
            for (int i = 0; i < rowLength; i++) {
                int block_size = lengthIndex[i]/7;
                while(block_size >= 0){
                    System.out.print("-------");
                    block_size -= 1;
                }
                System.out.print("-+");
            }
            System.out.println();
        }
    }


    /** Parse and execute a table definition, returning the specified
     *  table. */
    Table tableDefinition() {//
        Table table;
        if (_input.nextIf("(")) {
//            TODO FINISH
            ArrayList<String> array0=new ArrayList<String>();
            array0.add(columnName());
            while (_input.nextIf(",")) {
                array0.add(this.columnName());
            }
            _input.next(")");
            table=new Table(array0);
        } else {
//            TODO FINISH
            _input.next("as");
            table=selectClause();
        }
        return table;
    }

    /** Parse and execute a select clause from the token stream, returning the
     *  resulting table. */
    Table selectClause() {
//        TODO Here Need to modify
        _input.next("select");
        ArrayList<String> arrayColumn = new ArrayList<String>();
        int allColumn = 0;
        if (_input.nextIf("*")) {
            allColumn = 1;
            _input.next("from");
        } else {
//            _funcCalls.add("null");
            arrayColumn.addAll(readColNames());
        }

//        }

//        ArrayList<String> arrayColumn=new ArrayList<String>();   // array contains column names
//        TODO The first column should not be started with ","
//        /*
//         * The example is
//         * select SID, Firstname from students where Lastname ="Chan";
//         * */
//        arrayColumn.add(this.columnName());    // read next substring conform to pattern identifier and add it to array
//        while (_input.nextIf(",")) {    //  if detect ",", perform last row
//            arrayColumn.add(this.columnName());
//        }
//        _input.next("from");  // check if  next token is "from"



        Table table0 = this.tableName();  //   read first table name
        Table table1 = null;
        if (_input.nextIf(",")) {//如果有“，” 则有table1
            table1=this.tableName();
        }
        ArrayList<Condition> arrayCondition;
        if (table1 == null) {

//            * here is need to perform the notation of select all
            if (allColumn == 1) {
                for (int i = 0; i < table0.columns(); i++) {
                    arrayColumn.add(table0.getTitle(i));
                }
            }
            arrayCondition = conditionClause(table0);
            return table0.select(arrayColumn, arrayCondition);
        } else {
            arrayCondition = conditionClause(table0, table1);
            return table0.select(table1,arrayColumn, arrayCondition);//array1是属性名列表，array2是条件
        }
    }

    /** Parse and return a valid name (identifier) from the token stream. */
    String name() {
        return _input.next(Tokenizer.IDENTIFIER);
    }

    /** Parse and return a valid column name from the token stream. Column
     *  names are simply names; we use a different method name to clarify
     *  the intent of the code. */
    String columnName() {
        return this.name();
    }

    /** Parse a valid table name from the token stream, and return the Table
     *  that it designates, which must be loaded. */
    Table tableName() {
        String name = this.name();
        Table table = _database.get(name);
        if (table == null) {
            throw error("unknown table: %s", name);
        }
        return table;
    }

    Table prevTokenTable() {
        String name = _input.getLastTokenForTable();
        Table table = _database.get(name);
        if (table == null) {
            throw error("unknown table: %s", name);
        }
        return table;
    }

    /** Parse a literal and return the string it represents (i.e., without
     *  single quotes). */
    String literal() {
        String lit = _input.next(Tokenizer.LITERAL);
        return lit.substring(1, lit.length() - 1).trim();
    }

    /** Parse and return a list of Conditions that apply to TABLES from the
     *  token stream.  This denotes the conjunction (`and') zero
     *  or more Conditions. */
    ArrayList<Condition> conditionClause(Table... tables) {//对输入的table根据后面的conditionClauses进行
//        TODO
        ArrayList<Condition> result = new ArrayList<Condition>();//结果
        if (_input.nextIf("where")) {
            result.add(condition(tables));
            while (_input.nextIf("and")) {
                result.add(condition(tables));
            }
        } else {
            //do nothing, so that the result is empty.
        }
        return result;
    }

    /** Parse and return a Condition that applies to TABLES from the
     *  token stream. */
    Condition condition(Table... tables) {
//        TODO FINISH
//        * Here the column name is passed by the this.columnName
        Column column_object=new Column(this.columnName(), tables);
        String r0 = "";
        if (_input.nextIf("not")) {
            _input.next("in");
            r0 = "notIn";
        } else if (_input.nextIf("in")) {
            r0 = "in";
        } else {
            r0 = _input.next(Tokenizer.RELATION);
        }

        if (_input.nextIs(Tokenizer.LITERAL)) {
            return new Condition(column_object, r0, literal());
        } else {
            Column col2 = new Column(this.columnName(), tables);
            return new Condition(column_object, r0, col2);
        }
    }

    /** Advance the input past the next semicolon. */
    void skipCommand() {
        while (true) {
            try {
                while (!_input.nextIf(";") && !_input.nextIf("*EOF*")) {
                    _input.next();
                }
                return;
            } catch (DBException excp) {
                /* No action */
            }
        }
    }

    void printArraySet(ArrayList<LinkedHashSet<Row>> groupRow, int[]lengthIndex) {
        int rowLength = groupRow.get(0).iterator().next().size();
        for (LinkedHashSet<Row> arrayElement : groupRow) {
            for (Row eachRow : arrayElement) {
                eachRow.printRow(lengthIndex);
            }
        }
        System.out.print("+");
        for (int i = 0; i < rowLength; i++) {
            int block_size = lengthIndex[i]/7;
            while(block_size >= 0){
                System.out.print("-------");
                block_size -= 1;
            }
            System.out.print("-+");
        }
        System.out.println();
    }


    ArrayList<String> readColNames(){
        ArrayList<String> arrayColumn=new ArrayList<String>();
        // array contains column names
        int columnNumber = 0;
        readColAndFunc(arrayColumn, columnNumber);
        while (_input.nextIf(",")) {
            //  if detect ",", perform next column
            columnNumber++;
            readColAndFunc(arrayColumn, columnNumber);
        }
        _input.next("from");  // check if  next token is "from"
        return arrayColumn;

    }

    private void readColAndFunc(ArrayList<String> arrayColumn, int columnNumber) throws DBException {
        String[] functionArr = {"max", "min", "avg", "sum", "count"};
        if (Arrays.asList(functionArr).contains(_input.peek())) {
            _funcCalls.add(_input.next());
            if (_input.nextIf("(")) {
                _funcPos.add(columnNumber);
                arrayColumn.add(this.columnName());
                _input.next(")");
            } else {
                throw error("Illegal function format. You should brace the value that you want to perform function");
            }
        } else {
            arrayColumn.add(this.columnName());
        }
    }

    ArrayList<LinkedHashSet<Row>> maxCall(ArrayList<LinkedHashSet<Row>>  groupedRows){

        ArrayList<LinkedHashSet<Row>> result = new ArrayList<LinkedHashSet<Row>>();
        // traverse groupedRows. replace each row group with a single row containing max, min or avg
        Iterator<LinkedHashSet<Row>> it = groupedRows.iterator();
        while(it.hasNext()){
            LinkedHashSet<Row> presentRows = it.next();
            String[] funcField = null;      // allocate for function use, e.g. store temporary min or max values;
            // traverse present row group and update the function field
            Iterator<Row> rowIt = presentRows.iterator();
            while(rowIt.hasNext()){       // iterate rows
                Row presentRow = rowIt.next();
                if (funcField == null){
                    funcField = new String[presentRow.size()];
                    for (int i =0;i<presentRow.size();i++){    // initialize funcField
                        funcField[i] = presentRow.get(i);
                    }
                    continue;
                }
                for (int k=0;k<presentRow.size();k++){      // traverse attributes in the row
                    String data = presentRow.get(k);
                    funcField[k] = selectMax(funcField[k],data);
                }
            }
            LinkedHashSet<Row> tempSet = new LinkedHashSet<>();
            tempSet.add(new Row(funcField));
            result.add(tempSet);

        }
        return result;

    }

    Table maxCall(Table p_table){
        List<String> columnNames = new ArrayList<String>();
        for (int i=0;i<p_table.columns();i++){         // get column names
            columnNames.add(p_table.getTitle(i));
        }
        Table result = new Table(columnNames);        //create the output table, which will contain only 1 row
        String[] funcField = null;      // allocate for function use, e.g. store temporary min or max values;
        Iterator<Row> rowIt = p_table.iterator();
        while(rowIt.hasNext()){
            Row tempRow = rowIt.next();
            if (funcField == null){
                funcField = new String[tempRow.size()];
                for (int k=0;k<tempRow.size();k++){      // initialize function field
                    String data = tempRow.get(k);
                    funcField[k] = data;
                }
                continue;
            }
            for (int k=0;k<tempRow.size();k++){      // traverse attributes in the row
                String data = tempRow.get(k);
                funcField[k] = selectMax(funcField[k],data);
            }
        }

        Row resultRow = new Row(funcField);
        result.add(resultRow);
        return result;
    }

    String selectMax(String s1, String s2){
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (pattern.matcher(s1).matches()) {    // s1 is int
            if (Double.compare(Double.parseDouble(s1), Double.parseDouble(s2)) < 0){
                return s2;
            }else{
                return s1;
            }
        } else {     // s1 is string
            if (s1.compareTo(s2)<0){
                return s2;
            }else{
                return s1;
            }
        }
    }

    String selectMin(String s1, String s2){
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (pattern.matcher(s1).matches()) {    // s1 is int
            if (Double.compare(Double.parseDouble(s1), Double.parseDouble(s2)) < 0){
                return s1;
            }else{
                return s2;
            }
        } else {     // s1 is string
            if (s1.compareTo(s2)<0){
                return s1;
            }else{
                return s2;
            }
        }
    }

    ArrayList<LinkedHashSet<Row>> minCall(ArrayList<LinkedHashSet<Row>>  groupedRows){

        ArrayList<LinkedHashSet<Row>> result = new ArrayList<LinkedHashSet<Row>>();
        // traverse groupedRows. replace each row group with a single row containing max, min or avg
        Iterator<LinkedHashSet<Row>> it = groupedRows.iterator();
        while(it.hasNext()){
            LinkedHashSet<Row> presentRows = it.next();
            String[] funcField = null;      // allocate for function use, e.g. store temporary min or max values;
            // traverse present row group and update the function field
            Iterator<Row> rowIt = presentRows.iterator();
            while(rowIt.hasNext()){       // iterate rows
                Row presentRow = rowIt.next();
                if (funcField == null){
                    funcField = new String[presentRow.size()];
                    for (int i =0;i<presentRow.size();i++){    // initialize funcField
                        funcField[i] = presentRow.get(i);
                    }
                    continue;
                }
                for (int k=0;k<presentRow.size();k++){      // traverse attributes in the row
                    String data = presentRow.get(k);
                    funcField[k] = selectMin(funcField[k],data);
                }
            }
            LinkedHashSet<Row> tempSet = new LinkedHashSet<>();
            tempSet.add(new Row(funcField));
            result.add(tempSet);

        }
        return result;

    }

    Table minCall(Table p_table){
        List<String> columnNames = new ArrayList<String>();
        for (int i=0;i<p_table.columns();i++){         // get column names
            columnNames.add(p_table.getTitle(i));
        }
        Table result = new Table(columnNames);        //create the output table, which will contain only 1 row
        String[] funcField = null;      // allocate for function use, e.g. store temporary min or max values;
        Iterator<Row> rowIt = p_table.iterator();
        while(rowIt.hasNext()){
            Row tempRow = rowIt.next();
            if (funcField == null){
                funcField = new String[tempRow.size()];
                for (int k=0;k<tempRow.size();k++){      // initialize function field
                    String data = tempRow.get(k);
                    funcField[k] = data;
                }
                continue;
            }
            for (int k=0;k<tempRow.size();k++){      // traverse attributes in the row
                String data = tempRow.get(k);
                funcField[k] = selectMin(funcField[k],data);
            }
        }

        Row resultRow = new Row(funcField);
        result.add(resultRow);
        return result;
    }

    ArrayList<String> avgCall(ArrayList<LinkedHashSet<Row>> groupedRows, int columnNumber){

        ArrayList<String> result = new ArrayList<String>();
        // traverse groupedRows. replace each row group with a single row containing max, min or avg
        Iterator<LinkedHashSet<Row>> it = groupedRows.iterator();
        while(it.hasNext()){
            double sum = 0;
            LinkedHashSet<Row> presentRows = it.next();
            int count = presentRows.size();
            Iterator<Row> rowIt = presentRows.iterator();
            while(rowIt.hasNext()){       // iterate rows
                Row presentRow = rowIt.next();
                String data = presentRow.get(columnNumber);
                sum += Double.parseDouble(data);
//                }
            }
            String avgResult = Double.toString(sum / count);
            result.add(avgResult);

        }
        return result;
    }

    ArrayList<String> minCall(ArrayList<LinkedHashSet<Row>> groupedRows, int columnNumber){

        ArrayList<String> result = new ArrayList<String>();
        Iterator<LinkedHashSet<Row>> it = groupedRows.iterator();
        while(it.hasNext()){
            String minValue = "";
            LinkedHashSet<Row> presentRows = it.next();
            Iterator<Row> rowIt = presentRows.iterator();
            while(rowIt.hasNext()){       // iterate rows
                Row presentRow = rowIt.next();
                String data = presentRow.get(columnNumber);
                minValue = selectMin(minValue, data);
            }
            result.add(minValue);

        }
        return result;
    }

    ArrayList<String> maxCall(ArrayList<LinkedHashSet<Row>> groupedRows, int columnNumber){

        ArrayList<String> result = new ArrayList<String>();
        Iterator<LinkedHashSet<Row>> it = groupedRows.iterator();
        while(it.hasNext()){
            String maxValue = "";
            LinkedHashSet<Row> presentRows = it.next();
            Iterator<Row> rowIt = presentRows.iterator();
            while(rowIt.hasNext()){       // iterate rows
                Row presentRow = rowIt.next();
                String data = presentRow.get(columnNumber);
                maxValue = selectMax(maxValue, data);
            }
            result.add(maxValue);

        }
        return result;
    }

    ArrayList<String> sumCall(ArrayList<LinkedHashSet<Row>> groupedRows, int columnNumber){

        ArrayList<String> result = new ArrayList<String>();
        // traverse groupedRows. replace each row group with a single row containing max, min or avg
        Iterator<LinkedHashSet<Row>> it = groupedRows.iterator();
        while(it.hasNext()){
            double sum = 0;
            LinkedHashSet<Row> presentRows = it.next();
//            String[] funcField = null;      // allocate for function use, e.g. store temporary min or max values;
            Iterator<Row> rowIt = presentRows.iterator();
            while(rowIt.hasNext()){       // iterate rows
                Row presentRow = rowIt.next();
                String data = presentRow.get(columnNumber);
                sum += Double.parseDouble(data);
//                }
            }
            String avgResult = Double.toString(sum);
            result.add(avgResult);

        }
        return result;
    }

    ArrayList<String> countCall(ArrayList<LinkedHashSet<Row>> groupedRows, int columnNumber){

        ArrayList<String> result = new ArrayList<String>();
        // traverse groupedRows. replace each row group with a single row containing max, min or avg
        Iterator<LinkedHashSet<Row>> it = groupedRows.iterator();
        while(it.hasNext()){
//            String[] funcField = null;      // allocate for function use, e.g. store temporary min or max values;
            LinkedHashSet<Row> presentRows = it.next();
            int count = presentRows.size();
            String avgResult = Integer.toString(count);
            result.add(avgResult);

        }
        return result;

    }
    /** The command input source. */
    private db61b.Tokenizer _input;
    /** Database containing all tables. */
    private db61b.Database _database;

    private ArrayList<String> _funcCalls;
    private final ArrayList<Integer> _funcPos = new ArrayList<>();
}
