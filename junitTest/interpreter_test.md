# This is the file to test the commands 
since it is hard to write junit for CommandInterpreter.java. We use a markdown file to track all the test case.


## Test passed:
  + load students;
  + print students;
  + load testing/students;
  + quit;
  + create table test (col1, col2);
  + insert into test values '1', '2';
  + insert into test values 'A', 'B', 'C';
  + store students;
  + store TestResult/students;

## Test failed:
  + ~~**select SID, Lastname, from students;**~~ 

  + ~~**select SID, Lastname from students;**~~

  + **select Lastname from students;** 
     (There is case that when two students' last name is '**Chan**')

  +  ~~**load testing/enrolled;** (Error: unexpected token: 'testing')~~

  + **select Firstname, Grade from students, enrolled;** 
     (Exception in thread "main" java.lang.NullPointerException: Cannot invoke "java.util.List.iterator()" because "conditions" is null
     at db61b.Condition.test(Condition.java:78)
     at db61b.Table.select(Table.java:246)
     at db61b.CommandInterpreter.selectClause(CommandInterpreter.java:306)
     at db61b.CommandInterpreter.selectStatement(CommandInterpreter.java:253)
     at db61b.CommandInterpreter.statement(CommandInterpreter.java:160)
     at db61b.Main.main(Main.java:25))
  + **select Firstname, Lastname, Grade from students, enrolled where CCN = '21001' and SID = SID;**
    (Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: Index 1 out of bounds for length 1
     Row and condition have bugs.)

  + ~~**load testing/students**~~
  + ~~**quit;**~~
  + ~~**insert into test values '1', '2', '3';**~~
  + select SID from students ... ;
  + "search result" come out and "..." come next
