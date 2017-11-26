package br.ufal.ic.easy.ast.print;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.ChildPropertyDescriptor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.SimplePropertyDescriptor;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * This is an incomplete class.
 */
public class ASTPrint extends ASTVisitor {
	
	private String result;
	private Stack<String> stack = new Stack<String>();
	private long count;
	
	private void addStack(String nodeName) {
		this.stack.push(nodeName + "_" + ++this.count);
	}
	
	private void removeStack() {
		if (this.stack.empty()) return;
		String last = this.stack.pop();
		if (!this.stack.empty()) {
			String current = this.stack.peek();
			System.out.println(current + " -- " + last);
		}
	}
	
	private void addNode(String node) {
		if (result == null) {
			this.result = node;
		} else {
			this.result += "->" + node;
		}
	}
	
	/**
	 * Print AST in dot format.
	 * @param node
	 * @param parrent
	 */
	private void print(ASTNode node, String parrent) {
		List properties = node.structuralPropertiesForType();
		System.out.println(properties.toString());
	    for (Iterator iterator = properties.iterator(); iterator.hasNext();) {
	        Object descriptor = iterator.next();
	        if (descriptor instanceof SimplePropertyDescriptor) {
	            SimplePropertyDescriptor simple = (SimplePropertyDescriptor) descriptor;
	            Object value = node.getStructuralProperty(simple);
	            System.out.println(parrent + " -- " + simple.getId() + "_" + value.toString() + "_" + ++count);
	        } else if (descriptor instanceof ChildPropertyDescriptor) {
	            ChildPropertyDescriptor child = (ChildPropertyDescriptor) descriptor;
	            ASTNode childNode = (ASTNode) node.getStructuralProperty(child);
	            if (childNode != null) {
	                //System.out.println("Child (" + child.getId() + ") {");
	            	//System.out.println(childNode.toString());
	            	//System.out.println(ASTNode.nodeClassForType(childNode.getNodeType()));
	            	String className = ASTNode.nodeClassForType( childNode.getNodeType() ).getSimpleName();
	            	className = className.replace(".", "_");
	            	String str = child.getId() + "_" + className + "_" + ++this.count;
	            	System.out.println(parrent + " -- " + str);
	                print(childNode, str);
	                //System.out.println("}");
	            } else {
	            	String str = child.getId() + "_" + ++this.count;
	            	//System.out.println(child.getChildType().getSimpleName());
	            	System.out.println(parrent + " -- " + str);
	            }
	        } else {
	            ChildListPropertyDescriptor list = (ChildListPropertyDescriptor) descriptor;
	            //System.out.println("List (" + list.getId() + "){");
	            //String nextParrent = list.getNodeClass().getSimpleName() + "_" + ++this.count;
	            String nextParrent = list.getId() + "_" + ++this.count;
	            System.out.println(parrent + " -- " + nextParrent);
	            print((List) node.getStructuralProperty(list), nextParrent);
	            //System.out.println("}");
	        }
	    }
	}
	
	private void print(List nodes, String parrent) {
	    for (Iterator iterator = nodes.iterator(); iterator.hasNext();) {
	        print((ASTNode) iterator.next(), parrent);
	    }
	}

	@Override
	public boolean visit(NumberLiteral node) {
		addStack("NumberLiteral_" + node.getToken());
		return super.visit(node);
	}
	
	@Override
	public void endVisit(NumberLiteral node) {
		removeStack();
		super.endVisit(node);
	}
	
	/**
	 * Get type from variable.??
	 * @param node
	 * @return
	 */
	/*
	  public boolean visit(VariableDeclaration node) {                                                   
	        IType iType = (IType) node.resolveBinding().getJavaElement();       
	        System.out.println(iType.getFullyQualifiedName());
	        return true;
	   }
	*/

	/**
	 * Compare two ASTNode
	 * @param left
	 * @param right
	 * @return
	 */
	 public boolean equals(ASTNode left, ASTNode right) {
	        // if both are null, they are equal, but if only one, they aren't
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
	                	SimplePropertyDescriptor simple = (SimplePropertyDescriptor) property;
	                	System.out.println(simple.getId());
	                    return false;
	                }
	            } else if (property.isChildProperty()) {
	                // recursively call this function on child nodes
	                if (!equals((ASTNode) leftVal, (ASTNode) rightVal)) {
	                    return false;
	                }
	            } else if (property.isChildListProperty()) {
	                Iterator<ASTNode> leftValIt = ((Iterable<ASTNode>) leftVal)
	                        .iterator();
	                Iterator<ASTNode> rightValIt = ((Iterable<ASTNode>) rightVal)
	                        .iterator();
	                while (leftValIt.hasNext() && rightValIt.hasNext()) {
	                    // recursively call this function on child nodes
	                    if (!equals(leftValIt.next(), rightValIt.next())) {
	                        return false;
	                    }
	                }
	                // one of the value lists have additional elements
	                if (leftValIt.hasNext() || rightValIt.hasNext()) {
	                    return false;
	                }
	            }
	        }
	        return true;
	    }
	
	@Override
	public boolean visit(IfStatement node) {
		System.out.println(equals(node, node.getParent()));
		return super.visit(node);
	}
	
	@Override
	public boolean visit(TypeDeclaration node) {
		print(node, "Class_" + node.getName() + ++this.count);
		addStack("Class_" + node.getName());
		return super.visit(node);
	}
	
	@Override
	public void endVisit(TypeDeclaration node) {
		removeStack();
		super.endVisit(node);
	}
	
	@Override
	public boolean visit(MethodDeclaration node) {
		addStack("Methoddeclaration_" + node.getName());
		return super.visit(node);
	}
	
	@Override
	public void endVisit(MethodDeclaration node){
		removeStack();
		super.endVisit(node);
	}
	
	private boolean findedClassInstanceCreation;
	public void endVisit(FieldAccess node) {
		findedClassInstanceCreation = false;
		node.accept(new ASTVisitor() {
			public void endVisit(ClassInstanceCreation node) { findedClassInstanceCreation = true; }
		});
		if (findedClassInstanceCreation) {
			System.out.println(node.toString());
		}
	}

}