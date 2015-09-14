
package jp.ac.u_tokyo.k.is.logic;

import java.io.File;


public class Marker {
	private String workdir;
	private String message = "";

	public Marker(String workdir) {
		this.workdir = workdir;
	}

	public String getMessage() {
		return message;
	}

	public boolean mark(String classname, String source, String[] args, String[] answers, String policypath) {
		Compiler compiler = new Compiler(workdir);
		boolean success = compiler.compile(classname, source);

		if (!success) {
			message = compiler.getMessage();
			return false;
		}

		Executor executor = new Executor(workdir, policypath);

		for (int i = 0; i < args.length; i++) {
			success = executor.execute(classname, args[i]);
			if (!success) {
				message = executor.getMessage();
				return false;
			}

			String result = executor.getMessage().trim();
			if (!result.equals(answers[i])) {
				StringBuilder sb = new StringBuilder();
				sb.append("The output of the program is not correct.\n\n");
				sb.append("&lt;Your Result&gt;\n");
				sb.append("&gt; java " + classname + " " + args[i] + "\n");
				sb.append(result);
				sb.append("\n\n");
				sb.append("&lt;Answer&gt;\n");
				sb.append("&gt; java " + classname + " " + args[i] + "\n");
				sb.append(answers[i]);
				message = sb.toString();
				return false;
			}
		}

		message = "Passed.";
		return true;
	}
	
	// for OOP question
	public boolean mark(String classname, String source, String[] args, String[] answers, String policypath, String oopClassname) {
		boolean success = mark(classname, source, args, answers, policypath);
		
		// check whether (oopClassname).class exists
		File oopClassFile = new File(workdir, oopClassname + ".class");
		if (!oopClassFile.exists()) {
			success = false;
			message = oopClassname + " class is not found.\nPlease create " + oopClassname + " class.";
		}
		
		return success;
	}

	public String[] getResults(String classname, String source, String[] args, String policypath) {
		//配列のサイズに合わせて、結果を格納する配列を作成
		String[] results = new String[args.length];

		Compiler compiler = new Compiler(workdir);
		boolean success = compiler.compile(classname, source);

		if (!success) {
			message = compiler.getMessage();
			return null;
		}

		Executor executor = new Executor(workdir, policypath);

		for (int i = 0; i < args.length; i++) {
			success = executor.execute(classname, args[i]);
			if (!success) {
				message = executor.getMessage();
				return null;
			}

			String result = executor.getMessage().trim();
			results[i] = result;
		}

		return results;
	}
}
