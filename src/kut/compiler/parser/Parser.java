package kut.compiler.parser;

import java.io.IOException;

import kut.compiler.exception.CompileErrorException;
import kut.compiler.exception.SyntaxErrorException;
import kut.compiler.lexer.Lexer;
import kut.compiler.lexer.Token;
import kut.compiler.lexer.TokenClass;
import kut.compiler.parser.ast.AstNode;
import kut.compiler.parser.ast.AstBinOp;
import kut.compiler.parser.ast.AstIntLiteral;
import kut.compiler.parser.ast.AstEmptyStatement;
import kut.compiler.parser.ast.AstProgram;
import kut.compiler.parser.ast.AstStatements;

/**
 * @author hnishino
 *
 */
public class Parser 
{
	protected Lexer 	lexer;
	protected Token		currentToken;
	
	/**
	 * @param lexer
	 */
	public Parser(Lexer lexer) {
		this.lexer = lexer;
	}
	
	
	
	/**
	 * @return
	 */
	protected Token getCurrentToken()
	{
		return this.currentToken;
	}
	
	/**
	 * 
	 */
	/**
	 * 
	 */
	protected void consumeCurrentToken() throws IOException, CompileErrorException
	{
		this.currentToken = lexer.getNextToken();
		return;
	}
	
	/**
	 * @return
	 */
	public AstNode parse() throws IOException, CompileErrorException
	{
		//read the first token.
		consumeCurrentToken();
		return program();
	}
	 

	/**
	 * @return
	 */
	protected AstNode program() throws IOException, CompileErrorException
	{
		AstNode node = statements ();
		
		//check if  the program is appropriately terminated with EOF
		if (this.getCurrentToken().getC() != TokenClass.EOF) {
			throw new SyntaxErrorException("the program is not appropriately terminated.");
		}
		
		return new AstProgram(node);
	}
	
	
	/**
	 * @return
	 * @throws IOException
	 * @throws CompileErrorException
	 */
	protected AstNode statements() throws IOException, CompileErrorException
	{
		AstStatements statements = new AstStatements();
		while(true) {
			Token t = this.getCurrentToken();
			//handle EOF.
			if (t.getC() == TokenClass.EOF) {
				break;
			}
			
			AstNode s = this.statement();
			statements.addStatement(s);
			
		}
		return statements;
	}
	
	/**
	 * @return
	 * @throws IOException
	 * @throws CompileErrorException
	 */
	public AstNode emptyStmt() throws IOException, CompileErrorException
	{
		Token t = this.getCurrentToken();
		if (t.getC() != ';') {
			throw new CompileErrorException("expected ';', but found :" + t);
		}
		
		this.consumeCurrentToken();
		
		return new AstEmptyStatement();
	}
	
	/**
	 * @return
	 */
	public AstNode statement() throws IOException, CompileErrorException
	{
		int tc = this.getCurrentToken().getC();

		//empty statement
		if (tc == ';') {
			return emptyStmt();
		}
		
		//expr statement.
		return exprStmt();
	}
	
	/**
	 * @return
	 * @throws IOException
	 * @throws CompileErrorException
	 */
	public AstNode exprStmt() throws IOException, CompileErrorException
	{
		AstNode e = this.additiveExpr();
		
		Token t = this.getCurrentToken();
		if (t.getC() != ';') {
			throw new SyntaxErrorException("expected ';' but found: " + t.getL());
		}
		this.consumeCurrentToken();
		
		return e;
	}
	
	/**
	 * @return
	 * @throws IOException
	 * @throws CompileErrorException
	 */
	public AstNode additiveExpr() throws IOException, CompileErrorException
	{
		AstNode lhs = multiplicativeExpr();
		while(true) {
			Token t = this.getCurrentToken();
			
			if (t == null) {
				break;
			}
			
			if (t.getC()!= '+' && t.getC() != '-') {
				break;
			}
			this.consumeCurrentToken();
			
			AstNode rhs = multiplicativeExpr();
			AstNode binop = new AstBinOp(lhs, rhs, t);
			lhs = binop;
		}
		return lhs;

	}
	
	/**
	 * @return
	 * @throws IOException
	 * @throws CompileErrorException
	 */
	public AstNode multiplicativeExpr() throws IOException, CompileErrorException
	{
		AstNode lhs = primary();
		
		while(true) {
			Token t = this.getCurrentToken();
			
			if (t == null) {
				break;
			}
			
			if (t.getC() != '*' && t.getC() != '/') {
				break;
			}
			this.consumeCurrentToken();
			
			AstNode rhs = primary();
			AstNode binop = new AstBinOp(lhs, rhs, t);
			lhs = binop;
		}
		
		return lhs;
	}
	

	/**
	 * @return
	 * @throws IOException
	 * @throws CompileErrorException
	 */
	public AstNode primary() throws IOException, CompileErrorException
	{	
		//( expr )
		Token t = this.getCurrentToken();
		if (t.getC() == '(') {
			this.consumeCurrentToken();
			
			AstNode e = additiveExpr();
			
			t = this.getCurrentToken();
			if (t.getC() != ')') {
				throw new SyntaxErrorException("expected ')' but found : " + t.getL() );
			}
			
			this.consumeCurrentToken();
			
			return e;
		}
		
		// if not integer.
		return integer();
	}
	 
	 
	
	/**
	 * @return
	 * @throws IOException
	 * @throws CompileErrorException
	 */
	protected AstNode integer() throws IOException, CompileErrorException
	{
		Token t = this.getCurrentToken();
		if (t.getC() != TokenClass.IntLiteral) {
			throw new SyntaxErrorException("expected an integer literal, but found: " + t.getL());
		}
		
		AstNode node = new AstIntLiteral(t);
		this.consumeCurrentToken();
		
		return node;
	}
	
	
}
