%{
#include <stdio.h>
#include <stdlib.h>

#define YYDEBUG 1
%}

%token ATRIB
%token GREATER
%token LESS
%token EQUAL
%token NOT_EQUAL
%token GREATER_EQUAL
%token LESS_EQUAL
%token STRAIGHT_BRACKET_OPEN
%token STRAIGHT_BRACKET_CLOSE
%token CURLY_BRACKET_OPEN
%token CURLY_BRACKET_CLOSE
%token NORMAL_BRACKET_OPEN
%token NORMAL_BRACKET_CLOSE
%token SEMICOLON
%token CHAR
%token ID
%token CONST
%token FOR
%token ELSE
%token IF 
%token INT
%token BOOL
%token READ
%token WRITE
%token VAR
%token WHILE

%token PLUS
%token MINUS
%token DIV
%token MOD
%token MUL

%start program

%%
program : decllist SEMICOLON stmtlist
decllist : declaration | declaration SEMICOLON decllist
declaration : type ID
type : BOOL | CHAR | INT
cmpdstmt : CURLY_BRACKET_OPEN stmtlist CURLY_BRACKET_CLOSE
stmtlist : stmt | stmt SEMICOLON stmtlist
stmt : simplstmt | structstmt
simplstmt : assignstmt | iostmt
assignstmt : identifier ATRIB expression

expression : expression (PLUS | MINUS) term | term | constant

term : term (MUL | DIV | MOD) factor | factor

constant : noconst

factor : NORMAL_BRACKET_OPEN expression NORMAL_BRACKET_CLOSE | identifier | constant

iostmt : (READ | WRITE) NORMAL_BRACKET_OPEN identifier NORMAL_BRACKET_CLOSE

structstmt : cmpdstmt | ifstmt | whilestmt

ifstmt : IF NORMAL_BRACKET_OPEN condition NORMAL_BRACKET_CLOSE cmpdstmt

whilestmt : WHILE NORMAL_BRACKET_OPEN condition NORMAL_BRACKET_CLOSE cmpdstmt

forstmt : FOR NORMAL_BRACKET_OPEN assignstmt SEMICOLON condition SEMICOLON assignstmt NORMAL_BRACKET_CLOSE cmpdstmt

condition : expression RELATION expression

RELATION : LESS | LESS_EQUAL | EQUAL | GREATER_EQUAL | GREATER | NOT_EQUAL

%%
yyerror(char *s)
{
	printf("%s\n",s);
}

extern FILE *yyin;

main(int argc, char **argv)
{
	if(argc>1) yyin :  fopen(argv[1],"r");
	if(argc>2 && !strcmp(argv[2],"-d")) yydebug: 1;
	if(!yyparse()) fprintf(stderr, "\tOk\n");
}