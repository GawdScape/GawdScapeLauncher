package com.gawdscape.launcher.launch;

import com.gawdscape.launcher.util.OperatingSystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Vinnie
 */
public final class ProcessLauncher {

    private final String jvmPath;
    private final List<String> commands;
    private File directory;

    public ProcessLauncher(String jvmPath, int memory, String commands) {
	if (jvmPath == null) {
	    jvmPath = OperatingSystem.getJavaDir();
	}
	this.jvmPath = jvmPath;
	this.commands = new ArrayList<>();
	if (commands != null && !commands.isEmpty()) {
	    if (!commands.toLowerCase().contains("-xmx")) {
		addCommands("-Xmx" + memory + "M");
	    }
	    addSplitCommands(commands);
	} else {
	    addCommands("-Xmx" + memory + "M");
	}
    }

    public MinecraftProcess start()
	    throws IOException {
	List<String> full = getFullCommands();
	MinecraftProcess process = new MinecraftProcess(full, new ProcessBuilder(full).directory(directory).redirectErrorStream(true).start());
        ProcessMonitorThread monitor = new ProcessMonitorThread(process);
        monitor.start();
        return process;
    }

    public List<String> getFullCommands() {
	List<String> result = new ArrayList<>(commands);
	result.add(0, getJavaPath());
	return result;
    }

    public List<String> getCommands() {
	return commands;
    }

    public void addCommands(String... commands) {
	this.commands.addAll(Arrays.asList(commands));
    }

    public void addSplitCommands(String commands) {
	addCommands(commands.split(" "));
    }

    public ProcessLauncher directory(File directory) {
	this.directory = directory;

	return this;
    }

    public File getDirectory() {
	return directory;
    }

    protected String getJavaPath() {
	return jvmPath;
    }

    @Override
    public String toString() {
	return "ProcessLauncher[commands=" + commands + ", java=" + jvmPath + "]";
    }
}
