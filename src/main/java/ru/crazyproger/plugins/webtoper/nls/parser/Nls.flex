package ru.crazyproger.plugins.webtoper.nls.parser;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.properties.parsing.PropertiesTokenTypes;

%%

%class _NlsLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

CRLF= \n | \r | \r\n
WHITE_SPACE_CHAR=[\ \n\r\t\f]
VALUE_CHARACTER=[^\n\r\f\\] | "\\"{CRLF} | "\\".
END_OF_LINE_COMMENT=("#"|"!")[^\r\n]*
KEY_SEPARATOR=[\ \t]*[:=][\ \t]* | [\ \t]+
KEY_CHARACTER=[^:=\ \n\r\t\f\\] | "\\"{CRLF} | "\\".
NLS_NAME=[^\n\r\f\ \\,]
NLS_SEPARATOR=[\ \t]*[,][\ \t]*

%state IN_VALUE
%state IN_KEY_VALUE_SEPARATOR

%state IN_INCLUDE_VALUE_SEPARATOR
%state IN_INCLUDE_START_LIST
%state IN_INCLUDE_LIST

%%

<YYINITIAL> {END_OF_LINE_COMMENT}        { yybegin(YYINITIAL); return PropertiesTokenTypes.END_OF_LINE_COMMENT; }

<YYINITIAL> "NLS_INCLUDES"               { yybegin(IN_INCLUDE_VALUE_SEPARATOR); return NlsTokenTypes.INCLUDE_KEYWORD;}
<YYINITIAL> {KEY_CHARACTER}+             { yybegin(IN_KEY_VALUE_SEPARATOR); return PropertiesTokenTypes.KEY_CHARACTERS; }

<IN_INCLUDE_VALUE_SEPARATOR> {KEY_SEPARATOR}        {yybegin(IN_INCLUDE_START_LIST); return  PropertiesTokenTypes.KEY_VALUE_SEPARATOR;}
<IN_INCLUDE_START_LIST> {NLS_NAME}+      { yybegin(IN_INCLUDE_LIST); return NlsTokenTypes.NLS_NAME;}
<IN_INCLUDE_LIST> {NLS_SEPARATOR}        { yybegin(IN_INCLUDE_START_LIST); return NlsTokenTypes.NLS_SEPARATOR; }
<IN_INCLUDE_LIST> {CRLF}{WHITE_SPACE_CHAR}*     { yybegin(YYINITIAL); return PropertiesTokenTypes.WHITE_SPACE; }

<IN_KEY_VALUE_SEPARATOR> {KEY_SEPARATOR} { yybegin(IN_VALUE); return PropertiesTokenTypes.KEY_VALUE_SEPARATOR; }
<IN_VALUE> {VALUE_CHARACTER}+            { yybegin(YYINITIAL); return PropertiesTokenTypes.VALUE_CHARACTERS; }

<IN_KEY_VALUE_SEPARATOR> {CRLF}{WHITE_SPACE_CHAR}*  { yybegin(YYINITIAL); return PropertiesTokenTypes.WHITE_SPACE; }
<IN_VALUE> {CRLF}{WHITE_SPACE_CHAR}*     { yybegin(YYINITIAL); return PropertiesTokenTypes.WHITE_SPACE; }
{WHITE_SPACE_CHAR}+                      { return PropertiesTokenTypes.WHITE_SPACE; }
.                                        { return PropertiesTokenTypes.BAD_CHARACTER; }