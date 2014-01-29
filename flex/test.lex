%option noyywrap

/* Definitions */
letter ::= a|b|c|...|z|A|B|C|...|Z
digit  ::= 0|1|2|3|4|5|6|7|8|9

MP_AND            "and"
MP_BEGIN          "begin"
MP_DIV            "div"
MP_DO             "do"
MP_DOWNTO         "downto"
MP_ELSE           "else"
MP_END            "end"
MP_FIXED          "fixed" 
MP_FLOAT          "float"
MP_FOR            "for"
MP_FUNCTION       "function"
MP_IF             "if"
MP_INTEGER        "integer"
MP_MOD            "mod"
MP_NOT            "not"
MP_OR             "or"
MP_PROCEDURE      "procedure"
MP_PROGRAM        "program"
MP_READ           "read"
MP_REPEAT         "repeat"
MP_THEN           "then"
MP_TO             "to"
MP_UNTIL          "until"	
MP_VAR            "var"
MP_WHILE          "while"
MP_WRITE          "write"

MP_IDENTIFIER          (letter | "_"(letter | digit)){["_"](letter | digit)}
	
MP_INTEGER_LIT         digit{digit}
MP_FIXED_LIT           digit{digit} "." digit{digit}  
	
MP_FLOAT_LIT           (digit{digit} | digit{digit} "." digit{digit}) ("e"|"E")["+"|"-"]digit{digit}
MP_STRING_LIT          "'" {"''" | AnyCharacterExceptApostropheOrEOL} "'" 

MP_PERIOD         "."
MP_COMMA          ","
MP_SCOLON         ";"
MP_LPAREN         "("
MP_RPAREN         ")"
MP_EQUAL          "="
MP_GTHAN          ">"
MP_GEQUAL         ">="
MP_LTHAN          "<"
MP_LEQUAL         "<="
MP_NEQUAL         "<>"
MP_ASSIGN         ":="
MP_PLUS           "+"
MP_MINUS          "-"
MP_TIMES          "*"
MP_COLON	  ":"
%%
{MP_GTHAN}          printf("Found a: %s !\n", yytext);
{MP_GEQUAL}         printf("Found a: %s !\n", yytext);
{MP_LTHAN}          printf("Found a: %s !\n", yytext);
{MP_LEQUAL}         printf("Found a: %s !\n", yytext);
{MP_NEQUAL}         printf("Found a: %s !\n", yytext);
{MP_ASSIGN}         printf("Found a: %s !\n", yytext);
{MP_PLUS}           printf("Found a: %s !\n", yytext);
{MP_MINUS}          printf("Found a: %s !\n", yytext);
{MP_TIMES}          printf("Found a: %s !\n", yytext);
{MP_COLON}          printf("Found a: %s !\n", yytext);
{MP_PERIOD}         printf("Found a: %s !\n", yytext);
{MP_COMMA}          printf("Found a: %s !\n", yytext);
{MP_SCOLON}         printf("Found a: %s !\n", yytext);
{MP_LPAREN}         printf("Found a: %s !\n", yytext);
{MP_RPAREN}         printf("Found a: %s !\n", yytext);
{MP_EQUAL}          printf("Found a: %s !\n", yytext);
%%
/* User Code */
main() {
	printf("Give me your inputs:\n");
	yylex();
}

