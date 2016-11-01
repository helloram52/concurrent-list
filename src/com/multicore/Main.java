package com.multicore;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Utils.setLogger();

        int totalOperationsToPerform = RunParameters.TARGET_OPERATION_COUNT_FOR_EACH_MODE.value;
        if (args.length == 1) {
            totalOperationsToPerform = Integer.valueOf(args[0]);
        }

        int processors = Runtime.getRuntime().availableProcessors();
        int numberOfThreads = processors * 2;
        Utils.logInfo("Number of available CPU cores #" + processors);

        for(int i = 1;i <= RunParameters.NUMBER_OF_RUNS.value;i++) {
            Utils.logInfo("Run  #" + i + ":");

            //new ListRunner().run(2, totalOperationsToPerform);
            for (int j = 1;j <= numberOfThreads;j++) {
                Utils.logInfo("Number of threads: " + j);
                new ListRunner().run(j, totalOperationsToPerform, i);
            }
        }
    }
}
