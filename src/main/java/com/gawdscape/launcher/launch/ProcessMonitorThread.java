package com.gawdscape.launcher.launch;

import com.gawdscape.launcher.GawdScapeLauncher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;

/**
 *
 * @author Vinnie
 */
public class ProcessMonitorThread extends Thread {

    private final MinecraftProcess process;

    public ProcessMonitorThread(MinecraftProcess process) {
	super("ProcessMonitor");
	this.process = process;
    }

    @Override
    public void run() {
	String line;
	try (BufferedReader buf = new BufferedReader(new InputStreamReader(
                process.getRawProcess().getInputStream(), "UTF-8"))) {
            while ((line = buf.readLine()) != null) {
                System.out.println(line);
                if (GawdScapeLauncher.logFrame != null) {
                    GawdScapeLauncher.logFrame.formatAndPrintLine(line);
                }
            }
        } catch (IOException ex) {
            GawdScapeLauncher.LOGGER.log(Level.SEVERE, "Error reading from Minecraft process", ex);
        }
        MinecraftExit onExit = process.getExitRunnable();
        if (onExit != null) {
            onExit.onMinecraftExit(process);
        }
    }
}
