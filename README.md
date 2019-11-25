# Syntax-Checker


Notes:

    All test cases including all but last extra-credit case should be fully functional.

How to run:
    - place yacc.linux file in same directory
    - place jflex-1.6.1.jar in same directory
    - place all zip file contents in same directory
    - place folder in directory with path ~/sample/minc/ <PLACE MINC FILES HERE>
    
    - On terminal, execute:
    
        java -jar jflex-1.6.1.jar Lexer.flex
    
        ./yacc.linux -Jthrows="Exception" -Jextends=ParserImpl -Jnorun -J ./Parser.y
        
        javac *.java
        
        java TestEnv        #to test the environment
        
        java Program sample/minc/test_01_main_succ.minc

