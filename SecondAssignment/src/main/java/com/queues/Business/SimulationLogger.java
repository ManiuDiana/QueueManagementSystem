package com.queues.Business;

import com.queues.Model.Server;
import com.queues.Model.Task;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class SimulationLogger {
    private StringBuilder logBuilder = new StringBuilder();
    private String filePath;

    // Constructor with custom file path
    public SimulationLogger(String filePath) {
        this.filePath = filePath;
    }



    public void logState(int currentTime, List<Task> waitingTasks, List<Server> servers) {
        logBuilder.append("Time ").append(currentTime).append("\n");

        logBuilder.append("Waiting clients: ");
        for (Task task : waitingTasks) {
            logBuilder.append(String.format("(%d,%d,%d); ", task.getId(), task.getArrivalTime(), task.getServiceTime()));
        }
        logBuilder.append("\n");

        int serverIndex = 1;
        for (Server server : servers) {
            List<Task> liveTasks = server.getAllTasks();

            // Create a snapshot of the current task list
            List<Task> snapshot = List.copyOf(liveTasks);  // snapshot won't change even if queue changes

            if (snapshot.isEmpty()) {
                logBuilder.append("Queue ").append(serverIndex).append(": closed\n");
            } else {
                logBuilder.append("Queue ").append(serverIndex).append(": ");
                for (Task t : snapshot) {
                    logBuilder.append(String.format("(%d,%d,%d); ", t.getId(), t.getArrivalTime(), t.getServiceTime()));
                }
                logBuilder.append("\n");
            }
            serverIndex++;
        }

        logBuilder.append("\n");
    }

    public void printLogToConsole() {
        System.out.println(logBuilder.toString());
    }


    public void writeToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            writer.write(logBuilder.toString());
            System.out.println("Log written to " + filePath); // Confirmation

        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Clear the log if needed
    public void clearLog() {
        logBuilder = new StringBuilder();
    }
}
