# ToyLangEx07
Toy Programming Language: Ex07

生成した構文木を表示できる機能を追加．

現状の文法．

\<Program\> ::= \<Statements\>

\<Statements\> ::= [\<Statement\>]\*

\<Statement\> :: = \<ExprStmt>　| \<EmptyStmt\>

\<ExprStmt> ::= \<AdditiExpr\>　 ';'

\<EmptyStmt\> :: = ';'

\<AdditiExpr\>　:: = \<MultiplicativeExpr\> [ ( '+' | '-' ) \<MultiplicativeExpr\> ]\*

\<MultiplicativeExpr\> :: = \<Primary\> [ ( '\*'  | '/' ) \<Primary\> ]\*

\<Primary\> :: = ( \<expr\> ) | \<Integer\>
