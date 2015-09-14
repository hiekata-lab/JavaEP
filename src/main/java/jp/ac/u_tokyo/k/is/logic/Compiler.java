package jp.ac.u_tokyo.k.is.logic;

import java.io.File;
import java.util.Arrays;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

public class Compiler {
	private String dstPath;
	private String message = "";
	
	public Compiler(String dstPath) {
		this.dstPath = dstPath;
	}
	
	public String getMessage() {
		return message;
	}
	
	public boolean compile(String classname, String source) {
		// delete old class files
		File[] classFiles = Util.listFiles(dstPath, ".class");
		for (File file : classFiles) {
			file.delete();
		}
		
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		
		JavaFileObject javaFileObj = new JavaStringSourceObject(classname, source);
		
		Iterable<String> compilationOptions = Arrays.asList(new String[] { "-d", dstPath, "-encoding", "UTF-8" });
		Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(javaFileObj);
		CompilationTask task = compiler.getTask(null, null, diagnostics, compilationOptions, null, compilationUnits);
		
		boolean success = task.call();
		
		StringBuilder sb = new StringBuilder();
		for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
			String error = "Error on line " + diagnostic.getLineNumber() + " in " + diagnostic + "\n";
			sb.append(error);
		}
		
		message = sb.toString();
		if (message.trim().isEmpty()) {
			message = "Compiled.";
		}
		
		return success;
	}
}
