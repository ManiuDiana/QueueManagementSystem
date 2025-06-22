package com.queues.Business;

import com.queues.Model.Server;
import com.queues.Model.Task;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Server> serverList = new ArrayList<>();
    private final int maxNoServers; //no queues
    private final int maxTasksPerServer; //no clients
    private Strategy strategy;

    //the constructor
    public Scheduler(int maxNoServers, int maxTasksPerServer) {
       //for maxNoServers
        // - create server object
        // - create thread with the object
        this.maxNoServers = maxNoServers;
        this.maxTasksPerServer = maxTasksPerServer;

        for(int i = 0; i < maxNoServers; i++) {
            Server newServer = new Server(i);
            serverList.add(newServer);
            Thread thread = new Thread(newServer);
            thread.start();
            System.out.println("Server " + i + " started.");
        }

        // Set a default strategy
        this.strategy = new ShortestQueueStrategy();
    }

    public void changeStrategy(SelectionPolicy policy) {
        switch (policy){
            case SHORTEST_QUEUE:
                strategy = new ShortestQueueStrategy();
                break;
            case SHORTEST_WAITING_TIME:
                strategy = new TimeStrategy();
                break;
        }
    }

    public void dispatchTask(Task task) {
        //call the strategy addTask method
        if(strategy != null) {
            strategy.addTask(serverList, task);
        }
    }

    public List<Server> getServerList() {
        return serverList;
    }

    public int getMaxNoServers() {
        return maxNoServers;
    }
    public int getMaxTasksPerServer() {
        return maxTasksPerServer;
    }
}
