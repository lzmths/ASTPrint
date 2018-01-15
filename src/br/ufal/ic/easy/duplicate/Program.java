package br.ufal.ic.easy.duplicate;

import java.io.File;
import java.io.IOException;

public class Program {

	private boolean isJavaFile(File file) {
		return file.isFile() && file.getName().endsWith(".java");
	}
	
	public File mutante(File folder) throws IOException {
		if (folder != null) {
			if (folder.isDirectory()) {
				File[] files = folder.listFiles();
				for (File file: files) {
					if (file.isDirectory()) {
						return mutante(file);
					} else if (isJavaFile(file)) {
						return file;
					}
				}
			}
		}
		throw new IOException("It is not a folder or correct folder");
	}
	
	public File original(File folder, String name) throws IOException {
		if (folder != null) {
			if (folder.isDirectory()) {
				File[] files = folder.listFiles();
				for (File file: files) {
					if (file.isDirectory()) {
						return original(file, name);
					} else if (isJavaFile(file) && file.getName().contains(name)) {
						return file;
					}
				}
			}
		}
		throw new IOException("It is not a folder or correct folder");
	}
	
	public String getFileName(String pathName) throws Exception {
		String[] folderNames = pathName.split("/");
		for (int i = 0; i < folderNames.length; ++i) {
			if (folderNames[i].contains("ClassId")) return folderNames[i];
		}
		throw new Exception("Do not contains ClassId");
	}
}
