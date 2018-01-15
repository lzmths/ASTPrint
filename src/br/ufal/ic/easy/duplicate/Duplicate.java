package br.ufal.ic.easy.duplicate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import br.ufal.ic.easy.ast.context.Runner;

/**
 * 
 *
 */
public class Duplicate {

	public static void main(String[] args) {
		File file = null;
		File output = null;
		File root = null;
		for (int i = 0; i < args.length - 1; ++i) {
			if (args[i].equals("--file")) {
				file = new File(args[++i]);
			} else if (args[i].equals("--output")) {
				output = new File(args[++i]);
			} else if (args[i].equals("--root")) {
				root = new File(args[++i]);
			}
		}
		if (file == null) {
			System.err.println("Please. --file filePath");
		} else {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));) {
				String line = reader.readLine();
				Runner runnerAST = new Runner();
				while (line != null) {
					String[] paths = line.split(":");
					Program program = new Program();
					File original;
					try {
						original = program.original(new File(root + paths[0]), program.getFileName(paths[1]));
						File duplicate1 = program.mutante(new File(root + paths[1]));
						File duplicate2 = program.mutante(new File(root + paths[2]));
						runnerAST.callASTParser(original, duplicate1);
						runnerAST.callASTParser(original, duplicate2);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					line = reader.readLine();
				}
			} catch (FileNotFoundException e) {
				System.err.println("Can not open file.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
}
