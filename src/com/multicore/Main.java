package com.multicore;

public class Main {

    public static void main(String[] args) {
        Utils.setLogger();

        int processors = Runtime.getRuntime().availableProcessors();
        Utils.log("Number of available CPU cores #" + processors);

        for(int i = 1;i <= RunParameters.NUMBER_OF_RUNS.value;i++) {
            Utils.log("Run  #" + i + ":");

            for (int j = 1;j <= processors;j++) {
                Utils.log("Number of Processors: " + j);
                new MutexRunner().run(j);
            }
        }
    }
}
