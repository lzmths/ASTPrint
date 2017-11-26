package br.ufal.ic.easy.ast.context;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ChildPropertyDescriptor;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;

/**
 * Generates an AST Node Sequence
 * @author Luiz Carvalho <luiz at ic dot ufal dot br>
 *
 */
public class ASTNodeSequence {

	/**
	 * Print AST node Sequence
	 * @param node mutation operation applied in this node
	 */
	public void print(ASTNode node) {
		System.out.println("AST node sequence: " + generate(node, null));
	}
	
	/**
	 * Generates AST Node Sequence recursively
	 * @param node node
	 * @param ASTNodeSequence ASTNodeSequence (start with null)
	 * @return ASTNodeSequence
	 */
	public String generate(ASTNode node, String ASTNodeSequence) {
		String currentASTNodeSequence;
		if (node == null) return ASTNodeSequence;
		if(ASTNodeSequence == null) {
			currentASTNodeSequence = "" + node.getClass().getSimpleName();
		} else {
			currentASTNodeSequence = ASTNodeSequence + "<-" + node.getClass().getSimpleName();
		}
		return generate(node.getParent(), currentASTNodeSequence);
	}

}
