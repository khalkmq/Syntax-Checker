//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 15 "Parser.y"
import java.io.*;
//#line 19 "Parser.java"




public class Parser
             extends ParserImpl
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ASSIGN=257;
public final static short OR=258;
public final static short AND=259;
public final static short EQ=260;
public final static short NE=261;
public final static short LE=262;
public final static short LT=263;
public final static short GE=264;
public final static short GT=265;
public final static short NOT=266;
public final static short PLUS=267;
public final static short MINUS=268;
public final static short MUL=269;
public final static short DIV=270;
public final static short MOD=271;
public final static short INT_LIT=272;
public final static short FLOAT_LIT=273;
public final static short BOOL_LIT=274;
public final static short IDENT=275;
public final static short INT=276;
public final static short FLOAT=277;
public final static short BOOL=278;
public final static short IF=279;
public final static short ELSE=280;
public final static short NEW=281;
public final static short PRINT=282;
public final static short WHILE=283;
public final static short RETURN=284;
public final static short CONTINUE=285;
public final static short BREAK=286;
public final static short LPAREN=287;
public final static short RPAREN=288;
public final static short LBRACE=289;
public final static short RBRACE=290;
public final static short LBRACKET=291;
public final static short RBRACKET=292;
public final static short RECORD=293;
public final static short SIZE=294;
public final static short SEMI=295;
public final static short COMMA=296;
public final static short DOT=297;
public final static short ADDR=298;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,    2,    5,    7,    7,    7,   24,
    7,    7,   25,   26,   27,    6,    8,    8,   10,   10,
   11,   13,   13,   14,   14,   14,   14,   14,   14,   14,
   14,   14,   16,   16,   16,   16,   17,   28,   18,    3,
    3,    4,   29,   19,   15,   20,   21,   22,   12,   12,
    9,    9,   23,   23,   23,   23,   23,   23,   23,   23,
   23,   23,   23,   23,   23,   23,   23,   23,   23,   23,
   23,   23,   23,   23,   23,
};
final static short yylen[] = {                            2,
    1,    2,    0,    1,    1,    3,    1,    1,    1,    0,
    5,    3,    0,    0,    0,   12,    1,    0,    3,    1,
    2,    2,    0,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    3,    2,    6,    5,    5,    0,    5,    2,
    0,    3,    0,    8,    3,    2,    2,    3,    3,    1,
    1,    0,    3,    3,    2,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    1,    1,    1,    1,
    4,    4,    3,    5,    3,
};
final static short yydefred[] = {                         3,
    0,    0,    8,    9,    7,    0,    2,    4,    5,    0,
   10,    0,    0,   41,    6,    0,   12,    0,    0,   11,
   40,    0,    0,    0,    0,   20,    0,   21,   14,    0,
   42,    0,   19,   15,   41,    0,    0,    0,   69,   70,
   68,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   38,   16,   32,   22,   28,   24,   27,   25,   26,   29,
   30,   31,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   47,   46,    0,   41,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   34,    0,    0,   33,    0,    0,    0,    0,    0,   73,
    0,    0,   48,    0,   45,   66,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   64,   65,    0,
   75,   71,    0,    0,    0,    0,    0,    0,    0,   72,
    0,    0,   36,   43,   74,   37,   39,   35,    0,    0,
   44,
};
final static short yydgoto[] = {                          1,
    2,    7,   18,   21,    8,    9,   22,   24,   95,   25,
   26,   96,   37,   54,   55,   56,   57,   58,   59,   60,
   61,   62,   63,   14,   16,   32,   35,   78,  139,
};
final static short yysindex[] = {                         0,
    0, -259,    0,    0,    0, -263,    0,    0,    0, -271,
    0, -289, -256,    0,    0, -254,    0, -179, -259,    0,
    0, -235, -174, -249, -239,    0, -234,    0,    0, -259,
    0, -224,    0,    0,    0, -259,  197, -250,    0,    0,
    0, -229, -193, -259, -250, -184, -250, -190, -183, -250,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   28, -228,  171,  300, -250, -250, -273, -250,
 -178,   41, -250,   57,    0,    0,  154,    0, -250, -250,
 -250, -250, -250, -250, -250, -250, -250, -250, -250, -250,
    0, -250, -267,    0, -173, -152,  331,  115, -139,    0,
  167,  296,    0,  189,    0,    0, -259,  343,  354,    1,
    1,  171,  171,  171,  171, -255, -255,    0,    0,  128,
    0,    0, -250, -135,  300,  272,  141,  272,  222,    0,
  331,  300,    0,    0,    0,    0,    0,    0, -127,  272,
    0,
};
final static short yyrindex[] = {                         0,
    0,  156,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -130,    0,    0,    0,    0,    0,    0, -134,    0,
    0,    0,    0,    0, -128,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  247,    0,    0,    0,    0,
    0,   70,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -186, -122,    0, -124,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -123, -285,    0,   86,    0,
    0,    0,    0,    0,    0,    0,  247,  -35, -188,  -16,
  -11, -113,  -74,  -64,  -55, -172, -133,    0,    0,    0,
    0,    0,    0,   99,    0,    0,    0,    0,    0,    0,
 -283,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
    0,    0,  -25,    0,    0,    0,   36,    0,    0,    0,
  131,    0,   51,   -7,    0,  -65,    0,    0,    0,    0,
    0,    0,  -38,    0,    0,    0,    0,    0,    0,
};
final static int YYTABLESIZE=624;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         65,
   94,   99,   50,   12,   49,   15,   72,  121,   74,   36,
   50,   77,   49,   89,   90,   38,    3,    4,    5,   13,
  100,   39,   40,   41,   64,   11,  100,   66,   97,   98,
   44,  101,   19,    6,  104,   17,   50,   10,   29,   27,
  108,  109,  110,  111,  112,  113,  114,  115,  116,  117,
  118,  119,  107,  120,   23,   13,   30,   67,   67,  133,
   31,   68,   92,  127,   34,   23,  138,   69,   93,   54,
   54,   67,   67,   67,   67,   67,   67,   67,   67,   71,
   67,   67,   67,   67,  131,   62,   62,   62,   62,   62,
   62,   62,   62,   70,   62,   62,    3,    4,    5,   54,
   28,   67,   73,   54,   75,   67,   54,   54,   67,   67,
   20,   76,  102,    6,  122,   62,   13,  125,  134,   62,
  136,  132,   62,   62,   63,   63,   63,   63,   63,   63,
   63,   63,  141,   63,   63,   55,   55,   55,   55,   55,
   55,   55,   55,  123,   58,   58,   58,   58,   58,   58,
   58,   58,  140,   18,   63,    1,   13,  129,   63,   17,
   33,   63,   63,   52,   51,   55,    0,    0,    0,   55,
    0,    0,   55,   55,   58,    0,    0,    0,   58,    0,
    0,   58,   58,   59,   59,   59,   59,   59,   59,   59,
   59,    0,    0,   60,   60,   60,   60,   60,   60,   60,
   60,    0,   61,   61,   61,   61,   61,   61,   61,   61,
    0,    0,    0,   59,    0,    0,    0,   59,    0,    0,
   59,   59,   53,   60,    0,    0,    0,   60,    0,    0,
   60,   60,   61,    0,    0,    0,   61,    0,    0,   61,
   61,   56,   56,   56,   56,    0,   57,   57,   57,   57,
    0,    0,   53,    0,    0,    0,   53,    0,    0,   53,
   53,    0,   83,   84,   85,   86,    0,   87,   88,   89,
   90,   56,    0,    0,    0,   56,   57,    0,   56,   56,
   57,    0,    0,   57,   57,   79,   80,   81,   82,   83,
   84,   85,   86,    0,   87,   88,   89,   90,   79,   80,
   81,   82,   83,   84,   85,   86,    0,   87,   88,   89,
   90,    0,    0,    0,   79,   80,   81,   82,   83,   84,
   85,   86,   91,   87,   88,   89,   90,   67,   67,   67,
   67,   67,   67,   67,   67,  103,   67,   67,   67,   67,
    0,    0,    0,   75,   75,   75,   75,   75,   75,   75,
   75,  105,   75,   75,   75,   75,   72,   72,   72,   72,
   72,   72,   72,   72,   67,   72,   72,   72,   72,    0,
    0,    0,   79,   80,   81,   82,   83,   84,   85,   86,
   75,   87,   88,   89,   90,   79,   80,   81,   82,   83,
   84,   85,   86,   72,   87,   88,   89,   90,   79,   80,
   81,   82,   83,   84,   85,   86,  124,   87,   88,   89,
   90,   79,   80,   81,   82,   83,   84,   85,   86,  130,
   87,   88,   89,   90,   79,   80,   81,   82,   83,   84,
   85,   86,  135,   87,   88,   89,   90,   87,   88,   89,
   90,  106,    0,    0,    0,    0,   79,   80,   81,   82,
   83,   84,   85,   86,  126,   87,   88,   89,   90,    0,
    0,    0,   38,    0,    0,    0,    0,    0,   39,   40,
   41,   42,    0,    0,    0,   43,  128,   44,   45,   46,
   47,   48,   49,   50,    0,   51,   52,   38,    0,    0,
    0,   53,    0,   39,   40,   41,   42,    0,    0,    0,
   43,    0,   44,   45,   46,   47,   48,   49,   50,    0,
   51,  137,   23,    0,    0,    0,   53,    0,   23,   23,
   23,   23,    0,    0,    0,   23,    0,   23,   23,   23,
   23,   23,   23,   23,    0,   23,   23,   38,    0,    0,
    0,   23,    0,   39,   40,   41,   42,    0,    0,    0,
   43,    0,   44,   45,   46,   47,   48,   49,   50,    0,
   51,   38,    0,    0,    0,   38,   53,   39,   40,   41,
   64,   39,   40,   41,   42,    0,   44,    0,    0,    0,
   44,    0,   50,    0,    0,    0,   50,   17,   79,   80,
   81,   82,   83,   84,   85,   86,    0,   87,   88,   89,
   90,   80,   81,   82,   83,   84,   85,   86,    0,   87,
   88,   89,   90,   81,   82,   83,   84,   85,   86,    0,
   87,   88,   89,   90,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         38,
   66,  275,  288,  275,  288,  295,   45,  275,   47,   35,
  296,   50,  296,  269,  270,  266,  276,  277,  278,  291,
  294,  272,  273,  274,  275,  289,  294,  257,   67,   68,
  281,   70,  287,  293,   73,  292,  287,    2,  288,  275,
   79,   80,   81,   82,   83,   84,   85,   86,   87,   88,
   89,   90,   78,   92,   19,  291,  296,  287,  287,  125,
  295,  291,  291,  102,  289,   30,  132,  297,  297,  258,
  259,  258,  259,  260,  261,  262,  263,  264,  265,   44,
  267,  268,  269,  270,  123,  258,  259,  260,  261,  262,
  263,  264,  265,  287,  267,  268,  276,  277,  278,  288,
  275,  288,  287,  292,  295,  292,  295,  296,  295,  296,
  290,  295,  291,  293,  288,  288,  291,  257,  126,  292,
  128,  257,  295,  296,  258,  259,  260,  261,  262,  263,
  264,  265,  140,  267,  268,  258,  259,  260,  261,  262,
  263,  264,  265,  296,  258,  259,  260,  261,  262,  263,
  264,  265,  280,  288,  288,    0,  287,  107,  292,  288,
   30,  295,  296,  288,  288,  288,   -1,   -1,   -1,  292,
   -1,   -1,  295,  296,  288,   -1,   -1,   -1,  292,   -1,
   -1,  295,  296,  258,  259,  260,  261,  262,  263,  264,
  265,   -1,   -1,  258,  259,  260,  261,  262,  263,  264,
  265,   -1,  258,  259,  260,  261,  262,  263,  264,  265,
   -1,   -1,   -1,  288,   -1,   -1,   -1,  292,   -1,   -1,
  295,  296,  258,  288,   -1,   -1,   -1,  292,   -1,   -1,
  295,  296,  288,   -1,   -1,   -1,  292,   -1,   -1,  295,
  296,  258,  259,  260,  261,   -1,  258,  259,  260,  261,
   -1,   -1,  288,   -1,   -1,   -1,  292,   -1,   -1,  295,
  296,   -1,  262,  263,  264,  265,   -1,  267,  268,  269,
  270,  288,   -1,   -1,   -1,  292,  288,   -1,  295,  296,
  292,   -1,   -1,  295,  296,  258,  259,  260,  261,  262,
  263,  264,  265,   -1,  267,  268,  269,  270,  258,  259,
  260,  261,  262,  263,  264,  265,   -1,  267,  268,  269,
  270,   -1,   -1,   -1,  258,  259,  260,  261,  262,  263,
  264,  265,  295,  267,  268,  269,  270,  258,  259,  260,
  261,  262,  263,  264,  265,  295,  267,  268,  269,  270,
   -1,   -1,   -1,  258,  259,  260,  261,  262,  263,  264,
  265,  295,  267,  268,  269,  270,  258,  259,  260,  261,
  262,  263,  264,  265,  295,  267,  268,  269,  270,   -1,
   -1,   -1,  258,  259,  260,  261,  262,  263,  264,  265,
  295,  267,  268,  269,  270,  258,  259,  260,  261,  262,
  263,  264,  265,  295,  267,  268,  269,  270,  258,  259,
  260,  261,  262,  263,  264,  265,  292,  267,  268,  269,
  270,  258,  259,  260,  261,  262,  263,  264,  265,  292,
  267,  268,  269,  270,  258,  259,  260,  261,  262,  263,
  264,  265,  292,  267,  268,  269,  270,  267,  268,  269,
  270,  288,   -1,   -1,   -1,   -1,  258,  259,  260,  261,
  262,  263,  264,  265,  288,  267,  268,  269,  270,   -1,
   -1,   -1,  266,   -1,   -1,   -1,   -1,   -1,  272,  273,
  274,  275,   -1,   -1,   -1,  279,  288,  281,  282,  283,
  284,  285,  286,  287,   -1,  289,  290,  266,   -1,   -1,
   -1,  295,   -1,  272,  273,  274,  275,   -1,   -1,   -1,
  279,   -1,  281,  282,  283,  284,  285,  286,  287,   -1,
  289,  290,  266,   -1,   -1,   -1,  295,   -1,  272,  273,
  274,  275,   -1,   -1,   -1,  279,   -1,  281,  282,  283,
  284,  285,  286,  287,   -1,  289,  290,  266,   -1,   -1,
   -1,  295,   -1,  272,  273,  274,  275,   -1,   -1,   -1,
  279,   -1,  281,  282,  283,  284,  285,  286,  287,   -1,
  289,  266,   -1,   -1,   -1,  266,  295,  272,  273,  274,
  275,  272,  273,  274,  275,   -1,  281,   -1,   -1,   -1,
  281,   -1,  287,   -1,   -1,   -1,  287,  292,  258,  259,
  260,  261,  262,  263,  264,  265,   -1,  267,  268,  269,
  270,  259,  260,  261,  262,  263,  264,  265,   -1,  267,
  268,  269,  270,  260,  261,  262,  263,  264,  265,   -1,
  267,  268,  269,  270,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=298;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"ASSIGN","OR","AND","EQ","NE","LE","LT","GE","GT","NOT","PLUS",
"MINUS","MUL","DIV","MOD","INT_LIT","FLOAT_LIT","BOOL_LIT","IDENT","INT",
"FLOAT","BOOL","IF","ELSE","NEW","PRINT","WHILE","RETURN","CONTINUE","BREAK",
"LPAREN","RPAREN","LBRACE","RBRACE","LBRACKET","RBRACKET","RECORD","SIZE",
"SEMI","COMMA","DOT","ADDR",
};
final static String yyrule[] = {
"$accept : program",
"program : decl_list",
"decl_list : decl_list decl",
"decl_list :",
"decl : var_decl",
"decl : fun_decl",
"var_decl : type_spec IDENT SEMI",
"type_spec : BOOL",
"type_spec : INT",
"type_spec : FLOAT",
"$$1 :",
"type_spec : RECORD LBRACE $$1 local_decls RBRACE",
"type_spec : type_spec LBRACKET RBRACKET",
"$$2 :",
"$$3 :",
"$$4 :",
"fun_decl : type_spec IDENT $$2 LPAREN params RPAREN $$3 LBRACE $$4 local_decls stmt_list RBRACE",
"params : param_list",
"params :",
"param_list : param_list COMMA param",
"param_list : param",
"param : type_spec IDENT",
"stmt_list : stmt_list stmt",
"stmt_list :",
"stmt : expr_stmt",
"stmt : compound_stmt",
"stmt : if_stmt",
"stmt : while_stmt",
"stmt : return_stmt",
"stmt : break_stmt",
"stmt : continue_stmt",
"stmt : print_stmt",
"stmt : SEMI",
"expr_stmt : IDENT ASSIGN expr_stmt",
"expr_stmt : expr SEMI",
"expr_stmt : IDENT LBRACKET expr RBRACKET ASSIGN expr_stmt",
"expr_stmt : IDENT DOT IDENT ASSIGN expr_stmt",
"while_stmt : WHILE LPAREN expr RPAREN stmt",
"$$5 :",
"compound_stmt : LBRACE $$5 local_decls stmt_list RBRACE",
"local_decls : local_decls local_decl",
"local_decls :",
"local_decl : type_spec IDENT SEMI",
"$$6 :",
"if_stmt : IF LPAREN expr RPAREN stmt $$6 ELSE stmt",
"return_stmt : RETURN expr SEMI",
"break_stmt : BREAK SEMI",
"continue_stmt : CONTINUE SEMI",
"print_stmt : PRINT expr SEMI",
"arg_list : arg_list COMMA expr",
"arg_list : expr",
"args : arg_list",
"args :",
"expr : expr OR expr",
"expr : expr AND expr",
"expr : NOT expr",
"expr : expr EQ expr",
"expr : expr NE expr",
"expr : expr LE expr",
"expr : expr LT expr",
"expr : expr GE expr",
"expr : expr GT expr",
"expr : expr PLUS expr",
"expr : expr MINUS expr",
"expr : expr MUL expr",
"expr : expr DIV expr",
"expr : LPAREN expr RPAREN",
"expr : IDENT",
"expr : BOOL_LIT",
"expr : INT_LIT",
"expr : FLOAT_LIT",
"expr : IDENT LPAREN args RPAREN",
"expr : IDENT LBRACKET expr RBRACKET",
"expr : IDENT DOT SIZE",
"expr : NEW type_spec LBRACKET expr RBRACKET",
"expr : IDENT DOT IDENT",
};

//#line 166 "Parser.y"
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
//#line 478 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
throws Exception
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 42 "Parser.y"
{ Debug("program  ->  decl_list"                   ); yyval.obj = program____decllist                         (); }
break;
case 2:
//#line 45 "Parser.y"
{ Debug("decl_list  ->  decl_list  decl"           ); yyval.obj = decllist_decllist_decl                      (); }
break;
case 3:
//#line 46 "Parser.y"
{ Debug("decl_list  ->  eps"                       ); yyval.obj = decllist_eps                                (); }
break;
case 4:
//#line 49 "Parser.y"
{ Debug("decl  ->  var_decl"                       ); yyval.obj = decl____vardecl                             (); }
break;
case 5:
//#line 50 "Parser.y"
{ Debug("decl  ->  fun_decl"                       ); yyval.obj = decl____fundecl                             (); }
break;
case 6:
//#line 53 "Parser.y"
{ Debug("var_decl  ->  type_spec IDENT ;"          ); yyval.obj = vardecl____typespec_IDENT_SEMI              (val_peek(2).obj, val_peek(1).obj); }
break;
case 7:
//#line 56 "Parser.y"
{ Debug("type_spec  ->  BOOL"                      ); yyval.obj = typespec____BOOL                            (); }
break;
case 8:
//#line 57 "Parser.y"
{ Debug("type_spec  ->  INT"                       ); yyval.obj = typespec____INT                             (); }
break;
case 9:
//#line 58 "Parser.y"
{ Debug("type_spec  ->  FLOAT"                     ); yyval.obj = typespec____FLOAT                           (); }
break;
case 10:
//#line 59 "Parser.y"
{ Debug("type_spec  ->  RECORD { "                 ); yyval.obj = typespec____RECORD_LBRACE                   (); }
break;
case 11:
//#line 60 "Parser.y"
{ Debug("type_spec  ->  local_decls }"             ); yyval.obj = typespec____localdecls_RBRACE               (); }
break;
case 12:
//#line 61 "Parser.y"
{ Debug("type_spec  ->  type_spec [ ]"             ); yyval.obj = typespec____typespec_LBRACKET_RBRACKET      (val_peek(2).obj); }
break;
case 13:
//#line 64 "Parser.y"
{ Debug("fun_decl  ->  IDENT"                      ); yyval.obj = fundecl____typespec_IDENT              (val_peek(1).obj, val_peek(0).obj); }
break;
case 14:
//#line 65 "Parser.y"
{ Debug("fun_decl  ->  ( params )"                 ); yyval.obj = fundecl____LPAREN_params_RPAREN        (val_peek(4).obj); }
break;
case 15:
//#line 66 "Parser.y"
{ Debug("fun_decl  ->  {"                          ); yyval.obj = fundecl____LBRACE                      (); }
break;
case 16:
//#line 67 "Parser.y"
{ Debug("fun_decl  ->  local_decls stmt_list }"    ); yyval.obj = fundecl____localdecls_stmtlist_RBRACE  (); }
break;
case 17:
//#line 70 "Parser.y"
{ Debug("params  ->  param_list"                   ); yyval.obj = params____paramlist                (); }
break;
case 18:
//#line 71 "Parser.y"
{ Debug("params  ->  eps"                          ); yyval.obj = params____eps                      (); }
break;
case 19:
//#line 74 "Parser.y"
{ Debug("param_list  ->  param_list , param"       ); yyval.obj = paramlist____paramlist_COMMA_param (); }
break;
case 20:
//#line 75 "Parser.y"
{ Debug("param_list  ->  param"                    ); yyval.obj = paramlist____param                 (); }
break;
case 21:
//#line 78 "Parser.y"
{ Debug("param  ->  type_spec IDENT"               ); yyval.obj = param____typespec_IDENT            (val_peek(1).obj, val_peek(0).obj); }
break;
case 22:
//#line 81 "Parser.y"
{ Debug("stmt_list  ->  stmt_list  stmt"           ); yyval.obj = stmtlist____stmtlist_stmt          (); }
break;
case 23:
//#line 82 "Parser.y"
{ Debug("stmt_list  ->  eps"                       ); yyval.obj = stmtlist____eps                    (); }
break;
case 24:
//#line 85 "Parser.y"
{ Debug("stmt  ->  expr_stmt"                      ); yyval.obj = stmt____exprstmt                   (); }
break;
case 25:
//#line 86 "Parser.y"
{ Debug("stmt  ->  compound_stmt"                  ); yyval.obj = stmt____compoundstmt               (); }
break;
case 26:
//#line 87 "Parser.y"
{ Debug("stmt  ->  if_stmt"                        ); yyval.obj = stmt____ifstmt                     (); }
break;
case 27:
//#line 88 "Parser.y"
{ Debug("stmt  ->  while_stmt"                     ); yyval.obj = stmt____whilestmt                  (val_peek(0).obj); }
break;
case 28:
//#line 89 "Parser.y"
{ Debug("stmt  ->  return_stmt"                    ); yyval.obj = stmt____returnstmt                 (val_peek(0).obj); }
break;
case 29:
//#line 90 "Parser.y"
{ Debug("stmt  ->  break_stmt"                     ); yyval.obj = stmt____breakstmt                  (); }
break;
case 30:
//#line 91 "Parser.y"
{ Debug("stmt  ->  continue_stmt"                  ); yyval.obj = stmt____continuestmt               (); }
break;
case 31:
//#line 92 "Parser.y"
{ Debug("stmt  ->  print_stmt"                     ); yyval.obj = stmt____printstmt                  (); }
break;
case 32:
//#line 93 "Parser.y"
{ Debug("stmt  -> ;"                               ); yyval.obj = stmt____SEMI                       (); }
break;
case 33:
//#line 96 "Parser.y"
{ Debug("expr_stmt -> IDENT  =  expr_stmt"         ); yyval.obj = exprstmt____IDENT_ASSIGN_exprstmt                        (val_peek(2).obj, val_peek(0).obj); }
break;
case 34:
//#line 97 "Parser.y"
{ Debug("expr_stmt -> expr ;"                      ); yyval.obj = exprstmt____expr_SEMI                                    (val_peek(1).obj); }
break;
case 35:
//#line 98 "Parser.y"
{ Debug("expr_stmt -> IDENT [ expr ] = expr_stmt"  ); yyval.obj = exprstmt____IDENT_LBRACKET_expr_RBRACKET_ASSIGN_exprstmt (val_peek(5).obj, val_peek(3).obj, val_peek(0).obj); }
break;
case 36:
//#line 99 "Parser.y"
{ Debug("expr_stmt -> IDENT DOT IDENT = expr_stmt" ); yyval.obj = exprstmt____IDENT_DOT_IDENT_ASSIGN_exprstmt              (val_peek(4).obj, val_peek(2).obj, val_peek(0).obj); }
break;
case 37:
//#line 102 "Parser.y"
{ Debug("while_stmt  ->  WHILE ( expr ) stmt"      ); yyval.obj = whilestmt____WHILE_LPAREN_expr_RPAREN_stmt        (val_peek(2).obj); }
break;
case 38:
//#line 105 "Parser.y"
{ Debug("compound_stmt  ->  { "    ); yyval.obj = compoundstmt____LBRACE(); }
break;
case 39:
//#line 106 "Parser.y"
{ Debug("compound_stmt  ->  local_decls stmt_list }"    ); yyval.obj = compoundstmt____localdecls_stmtlist_RBRACE (); }
break;
case 40:
//#line 109 "Parser.y"
{ Debug("local_decls  ->  local_decls local_decl"  ); yyval.obj = localdecls____localdecls_localdecl        (); }
break;
case 41:
//#line 110 "Parser.y"
{ Debug("local_decls  ->  eps"                     ); yyval.obj = localdecls____eps                         (); }
break;
case 42:
//#line 113 "Parser.y"
{ Debug("local_decl  ->  type_spec IDENT ;"        ); yyval.obj = localdecl____typespec_IDENT_SEMI          (val_peek(2).obj, val_peek(1).obj); }
break;
case 43:
//#line 116 "Parser.y"
{ Debug("if_stmt  ->  IF ( expr ) stmt"   );  yyval.obj = ifstmt____IF_LPAREN_expr_RPAREN_stmt (val_peek(2).obj); }
break;
case 44:
//#line 117 "Parser.y"
{ Debug("if_stmt  ->  ELSE stmt"          );  yyval.obj = ifstmt____ELSE_stmt (); }
break;
case 45:
//#line 120 "Parser.y"
{ Debug("return_stmt  ->  RETURN  expr ;" ); yyval.obj = returnstmt____RETURN_expr_SEMI               (val_peek(1).obj); }
break;
case 46:
//#line 123 "Parser.y"
{ Debug("break_stmt  ->  BREAK ;"         ); yyval.obj = breakstmt____BREAK_SEMI                      (); }
break;
case 47:
//#line 126 "Parser.y"
{ Debug("continue_stmt  ->  CONTINUE ;"   ); yyval.obj = continuestmt____CONTINUE_SEMI                (); }
break;
case 48:
//#line 129 "Parser.y"
{ Debug("print_stmt  ->  PRINT expr ;"    ); yyval.obj = printstmt____PRINT_expr_SEMI                 (); }
break;
case 49:
//#line 132 "Parser.y"
{ Debug("arg_list  ->  arg_list , expr"   ); yyval.obj = arglist____arglist_COMMA_expr                (val_peek(2).obj, val_peek(0).obj); }
break;
case 50:
//#line 133 "Parser.y"
{ Debug("arg_list  ->  expr"              ); yyval.obj = arglist____expr                              (val_peek(0).obj); }
break;
case 51:
//#line 136 "Parser.y"
{ Debug("args  ->  arg_list"              ); yyval.obj = args____arglist                              (val_peek(0).obj); }
break;
case 52:
//#line 137 "Parser.y"
{ Debug("args  ->  eps"                   ); yyval.obj = args____eps                                  (); }
break;
case 53:
//#line 140 "Parser.y"
{ Debug("expr  ->  expr || expr"           ); yyval.obj = expr____expr_OR_expr                        (val_peek(2).obj, val_peek(0).obj); }
break;
case 54:
//#line 141 "Parser.y"
{ Debug("expr  ->  expr && expr"           ); yyval.obj = expr____expr_AND_expr                       (val_peek(2).obj, val_peek(0).obj); }
break;
case 55:
//#line 142 "Parser.y"
{ Debug("expr  ->  ! expr"                 ); yyval.obj = expr____NOT_expr                            (val_peek(0).obj); }
break;
case 56:
//#line 143 "Parser.y"
{ Debug("expr  ->  expr == expr"           ); yyval.obj = expr____expr_EQ_expr                        (val_peek(2).obj, val_peek(0).obj); }
break;
case 57:
//#line 144 "Parser.y"
{ Debug("expr  ->  expr != expr"           ); yyval.obj = expr____expr_NE_expr                        (val_peek(2).obj, val_peek(0).obj); }
break;
case 58:
//#line 145 "Parser.y"
{ Debug("expr  ->  expr <= expr"           ); yyval.obj = expr____expr_LE_expr                        (val_peek(2).obj, val_peek(0).obj); }
break;
case 59:
//#line 146 "Parser.y"
{ Debug("expr  ->  expr < expr"            ); yyval.obj = expr____expr_LT_expr                        (val_peek(2).obj, val_peek(0).obj); }
break;
case 60:
//#line 147 "Parser.y"
{ Debug("expr  ->  expr >= expr"           ); yyval.obj = expr____expr_GE_expr                        (val_peek(2).obj, val_peek(0).obj); }
break;
case 61:
//#line 148 "Parser.y"
{ Debug("expr  ->  expr > expr"            ); yyval.obj = expr____expr_GT_expr                        (val_peek(2).obj, val_peek(0).obj); }
break;
case 62:
//#line 149 "Parser.y"
{ Debug("expr  ->  expr + expr"            ); yyval.obj = expr____expr_PLUS_expr                      (val_peek(2).obj, val_peek(0).obj); }
break;
case 63:
//#line 150 "Parser.y"
{ Debug("expr  ->  expr - expr"            ); yyval.obj = expr____expr_MINUS_expr                     (val_peek(2).obj, val_peek(0).obj); }
break;
case 64:
//#line 151 "Parser.y"
{ Debug("expr  ->  expr * expr"            ); yyval.obj = expr____expr_MUL_expr                       (val_peek(2).obj, val_peek(0).obj); }
break;
case 65:
//#line 152 "Parser.y"
{ Debug("expr  ->  expr / expr"            ); yyval.obj = expr____expr_DIV_expr                       (val_peek(2).obj, val_peek(0).obj); }
break;
case 66:
//#line 153 "Parser.y"
{ Debug("expr  ->  ( expr )"               ); yyval.obj = expr____LPAREN_expr_RPAREN                  (val_peek(1).obj); }
break;
case 67:
//#line 154 "Parser.y"
{ Debug("expr  ->  IDENT"                  ); yyval.obj = expr____IDENT                               (val_peek(0).obj); }
break;
case 68:
//#line 155 "Parser.y"
{ Debug("expr  ->  BOOL_LIT"               ); yyval.obj = expr____BOOLLIT                             (val_peek(0).obj); }
break;
case 69:
//#line 156 "Parser.y"
{ Debug("expr  ->  INT_LIT"                ); yyval.obj = expr____INTLIT                              (val_peek(0).obj); }
break;
case 70:
//#line 157 "Parser.y"
{ Debug("expr  ->  FLOAT_LIT"              ); yyval.obj = expr____FLOATLIT                            (val_peek(0).obj); }
break;
case 71:
//#line 158 "Parser.y"
{ Debug("expr  ->  IDENT ( args )"         ); yyval.obj = expr____IDENT_LPAREN_args_RPAREN            (val_peek(3).obj, val_peek(1).obj); }
break;
case 72:
//#line 159 "Parser.y"
{ Debug("expr  ->  IDENT [ expr ]"         ); yyval.obj = expr____IDENT_LBRACKET_expr_RBRACKET        (val_peek(3).obj, val_peek(1).obj); }
break;
case 73:
//#line 160 "Parser.y"
{ Debug("expr  ->  IDENT . SIZE"           ); yyval.obj = expr____IDENT_DOT_SIZE                      (val_peek(2).obj); }
break;
case 74:
//#line 161 "Parser.y"
{ Debug("expr  ->  NEW type_spec [ expr ]" ); yyval.obj = expr____NEW_typespec_LBRACKET_expr_RBRACKET (val_peek(3).obj, val_peek(1).obj); }
break;
case 75:
//#line 162 "Parser.y"
{ Debug("expr  ->  IDENT . IDENT"          ); yyval.obj = expr____IDENT_DOT_IDENT                     (val_peek(2).obj, val_peek(0).obj); }
break;
//#line 927 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
//## The -Jnorun option was used ##
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
