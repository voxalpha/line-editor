Line editor
----------

Usage
-----

1. `mvn exec:java`
2. `mvn exec:java -Dexec.args="src/main/resources/z.txt"`
3. 
`mvn clean package`

`cd bin`

`line-editor.bat ../src/main/resources/z.txt`

Commands
--------
Editor loads the file passed as 1st command line parameter to the application

**list** - show content

**ins n some text** - insert some text in row n

**del n** - delete row n

**save** - saves content to the same file from where it was loaded

**quit** - terminate application  

 

Original Requirements
---------------------
Write a line-oriented text editor that reads a text file and allows basic editing commands
 
Usage:
lineeditor c:\temp\myfile.txt
(displays a >> prompt)
 
Commands:
list - list each line in n:xxx format, e.g.
1: first line
2: second line
3: last line
del n - delete line at n
ins n <string> - insert a line at n(row number)
save - saves to disk
quit - quits the editor and returns to the command line
 
Execute: 
Create bat or sh file to start app with ability to add text file as first parameter.
Please, upload your solution to some public file/code repository instead of sending as an attachment.

 

