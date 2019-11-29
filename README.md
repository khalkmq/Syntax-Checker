# Syntax-Checker

## Description:
The main goal of this project is to build a simple syntax checker implemented using java for a minified version of the C langauge (Mini-C)

What it does:
1) Read an input mini-C source program using jflex for tokenizing. 
2) Check if the input program satisfies the (given subset) of mini-C grammar rules. 
3) Parse the input program using recursive predictive parser. 
4) If there is no syntax error, program should print “Success: no syntax error found.”; otherwise should print the line and column numbers of the first error and its corresponding message. 

## How it works:

1. Clone this into your source directory

2. Download jflex-1.X.X.jar from https://jflex.de/download.html and place it in the source directory.

3. Open your terminal and cd to your source directory.

4. Compile Lexer.jflex as follows, make sure your jflex number is the same as the one you downloaded:
```
java -jar jflex-1.X.X.jar Lexer.flex
```
5. Execute the following YACC command (this particular one of for linux based operating systems)
```
./yacc.linux -Jthrows="Exception" -Jextends=ParserImpl -Jnorun -J ./Parser.y
```
6. Compile all java files using the following:
```
javac *.java
```
7. Test the environment
```
java TestEnv
```
8. Run program and capture its output as follows:
```
java Program testcases/<file name here>.minc
```

## Related Repos:
[Lexical-Tokenizer](https://github.com/khalkmq/Lexical-Tokenizer)

[Lexical-Analyzer](https://github.com/khalkmq/Lexical-Analyzer)

[Syntax Checker](https://github.com/khalkmq/Syntax-Checker)
