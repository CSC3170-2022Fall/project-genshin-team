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
  + select SID, Firstname from students where Lastname = 'Chan';
  + create table students as select SID, Lastname, Firstname, SemEnter, YearEnter, Major from students where SID != '103';
  + create table enrolled as select SID, CCN, Grade from enrolled where SID != '103';

## Test failed:
  + ~~**select SID, Lastname, from students;**~~ 

  + ~~**select SID, Lastname from students;**~~

  ~~+ **select Lastname from students;**~~ 
     (There is case that when two students' last name is '**Chan**')

  +  ~~**load testing/enrolled;** (Error: unexpected token: 'testing')~~

  + **select Firstname, Grade from students, enrolled;**

     It seems Okay in this case, but the result is still not sure.
  + **select Firstname, Lastname, Grade from students, enrolled where CCN = '21001' and SID = SID;**
  
    (Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: Index 1 out of bounds for length 1
     Row and condition have bugs.)
  + **create table enrolled2 as select SID from enrolled, schedule where Dept = 'EECS' and Num = '61A';**

    (Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: Index 1 out of bounds for length 1
    at db61b.Column.getFrom(Column.java:49)
    at db61b.Condition.test(Condition.java:46)
    at db61b.Condition.test(Condition.java:79)
    at db61b.Table.select(Table.java:282)
    at db61b.CommandInterpreter.selectClause(CommandInterpreter.java:325)
    at db61b.CommandInterpreter.tableDefinition(CommandInterpreter.java:292)
    at db61b.CommandInterpreter.createStatement(CommandInterpreter.java:176)
    at db61b.CommandInterpreter.statement(CommandInterpreter.java:143)
    at db61b.Main.main(Main.java:25))
  + ~~**load testing/students**~~
  + ~~**quit;**~~
  + ~~**insert into test values '1', '2', '3';**~~
  + select SID from students ... ;
  + "search result" come out and "..." come next
