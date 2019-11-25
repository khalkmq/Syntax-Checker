/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2000 Gerwin Klein <lsf@jflex.de>                          *
 * All rights reserved.                                                    *
 *                                                                         *
 * Thanks to Larry Bell and Bob Jamison for suggestions and comments.      *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

%%

%class Lexer
%byaccj

%{

  public Parser   yyparser;
  public int      lineno;

  public Lexer(java.io.Reader r, Parser yyparser) {
    this(r);
    this.yyparser = yyparser;
    this.lineno   = 1;
  }

%}

int        = [0-9]+
float      = [0-9]+"."[0-9]+(E[+-]?[0-9]+)?
identifier = [a-zA-Z_][a-zA-Z0-9_]*
newline    = \n
whitespace = [ \t\r]+
comment    = "//".*

%%

"print"                             { return Parser.PRINT    ; }
"bool"                              { return Parser.BOOL     ; }
"int"                               { return Parser.INT      ; }
"float"                             { return Parser.FLOAT    ; }
"record"                            { return Parser.RECORD   ; }
"size"                              { return Parser.SIZE     ; }
"new"                               { return Parser.NEW      ; }
"while"                             { return Parser.WHILE    ; }
"if"                                { return Parser.IF       ; }
"else"                              { return Parser.ELSE     ; }
"return"                            { return Parser.RETURN   ; }
"break"                             { return Parser.BREAK    ; }
"continue"                          { return Parser.CONTINUE ; }
"and"                               { return Parser.AND      ; }
"or"                                { return Parser.OR       ; }
"not"                               { return Parser.NOT      ; }
"&"                                 { return Parser.ADDR     ; }
"{"                                 { return Parser.LBRACE   ; }
"}"                                 { return Parser.RBRACE   ; }
"("                                 { return Parser.LPAREN   ; }
")"                                 { return Parser.RPAREN   ; }
"["                                 { return Parser.LBRACKET ; }
"]"                                 { return Parser.RBRACKET ; }
"="                                 { return Parser.ASSIGN   ; }
"<"                                 { return Parser.LT       ; }
">"                                 { return Parser.GT       ; }
"+"                                 { return Parser.PLUS     ; }
"-"                                 { return Parser.MINUS    ; }
"*"                                 { return Parser.MUL      ; }
"/"                                 { return Parser.DIV      ; }
"%"                                 { return Parser.MOD      ; }
";"                                 { return Parser.SEMI     ; }
","                                 { return Parser.COMMA    ; }
"."                                 { return Parser.DOT      ; }
"=="                                { return Parser.EQ       ; }
"!="                                { return Parser.NE       ; }
"<="                                { return Parser.LE       ; }
">="                                { return Parser.GE       ; }
"true"|"false"                      { yyparser.yylval.obj = yytext(); return Parser.BOOL_LIT ; }
{int}                               { yyparser.yylval.obj = yytext(); return Parser.INT_LIT  ; }
{float}                             { yyparser.yylval.obj = yytext(); return Parser.FLOAT_LIT; }
{identifier}                        { yyparser.yylval.obj = yytext(); return Parser.IDENT    ; }
{comment}                           { /* skip */ }
{newline}                           { lineno++;  }
{whitespace}                        { /* skip */ }


\b     { System.err.println("Sorry, backspace doesn't work"); }

/* error fallback */
[^]    { System.err.println("Error: unexpected character '"+yytext()+"'"); return -1; }
