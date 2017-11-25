package br.ufal.ic.easy.ast.context;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.SimplePropertyDescriptor;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;

/**
 * This Class compare two AST.
 * In this particular case: original program and mutant
 * 
 * @author Luiz Carvalho <luiz at ic dot ufal dot br>
 * 
 */
public class ASTCompare {
	
	/**
	 * Compare two ast.
	 * Modified from Boldizsár Németh in https://stackoverflow.com/questions/974855/eclipse-abstract-syntax-tree-diff
	 * @param left original program
	 * @param right mutant
	 * @param canPrint if true then print. otherwise, do not print.
	 * @return are they equals?
	 */
	public boolean equals(ASTNode left, ASTNode right, boolean canPrint) {
		ASTNodeSequence nSequence = new ASTNodeSequence();
	    if (left == null && right == null) {
	    	return true;
	    } else if (left == null || right == null) {
	    	return false;
	    }
	        // if node types are the same we can assume that they will have the same
	        // properties
	        if (left.getNodeType() != right.getNodeType()) {
	            return false;
	        }
	        List<StructuralPropertyDescriptor> props = left.structuralPropertiesForType();
	        for (StructuralPropertyDescriptor property : props) {
	            Object leftVal = left.getStructuralProperty(property);
	            Object rightVal = right.getStructuralProperty(property);
	            if (property.isSimpleProperty()) {
	                // check for simple properties (primitive types, Strings, ...)
	                // with normal equality
	                if (!leftVal.equals(rightVal)) {
	                	if (canPrint) {
	                		SimplePropertyDescriptor simple = (SimplePropertyDescriptor) property;
	                		Object leftValue = left.getStructuralProperty(simple);
	                		Object rightValue = right.getStructuralProperty(simple);
	                		//System.out.println(simple.getId());
	                		System.out.println("Mutantation in: " + simple.getValueType().getSimpleName());
	                		System.out.println("Mutant parent: " + left.toString());
	                		System.out.println("Mutant: " + leftValue.toString());
	                		System.out.println("Original parent: " + right.toString());
	                		System.out.println("Original: " + rightValue.toString());
	                		nSequence.print(right);
	                	}
	                	return false;
	                }
	            } else if (property.isChildProperty()) {
	                // recursively call this function on child nodes
	                if (!equals((ASTNode) leftVal, (ASTNode) rightVal, canPrint)) {
	                	return false;
	                }
	            } else if (property.isChildListProperty()) {
	                Iterator<ASTNode> leftValIt = ((Iterable<ASTNode>) leftVal)
	                        .iterator();
	                Iterator<ASTNode> rightValIt = ((Iterable<ASTNode>) rightVal)
	                        .iterator();
	                while (leftValIt.hasNext() && rightValIt.hasNext()) {
	                    // recursively call this function on child nodes
	                    if (!equals(leftValIt.next(), rightValIt.next(), canPrint)) {
	                        return false;
	                    }
	                }
	                // one of the value lists have additional elements
	                if (leftValIt.hasNext() || rightValIt.hasNext()) {
                    	ChildListPropertyDescriptor list = (ChildListPropertyDescriptor) property;
                    	if (canPrint) {
                    		System.out.println("Mutant in: " + list.getId());
                    		nSequence.print(right);
                    	}
	                    return false;
	                }
	            }
	        }
	        return true;
	    }
}
