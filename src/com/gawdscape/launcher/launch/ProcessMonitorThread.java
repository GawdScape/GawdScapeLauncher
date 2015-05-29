package com.gawdscape.launcher.launch;

import com.gawdscape.launcher.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Vinnie
 */
public class ProcessMonitorThread
		extends Thread {

	private final MinecraftProcess process;

	public ProcessMonitorThread(MinecraftProcess process) {
		this.process = process;
	}

	@Override
	public void run() {
		setName("ProcessMonitor");
		InputStreamReader reader = new InputStreamReader(process.getRawProcess().getInputStream());
		BufferedReader buf = new BufferedReader(reader);
		String line;
		try {
			while ((line = buf.readLine()) != null) {
				Log.println(line);
			}
		} catch (IOException ex) {
			Log.error("Error reading from Minecraft process", ex);
		} finally {
			try {
				reader.close();
			} catch (IOException ex) {
				Log.error("", ex);
			}
		}
		MinecraftExit onExit = process.getExitRunnable();
		if (onExit != null) {
			onExit.onMinecraftExit(process);
		}
	}
}
