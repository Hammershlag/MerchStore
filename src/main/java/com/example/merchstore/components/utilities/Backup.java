package com.example.merchstore.components.utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static com.example.merchstore.components.utilities.Defaults.BACKUP_INTERVAL;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 20.09.2024
 */
public class Backup {

    Thread commandThread;

    public Backup() {
        commandThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        // Determine the current working directory
                        // Get the current working directory (Windows style)
                        String currentDir = System.getProperty("user.dir");

                        // Convert the Windows path to WSL-compatible path
                        String wslPath = convertPathToWSL(currentDir) + "/database";

                        // Create a ProcessBuilder instance and set the working directory
                        ProcessBuilder processBuilder = new ProcessBuilder();

                        // PowerShell command to display the current path and execute the WSL script
                        String command ="wsl '" +wslPath+ "/backup.sh'"; // Execute the WSL script

                        // Set the command to run with PowerShell
                        processBuilder.command("powershell.exe", "-Command", command);

                        // Start the process
                        Process process = processBuilder.start();

                        // Capture standard output
                        BufferedReader stdOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        String line;
                        System.out.println("Standard Output:");
                        while ((line = stdOutput.readLine()) != null) {
                            System.out.println(line); // Print each line of the output
                        }

                        process.waitFor();


                        Thread.sleep(BACKUP_INTERVAL);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void start() {
        commandThread.start();
    }

    public void stop() {
        commandThread.interrupt();
    }

    // Method to convert a Windows path to a WSL-compatible path
    private String convertPathToWSL(String windowsPath) {
        // Replace backslashes with forward slashes
        String wslPath = windowsPath.replace("\\", "/");

        // Replace the C: drive with /mnt/c
        wslPath = wslPath.replaceFirst("^([a-zA-Z]):", "/mnt/$1").toLowerCase();

        return wslPath;
    }
}
