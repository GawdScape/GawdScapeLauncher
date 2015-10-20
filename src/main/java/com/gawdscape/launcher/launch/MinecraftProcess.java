package com.gawdscape.launcher.launch;

import java.util.List;

/**
 *
 * @author Vinnie
 */
public class MinecraftProcess {

    private final List<String> commands;
    private final Process process;
    private MinecraftExit onExit;

    public MinecraftProcess(List<String> commands, Process process) {
	ProcessMonitorThread monitor = new ProcessMonitorThread(this);
	this.commands = commands;
	this.process = process;

	monitor.start();
    }

    public Process getRawProcess() {
	return process;
    }

    public List<String> getStartupCommands() {
	return commands;
    }

    public String getStartupCommand() {
	return process.toString();
    }

    public boolean isRunning() {
	try {
	    process.exitValue();
	} catch (IllegalThreadStateException ex) {
	    return true;
	}
	return false;
    }

    public void setExitRunnable(MinecraftExit runnable) {
	onExit = runnable;
    }

    public void safeSetExitRunnable(MinecraftExit runnable) {
	setExitRunnable(runnable);
	if ((!isRunning())
		&& (runnable != null)) {
	    runnable.onMinecraftExit(this);
	}
    }

    public MinecraftExit getExitRunnable() {
	return onExit;
    }

    public int getExitCode() {
        return process.exitValue();
    }

    @Override
    public String toString() {
	return "MinecraftProcess[commands=" + commands + ", isRunning=" + isRunning() + "]";
    }

    public void stop() {
	process.destroy();
    }
}
