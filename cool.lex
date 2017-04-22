/*
 *  The scanner definition for COOL.
 */

import java_cup.runtime.Symbol;

%%

%{

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */

    // Max size of string constants
    static int MAX_STR_CONST = 1025;

    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();

    private int curr_lineno = 1;
    private int cmt_depth = 0;
    int get_curr_lineno() {
  return curr_lineno;
    }

    private AbstractSymbol filename;

    void set_filename(String fname) {
  filename = AbstractTable.stringtable.addString(fname);
    }

    AbstractSymbol curr_filename() {
  return filename;
    }

%}

%init{

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */

    // empty for now
%init}

%eofval{

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */

    switch(yy_lexical_state) {
    case YYINITIAL:
      /* nothing special to do in the initial state */
      break;
      /* If necessary, add code for other states here, e.g:*/
    case COMMENT:
      yybegin(YYINITIAL); 
      if (cmt_depth > 0 )
        return new Symbol(TokenConstants.ERROR, "EOF in comment");
    }
    return new Symbol(TokenConstants.EOF);
%eofval}

%state COMMENT,STRING
comment=([^\*\(]|"("[^\*]|"*"[^\)])*
whitespace=[ \f\r\t\x0b]+
int_const=[0-9]+
alphanum=[_a-zA-Z0-9]
boolean=t[Rr][Uu][Ee]|f[Aa][Ll][Ss][Ee] 
else=[eE][lL][sS][eE]
case=[cC][aA][sS][eE]
then=[tT][hH][eE][nN]
class=[cC][lL][aA][sS][sS]

%class CoolLexer
%cup

%%

<YYINITIAL>"=>"     { /* Sample lexical rule for "=>" arrow.
                                     Further lexical rules should be defined
                                     here, after the last %% separator */
                      return new Symbol(TokenConstants.DARROW); }
<YYINITIAL>"--"[^\n]* { /* comment */; }
<YYINITIAL>"*)"     { return new Symbol(TokenConstants.ERROR, yytext());}
<YYINITIAL>"(*"     { cmt_depth++;
                      yybegin(COMMENT);
                    }
<COMMENT>{comment}  { /*comment*/ }
<COMMENT>"(*"  { cmt_depth++; }
<COMMENT>"*)"  { cmt_depth--; 
                  if(cmt_depth==0) 
                    yybegin(YYINITIAL); 
               } 
<YYINITIAL>{whitespace} { /* white space */ }
<YYINITIAL>\n     { curr_lineno++; }

<YYINITIAL>"*"    { return new Symbol(TokenConstants.MULT);}
<YYINITIAL>"inherits"   { return new Symbol(TokenConstants.INHERITS);}
<YYINITIAL>{boolean}    { return new Symbol(TokenConstants.BOOL_CONST, new Boolean(yytext().toLowerCase()));}
<YYINITIAL>"pool"   { return new Symbol(TokenConstants.POOL);}
<YYINITIAL>{case}   { return new Symbol(TokenConstants.CASE);}
<YYINITIAL>"("      { return new Symbol(TokenConstants.LPAREN);}
<YYINITIAL>";"      { return new Symbol(TokenConstants.SEMI);}
<YYINITIAL>"-"      { return new Symbol(TokenConstants.MINUS);}
<YYINITIAL>")"      { return new Symbol(TokenConstants.RPAREN);}
<YYINITIAL>"not"    { return new Symbol(TokenConstants.NOT);}
<YYINITIAL>"<"      { return new Symbol(TokenConstants.LT);}
<YYINITIAL>"in"     { return new Symbol(TokenConstants.IN);}
<YYINITIAL>","      { return new Symbol(TokenConstants.COMMA);}
<YYINITIAL>{class}  {  return new Symbol(TokenConstants.CLASS); }
<YYINITIAL>"fi"     {  return new Symbol(TokenConstants.FI); }
<YYINITIAL>"/"      {  return new Symbol(TokenConstants.DIV); }
<YYINITIAL>"loop"   {  return new Symbol(TokenConstants.LOOP); }
<YYINITIAL>"+"      {  return new Symbol(TokenConstants.PLUS); }
<YYINITIAL>"<-"     {  return new Symbol(TokenConstants.ASSIGN); }
<YYINITIAL>"if"     {  return new Symbol(TokenConstants.IF); }
<YYINITIAL>"."      {  return new Symbol(TokenConstants.DOT); }
<YYINITIAL>"<="     {  return new Symbol(TokenConstants.LE); }
<YYINITIAL>"of"     {  return new Symbol(TokenConstants.OF); }
<YYINITIAL>{int_const}  {  return new Symbol(TokenConstants.INT_CONST, new IntSymbol(yytext(), yytext().length(), 0)); }
<YYINITIAL>"new"      {  return new Symbol(TokenConstants.NEW); }
<YYINITIAL>"isvoid"   {  return new Symbol(TokenConstants.ISVOID); }
<YYINITIAL>"="        {  return new Symbol(TokenConstants.EQ); }
<YYINITIAL>":"        {  return new Symbol(TokenConstants.COLON); }
<YYINITIAL>"~"        {  return new Symbol(TokenConstants.NEG); }
<YYINITIAL>"{"        {  return new Symbol(TokenConstants.LBRACE); }
<YYINITIAL>{else}     {  return new Symbol(TokenConstants.ELSE); }
<YYINITIAL>"while"    {  return new Symbol(TokenConstants.WHILE); }
<YYINITIAL>"esac"     {  return new Symbol(TokenConstants.ESAC); }
<YYINITIAL>"let"      {  return new Symbol(TokenConstants.LET); }
<YYINITIAL>"}"        {  return new Symbol(TokenConstants.RBRACE); }
<YYINITIAL>{then}     {  return new Symbol(TokenConstants.THEN); }
<YYINITIAL>"@"        {  return new Symbol(TokenConstants.AT); }
<YYINITIAL>[A-Z]{alphanum}* {return new Symbol(TokenConstants.TYPEID, new IdSymbol(yytext(), yytext().length(), 0));}
<YYINITIAL>[a-z]{alphanum}* {return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
<YYINITIAL>\"((\\[^\x00])|[^\x00\n\"\\])*\" { yybegin(YYINITIAL);String text = yytext(); 
StringBuilder value = new StringBuilder();
for(int i = 1; i<text.length()-1;i++){
  char c = text.charAt(i);
  if (c == '\\') {
    i++;
    char cNext = text.charAt(i);  
    switch (cNext) {
      case '\\': c = '\\'; break;
      case '"': c = '\"'; break;
      case 'n': c = '\n'; break;
      case 't': c = '\t'; break;
      case 'b': c = '\b'; break;
      case 'f': c = '\f'; break;
      default: c = cNext; break;
    }
  }
  value.append(c);  
}
text = value.toString();
if (text.length() >= MAX_STR_CONST)
  return new Symbol(TokenConstants.ERROR, "String constant too long");
return new Symbol(TokenConstants.STR_CONST, new StringSymbol(text, text.length(), 0)); }
<YYINITIAL>\"((\\[^\x00])|[^\x00\"\\])*\n { return new Symbol(TokenConstants.ERROR, "Unterminated string constant");}
<YYINITIAL>\"((\\.)|[^\n\"\\])*[\n\"] { return new Symbol(TokenConstants.ERROR, "String contains null character");}
<YYINITIAL>\"((\\[^\x00])|[^\x00\n\"\\])* { return new Symbol(TokenConstants.ERROR, "EOF in string constant");}

.                   { /* This rule should be the very last
                      in your lexical specification and
                      will match match everything not
                      matched by other lexical rules. */
                      return new Symbol(TokenConstants.ERROR, yytext()); }
