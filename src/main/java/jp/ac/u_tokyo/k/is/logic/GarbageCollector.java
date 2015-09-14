/*
 * Copyright (c) 2012, Design Engineering Laboratory, The University of Tokyo.
 * All rights reserved. 
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the project nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE PROJECT AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE PROJECT OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */

package jp.ac.u_tokyo.k.is.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a class for GarbageCollector.
 * 
 * @author Kazuo Hiekata <hiekata@k.u-tokyo.ac.jp>
 */

public class GarbageCollector extends Thread {
	private static List<Process> processList = new ArrayList<Process>();
	private static List<Integer> processDuration = new ArrayList<Integer>();
	private static boolean m_halt = false;
	
	public GarbageCollector() {
		super();
	}
	
	public void run() {
		while (! m_halt) {
			try {
				
		    	// if this is new process, keep this process.
		    	// if this is existing process, destroy this process.
				
				// clean up
				for (int i = 0; i < processList.size(); i++) {
					try {
						synchronized(processList) {
							processList.get(i).exitValue();
							processList.remove(i);
							processDuration.remove(i);
						}
					} catch (IllegalThreadStateException itse) {
						synchronized(processList) {
							// still running
							if (processDuration.get(i) > 2) {
								try {
									Process p = (Process)(processList.get(i));
									
									p.destroy();
								} catch (Exception e) {
									
								}
								processList.remove(i);
								processDuration.remove(i);
							} else {
								Integer tmp = (Integer)processDuration.get(i);
								tmp++;
								processDuration.set(i, tmp);
							}
						}
					}
				}
				// 1min=60 000msec
				int interval = 5000; //60000;
				GarbageCollector.sleep(interval * (int)(1 + 0.1 * Math.random()));
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}
	
    public void halt() {
    	GarbageCollector.m_halt = true;
        this.interrupt();
    }
	
    public static void addProcess(Process p) {
    	synchronized (processList) 
    	{
	    	GarbageCollector.processList.add(p);
	    	GarbageCollector.processDuration.add(new Integer(1));
    	}
    }
    
}
