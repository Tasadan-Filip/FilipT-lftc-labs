%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int lines = 0;
char **constantSymbolTable;
int constantLength = 10;
int constantPointer = 0;
char **identifierSymbolTable;
int identifierLength = 10;
int identifierPointer = 0;
struct PifRecord{
    int code; //0 - token, 1 - identifier, 2 - constant
    char *token;
    int position;
};
struct PifRecord* pif;
int pifLength = 10;
int pifPointer = 0;

void symbolTableInitialization()
{
    constantSymbolTable = malloc(constantLength * sizeof(char*));
    identifierSymbolTable = malloc(identifierLength * sizeof(char*));
    pif = malloc(pifLength * sizeof(struct PifRecord));
}

void addConstantToSymbolTable (char *constant)
{
    if (constantLength == constantPointer) {
        char **new_constantSymbolTable = malloc(constantLength * 2 * sizeof (char*));
        for (int i = 0; i < constantLength; ++i)
            new_constantSymbolTable[i] = constantSymbolTable[i];
        free(constantSymbolTable);
        constantLength *= 2;
        constantSymbolTable = new_constantSymbolTable;
    }
    constantSymbolTable[constantPointer++] = constant;
}

void addIdentifierToSymbolTable (char *identifier)
{
    if (identifierLength == identifierPointer) {
        char **new_identifierSymbolTable = malloc(identifierLength * 2 * sizeof (char*));
        for (int i = 0; i < identifierLength; ++i)
            new_identifierSymbolTable[i] = identifierSymbolTable[i];
        free(identifierSymbolTable);
        identifierLength *= 2;
        identifierSymbolTable = new_identifierSymbolTable;
    }
    identifierSymbolTable[identifierPointer++] = identifier;
}

int getConstantIndex(char *string)
{
    for (int i = 0; i < constantPointer; ++i)
        if (strcmp(string, constantSymbolTable[i]) == 0)
            return i;
    addConstantToSymbolTable(string);
    return constantPointer-1;
}

int getIdentifierIndex(char *identifier)
{
    for (int i = 0; i < identifierPointer; ++i)
        if (strcmp(identifier, identifierSymbolTable[i]) == 0)
            return i;
    addIdentifierToSymbolTable(identifier);
    return identifierPointer-1;
}

void addToPif(struct PifRecord PifRecord)
{
    if (pifLength == pifPointer) {
        struct PifRecord *new_pif = malloc(pifLength * 2 * sizeof (struct PifRecord));
        for (int i = 0; i < pifLength; ++i)
            new_pif[i] = pif[i];
        free(pif);
        pifLength *= 2;
        pif = new_pif;
    }
    pif[pifPointer++] = PifRecord;
}

struct PifRecord getPifRecord(int opcode, char *token, int pos) {
    struct PifRecord PifRecord;
    PifRecord.code = opcode;
    PifRecord.position = pos;
    PifRecord.token = token;
    return PifRecord;
};

char* string_copy(char *string) {
    int size = (int)strlen(string);
    char* newString = malloc((size+1) * sizeof (char));
    for (int i = 0; i <= size; ++i)
        newString[i] = string[i];
    return newString;
}

%}
 
%option noyywrap
%option caseless
 
WORD        \'[0-9a-zA-Z]+\'
NUMBER      [+-]?[1-9][0-9]*|0$
CONSTANT    {WORD}|{NUMBER}
ID          [a-zA-Z][a-zA-Z0-9]*
 
%%
 
"char"      {char *token = string_copy(yytext); addToPif(getPifRecord(0, token, -1)); printf("Reserved word: %s\n", yytext);}
"const"     {char *token = string_copy(yytext); addToPif(getPifRecord(0, token, -1)); printf("Reserved word: %s\n", yytext);}
"for"       {char *token = string_copy(yytext); addToPif(getPifRecord(0, token, -1)); printf("Reserved word: %s\n", yytext);}
"else"      {char *token = string_copy(yytext); addToPif(getPifRecord(0, token, -1)); printf("Reserved word: %s\n", yytext);}
"if"        {char *token = string_copy(yytext); addToPif(getPifRecord(0, token, -1)); printf("Reserved word: %s\n", yytext);}
"int"       {char *token = string_copy(yytext); addToPif(getPifRecord(0, token, -1)); printf("Reserved word: %s\n", yytext);}
"bool"      {char *token = string_copy(yytext); addToPif(getPifRecord(0, token, -1)); printf("Reserved word: %s\n", yytext);}
"read"      {char *token = string_copy(yytext); addToPif(getPifRecord(0, token, -1)); printf("Reserved word: %s\n", yytext);}
"write"     {char *token = string_copy(yytext); addToPif(getPifRecord(0, token, -1)); printf("Reserved word: %s\n", yytext);}
"var"       {char *token = string_copy(yytext); addToPif(getPifRecord(0, token, -1)); printf("Reserved word: %s\n", yytext);}
"while"     {char *token = string_copy(yytext); addToPif(getPifRecord(0, token, -1)); printf("Reserved word: %s\n", yytext);}
 
{ID}        {char *id = string_copy(yytext); addToPif(getPifRecord(1, NULL, getIdentifierIndex(id))); printf( "Identifier: %s\n", yytext );}
{CONSTANT}  {char *constant = string_copy(yytext); addToPif(getPifRecord(2, NULL, getConstantIndex(constant))); printf( "Constant: %s\n", yytext );}
 
"{"     {printf( "Separator: %s\n", yytext );}
"}"     {printf( "Separator: %s\n", yytext );}
"["     {printf( "Separator: %s\n", yytext );}
"]"     {printf( "Separator: %s\n", yytext );}
";"     {printf( "Operator: %s\n", yytext );}
"*"     {printf( "Operator: %s\n", yytext );}
"/"     {printf( "Operator: %s\n", yytext );}
"=="    {printf( "Operator: %s\n", yytext );}
"!="    {printf( "Operator: %s\n", yytext );}
">="    {printf( "Operator: %s\n", yytext );}
"<="    {printf( "Operator: %s\n", yytext );}
"<"     {printf( "Operator: %s\n", yytext );}
">"     {printf( "Operator: %s\n", yytext );}
"%"     {printf( "Operator: %s\n", yytext );}
 
[ \t]+ {}
 
[\n]+ {++lines;}
 
. {printf("Error at token %s at line %d\n", yytext, lines); exit(1);}
%%
int main(int argc, char **arg v) 
{ 
    if ( argc > 1 ) 
        yyin = fopen(argv[1], "r"); 
    else 
        yyin = stdin;
    symbolTableInitialization();
    yylex();
    printf("Identifiers ST:\n");
    for (int i = 0; i < identifierPointer; ++i)
        printf("%s\n", identifierSymbolTable[i]);
    printf("Constants ST:\n");
    for (int i = 0; i < constantPointer; ++i)
        printf("%s\n", constantSymbolTable[i]);
    printf("PIF\n");
    for (int i = 0; i < pifPointer; ++i)
        printf("%d %s %d\n", pif[i].code, pif[i].token, pif[i].position);
}