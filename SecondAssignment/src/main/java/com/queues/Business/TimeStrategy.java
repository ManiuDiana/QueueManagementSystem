package com.queues.Business;

import com.queues.Model.Server;
import com.queues.Model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class TimeStrategy implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task t) {
        Server bestServer = servers.getFirst();
        int minWaitingTime = bestServer.getWaitingPeriod().get();

        for (Server server : servers) {
            int serverWaitingTime = server.getWaitingPeriod().get();
            if (serverWaitingTime < minWaitingTime) {
                minWaitingTime = serverWaitingTime;
                bestServer = server;
            }
        }


        bestServer.addTask(t);


        BlockingQueue<Task> currentTasks = bestServer.getTasks();
        // Print tasks as a list
        System.out.print("\nTasks in Q" + bestServer.getId() + ":");
        if (currentTasks.isEmpty()) {
            System.out.println("  No tasks");
        } else {
            // Convert to List to avoid modifying the queue
            List<Task> taskList = new ArrayList<>(currentTasks);
            for (int i = 0; i < taskList.size(); i++) {
                Task task = taskList.get(i);
                System.out.print(" (" + task.getId() +
                        ", " + task.getArrivalTime() + ", " + task.getServiceTime() + ") ");
            }
        }
    }
}
