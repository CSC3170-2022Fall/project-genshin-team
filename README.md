[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-c66648af7eb3fe8bc4f294546bfd86ef473780cde1dea487d3c4ff354943c9ae.svg)](https://classroom.github.com/online_ide?assignment_repo_id=9422221&assignment_repo_type=AssignmentRepo)
# CSC3170 Course Project

## Project Overall Description

This is our implementation for the course project of CSC3170, 2022 Fall, CUHK(SZ). For details of the project, you can refer to [project-description.md](project-description.md). In this project, we will utilize what we learned in the lectures and tutorials in the course, and implement either one of the following major job:

<!-- Please fill in "x" to replace the blank space between "[]" to tick the todo item; it's ticked on the first one by default. -->

- [ ] **Application with Database System(s)**
- [x] **Implementation of a Database System**

## Team Members

Our team consists of the following members, listed in the table below (the team leader is shown in the first row, and is marked with 🚩 behind his/her name):

<!-- change the info below to be the real case -->

| Student ID | Student Name | GitHub Account (in Email)  | GitHub User                                   |
| ---------- | ------------ | -------------------------  |-----------------------------------------------|
| 120090590  | 陈飞飏 🚩    | chenfeiyang852@163.com     | [CowboyPhilip](https://github.com/CowboyPhilip) | 
| 120010042  | 陶震         | 1072149188@qq.com          | [ffffffklj](https://github.com/ffffffklj)     |
| 120090460  | 朱骆锴       | 120090460@link.cuhk.edu.cn | [LucaZhu0219](https://github.com/LucaZhu0219) |
| 120090582  | 陈宣文       | 1548622359@qq.com          |                                               |
| 119010166  | 李卓毅       | 463776850@qq.com           |                                               |
| 120090447  | 王奕文       | 120090447@link.cuhk.edu.cn |                                               |
| 120090089  | 王雨奇       | 120090089@link.cuhk.edu.cn |                                               |
| 120090588  | 肖玮钊       | 1003669366@qq.com          |                                               |

## Project Specification

<!-- You should remove the terms/sentence that is not necessary considering your option/branch/difficulty choice -->

After thorough discussion, our team made the choice and the specification information is listed below:

- Our option choice is: **Option 3**
- The difficulty level is: **None**

## Project Abstract

<!-- TODO -->
1. Project Background 
   In this project, we will attempt to create a database management system (DBMS) using static programming language (Java). We will give the database the basic functions required, while adding more convenient and eﬀicient instructions based on our technology and ideas.

   

   ---

   

2. Project Overview
   1. Basic Contents:
      Our project object is a miniature relational database management system (DBMS) that stores tables of data in which we will set some number of labeled columns of data information. Our system will include a very simple query language for extracting information from these tables.

      

   2. Basic Functions:
      We will prepare the following basic functions under the framework of the database system built in the project.
   
      1. **Database** The function first creates an empty database. Then we can use the get function to enter the table name to quickly find the return table or null, or use the put function to legally change the table name.
      2. **Row** We will create a class named row. Users can set and update the value of the row through the function, and return the relevant information of the row (e.g.number,value). We also added equals function to judge by hashcode.
      3. **Table** This function contains the basic functions associated with Table. Users can find and return labels, values, and so on for specified rows and columns. We set up the function of reading and output table, and print the search results in standard form; At the same time, the filter table function is set to filter the tables that meet the requirements based on the row and column values.
      4. **Interpreter** We create an interpreter that uses the recursive descent principle, converts the bnf syntax, unties the input side, and uses tokens to create statements from the remaining sequences and perform operations, returning values. Specifically, we will prepare table(eg.create, exit, insert, load, store, print, select) for the statement.
      5. **Condition** We will set the corresponding relation for different conditions (e.g.val2,col2). We also added a test program to judge if rows satisfies all conditions.
   
   
   
   ---

   

3. Project Extensions
   We would like to add some extensions of our own ideas to the features required by the project.

   + **Basic Mathematical Functions** We will add ”Max”, ”Min”, ”Avg” and other basic mathematical functions to calculate the statistical information of rows and columns

   + **More Information** We will set more signature information(e.g.primary key) for the table, make it easier for the user to find. We will also try to add sort to rows and columns. In addition, we will also try to incorporate group statistics features such as group by.

   + **Multi-Select** In order to get the table with the required information step by step, we will expand on the basis of select so that the value returned by the lookup can be multi-tiered and step-by-step.

   + **Join of Tables** We will use different connection modes to connect the required information according to user requirements (e.g.left join, right join).

   + **GUI** We will try to create a fancy and user-friendly GUI that makes it easy for users to select the features they need, with helpful tips（e.g. Show possible paths as users type).