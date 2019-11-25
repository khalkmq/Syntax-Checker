/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2001 Gerwin Klein <lsf@jflex.de>                          *
 * All rights reserved.                                                    *
 *                                                                         *
 * This is a modified version of the example from                          *
 *   http://www.lincom-asg.com/~rjamison/byacc/                            *
 *                                                                         *
 * Thanks to Larry Bell and Bob Jamison for suggestions and comments.      *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

%{
import java.io.*;
%}


%right  ASSIGN
%left   OR 
%left   AND 
%left   EQ      NE  
%left   LE      LT      GE      GT 
%right  NOT
%left   PLUS    MINUS  
%left   MUL     DIV     MOD 


%token <obj> INT_LIT  FLOAT_LIT  BOOL_LIT  IDENT
%token <obj> INT      FLOAT      BOOL   
%token       IF  ELSE  NEW  PRINT  WHILE  RETURN  CONTINUE  BREAK
%token       ASSIGN  LPAREN  RPAREN  LBRACE  RBRACE  LBRACKET  RBRACKET  RECORD  SIZE  SEMI  COMMA  DOT  ADDR

%type <obj> program   decl_list  decl  local_decls  local_decl
%type <obj> var_decl  fun_decl   local_decls   type_spec
%type <obj> params  args   param_list  param  arg_list
%type <obj> stmt_list  stmt  return_stmt  expr_stmt  while_stmt  compound_stmt  if_stmt  break_stmt  continue_stmt  print_stmt
%type <obj> expr 

%%

program       : decl_list                                     { Debug("program  ->  decl_list"                   ); $$ = program____decllist                         (); }
              ;

decl_list     : decl_list decl                                { Debug("decl_list  ->  decl_list  decl"           ); $$ = decllist_decllist_decl                      (); }
              |                                               { Debug("decl_list  ->  eps"                       ); $$ = decllist_eps                                (); }
              ;

decl          : var_decl                                      { Debug("decl  ->  var_decl"                       ); $$ = decl____vardecl                             (); }
              | fun_decl                                      { Debug("decl  ->  fun_decl"                       ); $$ = decl____fundecl                             (); }
              ;

var_decl      : type_spec IDENT SEMI                          { Debug("var_decl  ->  type_spec IDENT ;"          ); $$ = vardecl____typespec_IDENT_SEMI              ($1, $2); }
              ;

type_spec     : BOOL                                          { Debug("type_spec  ->  BOOL"                      ); $$ = typespec____BOOL                            (); }
              | INT                                           { Debug("type_spec  ->  INT"                       ); $$ = typespec____INT                             (); }
              | FLOAT                                         { Debug("type_spec  ->  FLOAT"                     ); $$ = typespec____FLOAT                           (); }
              | RECORD LBRACE                                 { Debug("type_spec  ->  RECORD { "                 ); $$ = typespec____RECORD_LBRACE                   (); }
                local_decls RBRACE                            { Debug("type_spec  ->  local_decls }"             ); $$ = typespec____localdecls_RBRACE               (); }
              | type_spec LBRACKET RBRACKET                   { Debug("type_spec  ->  type_spec [ ]"             ); $$ = typespec____typespec_LBRACKET_RBRACKET      ($1); }
              ;

fun_decl      : type_spec IDENT                               { Debug("fun_decl  ->  IDENT"                      ); $$ = fundecl____typespec_IDENT              ($1, $2); }
                LPAREN params RPAREN                          { Debug("fun_decl  ->  ( params )"                 ); $$ = fundecl____LPAREN_params_RPAREN        ($2); }
                LBRACE                                        { Debug("fun_decl  ->  {"                          ); $$ = fundecl____LBRACE                      (); }
                local_decls stmt_list RBRACE                  { Debug("fun_decl  ->  local_decls stmt_list }"    ); $$ = fundecl____localdecls_stmtlist_RBRACE  (); }
              ;

params        : param_list                                    { Debug("params  ->  param_list"                   ); $$ = params____paramlist                (); }
              |                                               { Debug("params  ->  eps"                          ); $$ = params____eps                      (); }
              ;

param_list    : param_list COMMA param                        { Debug("param_list  ->  param_list , param"       ); $$ = paramlist____paramlist_COMMA_param (); }
              | param                                         { Debug("param_list  ->  param"                    ); $$ = paramlist____param                 (); }
              ;

param         : type_spec IDENT                               { Debug("param  ->  type_spec IDENT"               ); $$ = param____typespec_IDENT            ($1, $2); }
              ;

stmt_list     : stmt_list stmt                                { Debug("stmt_list  ->  stmt_list  stmt"           ); $$ = stmtlist____stmtlist_stmt          (); }
              |                                               { Debug("stmt_list  ->  eps"                       ); $$ = stmtlist____eps                    (); }
              ;

stmt          : expr_stmt                                     { Debug("stmt  ->  expr_stmt"                      ); $$ = stmt____exprstmt                   (); }
              | compound_stmt                                 { Debug("stmt  ->  compound_stmt"                  ); $$ = stmt____compoundstmt               (); }
              | if_stmt                                       { Debug("stmt  ->  if_stmt"                        ); $$ = stmt____ifstmt                     (); }
              | while_stmt                                    { Debug("stmt  ->  while_stmt"                     ); $$ = stmt____whilestmt                  ($1); }
              | return_stmt                                   { Debug("stmt  ->  return_stmt"                    ); $$ = stmt____returnstmt                 ($1); }
              | break_stmt                                    { Debug("stmt  ->  break_stmt"                     ); $$ = stmt____breakstmt                  (); }
              | continue_stmt                                 { Debug("stmt  ->  continue_stmt"                  ); $$ = stmt____continuestmt               (); }
              | print_stmt                                    { Debug("stmt  ->  print_stmt"                     ); $$ = stmt____printstmt                  (); }
              | SEMI                                          { Debug("stmt  -> ;"                               ); $$ = stmt____SEMI                       (); }
              ;

expr_stmt     : IDENT ASSIGN expr_stmt                        { Debug("expr_stmt -> IDENT  =  expr_stmt"         ); $$ = exprstmt____IDENT_ASSIGN_exprstmt                        ($1, $3); }
              | expr SEMI                                     { Debug("expr_stmt -> expr ;"                      ); $$ = exprstmt____expr_SEMI                                    ($1); }
              | IDENT LBRACKET expr RBRACKET ASSIGN expr_stmt { Debug("expr_stmt -> IDENT [ expr ] = expr_stmt"  ); $$ = exprstmt____IDENT_LBRACKET_expr_RBRACKET_ASSIGN_exprstmt ($1, $3, $6); }
              | IDENT DOT IDENT ASSIGN expr_stmt              { Debug("expr_stmt -> IDENT DOT IDENT = expr_stmt" ); $$ = exprstmt____IDENT_DOT_IDENT_ASSIGN_exprstmt              ($1, $3, $5); }
              ;

while_stmt    : WHILE LPAREN expr RPAREN stmt                 { Debug("while_stmt  ->  WHILE ( expr ) stmt"      ); $$ = whilestmt____WHILE_LPAREN_expr_RPAREN_stmt        ($3); }
              ;

compound_stmt : LBRACE                                        { Debug("compound_stmt  ->  { "    ); $$ = compoundstmt____LBRACE(); }
                local_decls stmt_list RBRACE                  { Debug("compound_stmt  ->  local_decls stmt_list }"    ); $$ = compoundstmt____localdecls_stmtlist_RBRACE (); }
              ;

local_decls   : local_decls local_decl                        { Debug("local_decls  ->  local_decls local_decl"  ); $$ = localdecls____localdecls_localdecl        (); }
              |                                               { Debug("local_decls  ->  eps"                     ); $$ = localdecls____eps                         (); }
              ;

local_decl    : type_spec IDENT SEMI                          { Debug("local_decl  ->  type_spec IDENT ;"        ); $$ = localdecl____typespec_IDENT_SEMI          ($1, $2); }
              ;

if_stmt       : IF LPAREN expr RPAREN stmt                    { Debug("if_stmt  ->  IF ( expr ) stmt"   );  $$ = ifstmt____IF_LPAREN_expr_RPAREN_stmt ($3); }
                ELSE stmt                                     { Debug("if_stmt  ->  ELSE stmt"          );  $$ = ifstmt____ELSE_stmt (); }
              ;

return_stmt   : RETURN expr SEMI                              { Debug("return_stmt  ->  RETURN  expr ;" ); $$ = returnstmt____RETURN_expr_SEMI               ($2); }
              ;

break_stmt    : BREAK SEMI                                    { Debug("break_stmt  ->  BREAK ;"         ); $$ = breakstmt____BREAK_SEMI                      (); }
              ;

continue_stmt : CONTINUE SEMI                                 { Debug("continue_stmt  ->  CONTINUE ;"   ); $$ = continuestmt____CONTINUE_SEMI                (); }
              ;

print_stmt    : PRINT expr SEMI                               { Debug("print_stmt  ->  PRINT expr ;"    ); $$ = printstmt____PRINT_expr_SEMI                 (); }
              ;

arg_list      : arg_list COMMA expr                           { Debug("arg_list  ->  arg_list , expr"   ); $$ = arglist____arglist_COMMA_expr                ($1, $3); }
              | expr                                          { Debug("arg_list  ->  expr"              ); $$ = arglist____expr                              ($1); }
              ;

args          : arg_list                                      { Debug("args  ->  arg_list"              ); $$ = args____arglist                              ($1); }
              |                                               { Debug("args  ->  eps"                   ); $$ = args____eps                                  (); }
              ;

expr          : expr OR expr                                  { Debug("expr  ->  expr || expr"           ); $$ = expr____expr_OR_expr                        ($1, $3); }
              | expr AND expr                                 { Debug("expr  ->  expr && expr"           ); $$ = expr____expr_AND_expr                       ($1, $3); }
              | NOT expr                                      { Debug("expr  ->  ! expr"                 ); $$ = expr____NOT_expr                            ($2); }
              | expr EQ expr                                  { Debug("expr  ->  expr == expr"           ); $$ = expr____expr_EQ_expr                        ($1, $3); }
              | expr NE expr                                  { Debug("expr  ->  expr != expr"           ); $$ = expr____expr_NE_expr                        ($1, $3); }
              | expr LE expr                                  { Debug("expr  ->  expr <= expr"           ); $$ = expr____expr_LE_expr                        ($1, $3); }
              | expr LT expr                                  { Debug("expr  ->  expr < expr"            ); $$ = expr____expr_LT_expr                        ($1, $3); }
              | expr GE expr                                  { Debug("expr  ->  expr >= expr"           ); $$ = expr____expr_GE_expr                        ($1, $3); }
              | expr GT expr                                  { Debug("expr  ->  expr > expr"            ); $$ = expr____expr_GT_expr                        ($1, $3); }
              | expr PLUS expr                                { Debug("expr  ->  expr + expr"            ); $$ = expr____expr_PLUS_expr                      ($1, $3); }
              | expr MINUS expr                               { Debug("expr  ->  expr - expr"            ); $$ = expr____expr_MINUS_expr                     ($1, $3); }
              | expr MUL expr                                 { Debug("expr  ->  expr * expr"            ); $$ = expr____expr_MUL_expr                       ($1, $3); }
              | expr DIV expr                                 { Debug("expr  ->  expr / expr"            ); $$ = expr____expr_DIV_expr                       ($1, $3); }
              | LPAREN expr RPAREN                            { Debug("expr  ->  ( expr )"               ); $$ = expr____LPAREN_expr_RPAREN                  ($2); }
              | IDENT                                         { Debug("expr  ->  IDENT"                  ); $$ = expr____IDENT                               ($1); }
              | BOOL_LIT                                      { Debug("expr  ->  BOOL_LIT"               ); $$ = expr____BOOLLIT                             ($1); }
              | INT_LIT                                       { Debug("expr  ->  INT_LIT"                ); $$ = expr____INTLIT                              ($1); }
              | FLOAT_LIT                                     { Debug("expr  ->  FLOAT_LIT"              ); $$ = expr____FLOATLIT                            ($1); }
              | IDENT LPAREN args RPAREN                      { Debug("expr  ->  IDENT ( args )"         ); $$ = expr____IDENT_LPAREN_args_RPAREN            ($1, $3); }
              | IDENT LBRACKET expr RBRACKET                  { Debug("expr  ->  IDENT [ expr ]"         ); $$ = expr____IDENT_LBRACKET_expr_RBRACKET        ($1, $3); }
              | IDENT DOT SIZE                                { Debug("expr  ->  IDENT . SIZE"           ); $$ = expr____IDENT_DOT_SIZE                      ($1); }
              | NEW type_spec LBRACKET expr RBRACKET          { Debug("expr  ->  NEW type_spec [ expr ]" ); $$ = expr____NEW_typespec_LBRACKET_expr_RBRACKET ($2, $4); }
              | IDENT DOT IDENT                               { Debug("expr  ->  IDENT . IDENT"          ); $$ = expr____IDENT_DOT_IDENT                     ($1, $3); }
              ;

%%
    private Lexer lexer;

    private int yylex () {
        int yyl_return = -1;
        try {          
	        setLine(lexer.lineno);
            yylval = new ParserVal(0);
            yyl_return = lexer.yylex();
        }
        catch (IOException e) {
            System.out.println("IO error :"+e);
        }
        return yyl_return;
    }

    public void yyerror (String error) {
        System.out.println ("Error: " + error);
    }

    public Parser(Reader r) {
        lexer = new Lexer(r, this);
    }
