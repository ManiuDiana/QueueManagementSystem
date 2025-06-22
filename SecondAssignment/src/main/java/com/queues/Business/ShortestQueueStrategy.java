package com.queues.Business;

import com.queues.Model.Server;
import com.queues.Model.Task;

import java.util.List;

public class ShortestQueueStrategy implements Strategy{
    @Override
    public void addTask(List<Server> servers, Task t)
    {
        Server bestServer = servers.getFirst();
        int minQueueSize = bestServer.getTasks().size();

        for(Server server : servers){
            int queueSize = server.getTasks().size();
            if(queueSize < minQueueSize){
                minQueueSize = queueSize;
                bestServer = server;
            }
        }

        bestServer.getTasks().add(t);
    }

}
