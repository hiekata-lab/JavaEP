package jp.ac.u_tokyo.k.is.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProcessBroker {
	private File workdir;
	private List<String> command = new ArrayList<String>();
	private StringBuilder stdout = new StringBuilder();
	private String timeoutStr = "";
	
	public String getStdout() {
		return stdout.toString();
	}
	
	public String getTimeoutStr() {
		return timeoutStr;
	}
	
	public ProcessBroker(File workdir, String... command) {
		this.workdir = workdir;
		this.command.addAll(Arrays.asList(command));
	}
	
	public int execute() throws IOException, InterruptedException {
		ProcessBuilder pb = new ProcessBuilder(command);
		pb.directory(workdir);
		pb.redirectErrorStream(true);
		
		Process process = pb.start();
		
		GarbageCollector.addProcess(process);
		
		long timeout = 5000; // 5 seconds
		long start = System.currentTimeMillis();
		
		InputStreamReader isr = new InputStreamReader(process.getInputStream());
		BufferedReader br = new BufferedReader(isr);
		
		try {
			while (true) {
				if (System.currentTimeMillis() - start > timeout) {
					process.destroy();
					timeoutStr = "Timeout at process.";
					return 1;
				}
				
				if (br.ready()) {
					String line;
					String lastLine = "";
					int overdrive = 0;
					
					while ((line = br.readLine()) != null) {
						stdout.append(line + "\n");
						
						if (System.currentTimeMillis() - start > timeout) {
							process.destroy();
							timeoutStr = "Timeout at reading stream.";
							break;
						}
						
						// if same output continues 10 times, destroy this process
						if (line.equals(lastLine)) {
							if (overdrive++ > 10) {
								process.destroy();
								break;
							}
						} else {
							overdrive = 0;
						}
						lastLine = line;
					}
				}
				
				try {
					process.exitValue();
					return 0;
				} catch (IllegalThreadStateException e) {
					// running
				}
			}
		} finally {
			br.close();
			isr.close();
		}
	}
}
