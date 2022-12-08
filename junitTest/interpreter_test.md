# This is the file to test the commands 
since it is hard to write junit for CommandInterpreter.java. We use a markdown file to track all the test case.

## Test passed: 
  1. load students;
  2. print students;

## Test failed:
  1. **select SID, Lastname, from students;** (Error: unexpected token: 'students')

  2. **select SID, Lastname from students;** 
     (Exception in thread "main" java.lang.NullPointerException: Cannot invoke "db61b.Table.iterator()" because "table2" is null
     at db61b.Table.select(Table.java:242)
     at db61b.CommandInterpreter.selectClause(CommandInterpreter.java:306)
     at db61b.CommandInterpreter.selectStatement(CommandInterpreter.java:253)
     at db61b.CommandInterpreter.statement(CommandInterpreter.java:160)
     at db61b.Main.main(Main.java:25))

  3. **select Lastname from students;** 
     (Exception in thread "main" java.lang.NullPointerException: Cannot invoke "db61b.Table.iterator()" because "table2" is null
     at db61b.Table.select(Table.java:242)
     at db61b.CommandInterpreter.selectClause(CommandInterpreter.java:306)
     at db61b.CommandInterpreter.selectStatement(CommandInterpreter.java:253)
     at db61b.CommandInterpreter.statement(CommandInterpreter.java:160)
     at db61b.Main.main(Main.java:25))

  4.  **load testing/enrolled;** (Error: unexpected token: 'testing')

  5. **select Firstname, Grade from students, enrolled;** 
     (Exception in thread "main" java.lang.NullPointerException: Cannot invoke "java.util.List.iterator()" because "conditions" is null
     at db61b.Condition.test(Condition.java:78)
     at db61b.Table.select(Table.java:246)
     at db61b.CommandInterpreter.selectClause(CommandInterpreter.java:306)
     at db61b.CommandInterpreter.selectStatement(CommandInterpreter.java:253)
     at db61b.CommandInterpreter.statement(CommandInterpreter.java:160)
     at db61b.Main.main(Main.java:25))
