package com.queues.Business;

import com.queues.Model.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class SimulationManager implements Runnable{
    //data read from the UI
    public int timeLimit = 50;
    public int maxProcessingTime = 10;
    public int minProcessingTime = 2;
    public int noServers = 3; // queues
    public int noClients = 6; //tasks
    public int minArrivalTime = 2;
    public int maxArrivalTime = 15;

    private SimulationLogger logger = new SimulationLogger("src/main/java/com/queues/Business/Log.txt");
    private Scheduler scheduler;
    private List<Task> generatedTasks;

    public SimulationManager() {
        //initialize the scheduler
        //create and start noServers threads
        //initialize the selection strategy => create a strategy
        //initialize frame to display simulation
        //generate noClients using generateNRandomTasks()
        //and store them to generatedTasks
        scheduler = new Scheduler(noServers, noClients);
        scheduler.changeStrategy(SelectionPolicy.SHORTEST_WAITING_TIME);
        generateNRandomTasks();
        for (Task task : generatedTasks) {
            System.out.print(task);
        }
    }

    private void generateNRandomTasks() {
        //generate N random tasks
        //- random processing time
        //minProcessingTime < processingTime < maxProcessingTime
        //- random arrival time
        //sort list with respect to arrivalTime
        generatedTasks = new ArrayList<>();
        Random random = new Random();

        for(int i = 0; i < noClients; i++)
        {
            int arrivalTime = minArrivalTime + random.nextInt(maxArrivalTime - minArrivalTime + 1);
            int servicetime = minProcessingTime + random.nextInt(maxProcessingTime - minProcessingTime + 1);
            Task task = new Task(i, arrivalTime, servicetime);
            generatedTasks.add(task);
        }

        generatedTasks.sort(Comparator.comparingInt(Task::getArrivalTime));
    }

    @Override
    public void run() {
        // Clear any previous log entries
        logger.clearLog();

        int currentTime = 0;
        while(currentTime < timeLimit)
        {
            //iterate generatedTasks list and pick tasks that have the arrival time equal with the currentTime
            // -send task to queue by calling the dispatchTask method from Scheduler
            // - delete the client from the list
            //update UI frame
            List<Task> toRemove = new ArrayList<>();

            for(Task task : generatedTasks)
            {
                if(task.getArrivalTime() == currentTime)
                {
                    scheduler.dispatchTask(task);
                    toRemove.add(task);
                }
            }

            generatedTasks.removeAll(toRemove);

            //wait an interval of 1 second
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

            logger.logState(currentTime, generatedTasks, scheduler.getServerList());
            currentTime++;
            //logger.printLogToConsole();
        }

        System.out.println("Simulation finished!");

        //print on screen
        logger.printLogToConsole();

        ///print to file
        logger.writeToFile();
    }

    public static void main(String[] args)
    {
        SimulationManager manager = new SimulationManager();
        Thread mainThread = new Thread(manager);
        mainThread.start();

    }

}
