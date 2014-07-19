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

    public ProcessLauncher(String jvmPath, String... commands) {
	if (jvmPath == null) {
	    jvmPath = getJavaDir();
	}
	this.jvmPath = jvmPath;
	this.commands = new ArrayList(commands.length);
	addCommands(commands);
    }

    public MinecraftProcess start()
	    throws IOException {
	List<String> full = getFullCommands();
	MinecraftProcess mc = new MinecraftProcess(full, new ProcessBuilder(full).directory(directory).redirectErrorStream(true).start());
	return mc;
    }

    public List<String> getFullCommands() {
	List<String> result = new ArrayList(commands);
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

    public String getJavaDir() {
	String separator = System.getProperty("file.separator");
	String path = System.getProperty("java.home") + separator + "bin" + separator;
	if ((OperatingSystem.getCurrentPlatform() == OperatingSystem.WINDOWS)
		&& (new File(path + "javaw.exe").isFile())) {
	    return path + "javaw.exe";
	}
	return path + "java";
    }

    public String toString() {
	return "ProcessLauncher[commands=" + commands + ", java=" + jvmPath + "]";
    }
}
