package com.queues.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable{
    private int Id;
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private Task currentTask = null;

    public Server(int id) {
        this.tasks = new LinkedBlockingQueue<>();
        this.waitingPeriod = new AtomicInteger(0);
        this.Id = id;
    }

    public void addTask(Task newTask) {
        try {
            //add tasks to the queue
            tasks.put(newTask);
            //increment the waiting period
            waitingPeriod.addAndGet(newTask.getServiceTime());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Failed to add task: " + newTask + " to server " + Id);
        }
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    @Override
    public void run()
    {
        while(true)
        {
            //take the next task from the queue
            //stop the thread for a time equal with the task's processing time
            //decrement the waiting period
            try{
                currentTask = tasks.take(); //waits if the queue is empty
                System.out.println("\nThe current queue (Q" + getId() + ") has waitingPeriod= " + getWaitingPeriod());
                System.out.println("Processing task: " + currentTask);
                // Simulate processing over multiple seconds
                for (int i = 0; i < currentTask.getServiceTime(); i++) {
                    Thread.sleep(1000); // wait 1 second
                    //waitingPeriod.decrementAndGet(); // decrease by 1 each second
                    System.out.println("Processing... " + (i + 1) + " seconds of " + currentTask.getServiceTime() + " seconds" + "  The waiting period for Q" + getId() + " is now: " + getWaitingPeriod());
                }

                waitingPeriod.addAndGet(-currentTask.getServiceTime()); // decrement the waiting period by the task's service time
                System.out.println("\nFinished task: " + currentTask);
                currentTask = null; // reset current task

            } catch(InterruptedException e){
                Thread.currentThread().interrupt();
                System.out.println("Server interrupted");
                break;
            }
        }

        System.out.println("Server thread stopped.");
    }

    public BlockingQueue<Task> getTasks()
    {
        return tasks;
    }

    public List<Task> getAllTasks() {
        List<Task> allTasks = new ArrayList<>(tasks);
        if(currentTask != null) {
            allTasks.add(0, currentTask); // add the current task at the front
        }
        return allTasks;
    }

    public int getId(){
        return Id;
    }

}
