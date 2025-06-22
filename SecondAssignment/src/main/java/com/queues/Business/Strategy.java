package com.queues.Business;

import com.queues.Model.Server;
import com.queues.Model.Task;

import java.util.List;

public interface Strategy {

    public void addTask(List<Server> servers, Task t);
}
