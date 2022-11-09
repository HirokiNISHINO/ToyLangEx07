package kut.compiler.parser.ast;


import kut.compiler.compiler.CodeGenerator;

public class AstEmptyStatement extends AstNode 
{

	
	/**
	 * @param t
	 */
	public AstEmptyStatement()
	{
	}

	/**
	 *
	 */
	@Override
	public void cgen(CodeGenerator gen)
	{	
		return;
	}
	

}
