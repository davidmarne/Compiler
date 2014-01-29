%option noyywrap

MP_IDENTIFIER          (letter | "_"(letter | digit)){["_"](letter | digit)}

%%
{MP_IDENTIFIER} printf("found a MP_PERIOD: %s \n", yytext);
%%
main(){
	printf("input:");
	yylex();
}
