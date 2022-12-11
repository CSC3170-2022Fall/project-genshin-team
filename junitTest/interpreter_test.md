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
  + select SID,Lastname,Firstname,SemEnter,YearEnter,Major from students group by Major;
  + select SID,Lastname,Firstname,SemEnter,YearEnter,Major from students group by YearEnter;
  + select SID,Lastname,Firstname,SemEnter,YearEnter,Major from students group by Major order by SID;
  + select SID,Lastname,Firstname,SemEnter,YearEnter,Major from students group by Major order by Major;
  + create table schedule2 as select CCN, Num, Dept, Time, Room, Sem, Year from schedule where Dept != 'EECS';
  + select Room, Sem, Year from schedule, schedule2 where CCN in CCN;

## Test failed:
  + ~~**select SID, Lastname, from students;**~~ 

  + ~~**select SID, Lastname from students;**~~

  + ~~**select Lastname from students;**~~ 
     (There is case that when two students' last name is '**Chan**')

  +  ~~**load testing/enrolled;** (Error: unexpected token: 'testing')~~

  + **~~select Firstname, Grade from students, enrolled;~~**

  + **~~select Firstname, Lastname, Grade from students, enrolled where CCN = '21001' and SID = SID;~~**
  
  + ~~**create table enrolled2 as select SID from enrolled, schedule where Dept = 'EECS' and Num = '61A';**~~

  + ~~**load testing/students**~~
  + ~~**quit;**~~
  + ~~**insert into test values '1', '2', '3';**~~
  + select Room, Sem, Year from schedule, schedule2 where CCN not in CCN;
  + select Room, Sem, Year from schedule, schedule2 where CCN != CCN;
  + select SID from students ... ;
  + "search result" come out and "..." come next
