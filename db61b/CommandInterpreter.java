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
            default:
                throw error("unrecognizable command");
        }
        return true;
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
        System.out.printf("Contents test of %s:%n", tableName);
        table_buffer.print();
    }

    /** Parse and execute a select statement from the token stream. */
    void selectStatement() {//no problem, problem is in selectClause()!
        // FILL THIS IN
//        TODO FINISH
        System.out.println("Search results:");
//        selectClause().print();
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

        if (_input.nextIf("order")){
            if (_input.nextIf("by")) {
                String columnTitle = _input.next();
                boolean order = !_input.nextIf("desc");
                if (whetherGroup) {
//                    TODO if order and group
                    for (int i = 0; i < table.columns() - 1; i++) {
                        System.out.print(table.getTitle(i));
                        System.out.print(',');
                    }
                    System.out.print(table.getTitle(table.columns() - 1) + '\n');
                    this.sortAndPrint(groupRow, table.findColumn(columnTitle), order, table.findColumn(groupColumnName));
                } else {
                    table.sortAndPrint(columnTitle, order);
                }
            } else {
                throw error("The correct syntax should order by <attr>");
            }
        } else {
            if (whetherGroup) {
                for (int i = 0; i < table.columns() - 1; i++) {
                    System.out.print(table.getTitle(i));
                    System.out.print(',');
                }
                System.out.print(table.getTitle(table.columns() - 1) + '\n');
                this.printArraySet(groupRow);
            } else {
                table.print();
            }
        }
        _input.next(";");
    }

    private void sortAndPrint(ArrayList<LinkedHashSet<db61b.Row>> groupRow, int columnNumber, boolean order, int groupColumn) {
        if (columnNumber == groupColumn) {
//            * thus we need to sort the group column which is defined by included as arrayList
            groupRow.sort((set1, set2) -> {
                Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
                Iterator<db61b.Row> rowIterator1 = set1.iterator();
                Iterator<db61b.Row> rowIterator2 = set2.iterator();
                Row row1 = rowIterator1.next();
                Row row2 = rowIterator2.next();
                if (pattern.matcher(row1.get(columnNumber)).matches()) {
                    return Integer.compare(Integer.parseInt(row1.get(columnNumber)), Integer.parseInt(row2.get(columnNumber)));
                } else {
                    return row1.get(columnNumber).compareTo(row2.get(columnNumber));
                }
            });
            if (!order) {
                Collections.reverse(groupRow);
            }
            this.printArraySet(groupRow);
        } else {
            for (LinkedHashSet<Row> arrayElement : groupRow) {
                arrayElement.stream().sorted((row1, row2) -> {
                    Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
                    if (pattern.matcher(row1.get(columnNumber)).matches()) {
                        int compareResult = Integer.compare(Integer.parseInt(row1.get(columnNumber)), Integer.parseInt(row2.get(columnNumber)));
                        return (order)? compareResult:(-compareResult);
                    } else {
                        int compareResult =  row1.get(columnNumber).compareTo(row2.get(columnNumber));
                        return (order)? compareResult:(-compareResult);
                    }
                }).forEach(
                        (eachRow)->{
                            for (int i = 0; i < eachRow.size() - 1; i++) {
                                System.out.print(eachRow.get(i));
                                System.out.print(',');
                            }
                        System.out.print(eachRow.get(eachRow.size() - 1) + '\n');
                });
            }
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
//        TODO
        _input.next("select");
        ArrayList<String> arrayColumn=new ArrayList<String>();
//        TODO The first column should not be started with ","
        /*
         * The example is
         * select SID, Firstname from students where Lastname ="Chan";
         * */
        arrayColumn.add(this.columnName());    //列的名字的array
        while (_input.nextIf(",")) {
            arrayColumn.add(this.columnName());
        }
        _input.next("from");

        Table table0 = this.tableName(); //第一个table
        Table table1 = null;
        if (_input.nextIf(",")) {//如果有“，” 则有table1
            table1=this.tableName();
        }
        ArrayList<Condition> arrayCondition;
        if (table1 == null) {
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

    void printArraySet(ArrayList<LinkedHashSet<Row>> groupRow) {
        for (HashSet<Row>arrayElement: groupRow) {
            for (Row eachRow: arrayElement) {
                for (int i = 0; i < eachRow.size() - 1; i++) {
                    System.out.print(eachRow.get(i));
                    System.out.print(',');
                }
                System.out.print(eachRow.get(eachRow.size() - 1) + '\n');
            }
        }
    }

    /** The command input source. */
    private db61b.Tokenizer _input;
    /** Database containing all tables. */
    private db61b.Database _database;
}

