package jp.ac.u_tokyo.k.is.logic;

import java.io.File;
import java.io.IOException;

public class Executor {
	private File workdir;
	private int exitValue;
	private String message = "";
	private static String javacommand;
	
	public Executor(String classpath, String policypath) {
		this.workdir = new File(classpath);
		if (javacommand == null) {
			javacommand = "java -Dfile.encoding=UTF-8 -Djava.security.manager -Djava.security.policy=\"" + policypath + "\" ";
		}
	}
	
	public int getExitValue() {
		return exitValue;
	}
	
	public String getMessage() {
		return message;
	}
	
	public boolean execute(String classname, String args) {
		String command = javacommand + classname + " " + args;
		
		ProcessBroker pb = new ProcessBroker(workdir, command.split(" "));
		boolean success = false;
		
		try {
			exitValue = pb.execute();
			StringBuilder sb = new StringBuilder();
			sb.append(pb.getStdout());
			sb.append(pb.getTimeoutStr());
			message = sb.toString();
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return success;
	}
}
