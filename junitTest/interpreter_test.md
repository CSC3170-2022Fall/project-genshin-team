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
  + select Firstname, Lastname, Grade from students, enrolled where CCN = '21001' and SID = SID;
  + select Firstname, Lastname from students, enrolled2;
  + select Firstname, Lastname from students, enrolled order by Lastname;
  + select Firstname, Lastname from students, enrolled order by Firstname desc;

## Test failed:
  + ~~**select SID, Lastname, from students;**~~ 

  + ~~**select SID, Lastname from students;**~~

  + ~~**select Lastname from students;**~~ 
     (There is case that when two students' last name is '**Chan**')

  +  ~~**load testing/enrolled;** (Error: unexpected token: 'testing')~~

  + **~~select Firstname, Grade from students, enrolled;~~**

     It seems Okay in this case, but the result is still not sure.
  + **~~select Firstname, Lastname, Grade from students, enrolled where CCN = '21001' and SID = SID;~~**
  
    (Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: Index 1 out of bounds for length 1
     Row and condition have bugs.)
  + **create table enrolled2 as select SID from enrolled, schedule where Dept = 'EECS' and Num = '61A';**

    (The print result should be 101 102 104 105 106 but ours is 101 102 103 104 105 106)
  + ~~**load testing/students**~~
  + ~~**quit;**~~
  + ~~**insert into test values '1', '2', '3';**~~
  + select SID from students ... ;
  + "search result" come first and "..." come next
