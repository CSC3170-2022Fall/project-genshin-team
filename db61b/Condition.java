// This is a SUGGESTED skeleton for a class that describes a single
// Condition (such as CCN = '99776').  You can throw this away if you
// want,  but it is a good idea to try to understand it first.
// Our solution changes or adds about 30 lines in this skeleton.

// Comments that start with "//" are intended to be removed from your
// solutions.
package db61b;

import java.util.List;
import java.util.regex.Pattern;

import db61b.Column;
import db61b.Table;
import db61b.Row;

/** Represents a single 'where' condition in a 'select' command.
 *  @author */
class Condition {

    /** A Condition representing COL1 RELATION COL2, where COL1 and COL2
     *  are column designators. and RELATION is one of the
     *  strings "<", ">", "<=", ">=", "=", or "!=". */
    Condition(Column col1, String relation, Column col2) {
//        TODO FINISH
        // YOUR CODE HERE
        _col1 = col1;
        _relation = relation;
        _col2 = col2;
    }

    /** A Condition representing COL1 RELATION 'VAL2', where COL1 is
     *  a column designator, VAL2 is a literal value (without the
     *  quotes), and RELATION is one of the strings "<", ">", "<=",
     *  ">=", "=", or "!=".
     */
    Condition(Column col1, String relation, String val2) {
        this(col1, relation, (Column) null);
        _val2 = val2;
    }

    /** Assuming that ROWS are rows from the respective tables from which
     *  my columns are selected, returns the result of performing the test I
     *  denote. */
    boolean test(Row... rows) {
//        TODO FINISH
        // REPLACE WITH SOLUTION
        String c1, c2;
        c1 = _col1.getFrom(rows);
        if (_col2 != null) {
            c2 = _col2.getFrom(rows);

        }
        else{
            c2 = _val2;
        }

        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        int result = -1;
        if (pattern.matcher(c1).matches()) {
            if (c1.contains(".") || c2.contains(".")) {
                result = Double.compare(Double.parseDouble(c1), Double.parseDouble(c2));
            } else {
                result = Integer.compare(Integer.parseInt(c1), Integer.parseInt(c2));
            }
        } else {
            result = c1.compareTo(c2);
        }
//        int result = c1.compareTo(c2);
        if (_relation.equals("<")) {
            return (result == -1);
        }
        if (_relation.equals(">")) {
            return (result == 1);
        }
        if (_relation.equals("<=")) {
            return (result <= 0);
        }
        if (_relation.equals(">=")) {
            return (result >= 0);
        }
        if (_relation.equals("=")) {
            return (result == 0);
        }
        if (_relation.equals("!=")) {
            return (result != 0);
        }
        if (_relation.equals("notIn")) {
            return (result != 0);
        }
        if (_relation.equals("in")){
            return (result == 0);
        }
        return false;
    }

    /** Return true iff ROWS satisfies all CONDITIONS. */
    static boolean test(List<Condition> conditions, Row... rows) {
        for (Condition cond : conditions) {
            if (!cond.test(rows)) {
                return false;
            }
        }
        return true;
    }

    /** The operands of this condition.  _col2 is null if the second operand
     *  is a literal. */
    private Column _col1, _col2;
    /** Second operand, if literal (otherwise null). */
    private String _val2;
    // ADD ADDITIONAL FIELDS HERE
//    TODO FINISH
    private String _relation;
}
