package br.ufal.ic.easy.ast.context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 * Creates a AST from Original and Mutant, after compare both.
 * 
 * @author Luiz Carvalho <luiz at ic dot ufal dot br>
 * 
 */

public class Runner {
	
	/**
	 * Run
	 * @param args arguments with original program and mutant
	 */
	public void run(String args[]) {
		File original = null, mutant =  null;
		for (int i = 0; i < args.length - 1; ++i) {
			if (args[i].equals("--original")) {
				original = new File(args[++i]);
			} else if (args[i].equals("--mutant")) {
				mutant = new File(args[++i]);
			} else {
				System.out.println("Please. --original path and --mutant path");
				return;
			}
		}
		try {
			if (original == null || mutant == null) return;
			CompilationUnit cuOriginal = parse(readFileToString(original));
			CompilationUnit cuMutant = parse(readFileToString(mutant));
			ASTCompare aSTCompare = new ASTCompare();
			boolean result = aSTCompare.equals(cuOriginal.getRoot(), cuMutant.getRoot(), true);
			System.out.println("Are they equals? " + result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Parse the code string with ASTParser from JDT
	 * @param str code
	 */
	private CompilationUnit parse(String str) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(str.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);		
		return cu;
	}
	
	/**
	 * File to string
	 * @param filePath file path
	 * @return code string
	 * @throws IOException
	 */
	private String readFileToString(File filePath) throws IOException {
		StringBuilder fileData = new StringBuilder(100);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[10];
		int numRead = 0;
		while((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return fileData.toString();
	}
}
