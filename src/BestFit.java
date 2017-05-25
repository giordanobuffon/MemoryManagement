public class BestFit {

    public static void main(String args[]) {

        int memorySize = 10000;
        int[] memory = new int[memorySize];
        int numberProcess = 10000;
        Process[] processes = new Process[numberProcess];
        FreePartitions[] listFreePartitions = new FreePartitions[numberProcess / 2];

        // Creation of the process
        for (int i = 0; i < numberProcess; i++) {
            processes[i] = new Process(memorySize, i);
        }

        int cycle = 0;
        int counter;
        int listCounter;
        while (true) {
            for (int i = 0; i < memorySize;){
                //memory[i] =
            }


            // list of free partitions
            listCounter = 0;
            for (int i = 0; i < numberProcess; i++) {
                counter = 0;
                if (memory[i] == 0) {
                    for (int j = i; j < numberProcess; j++) {
                        if (memory[j] != 0) break;
                        counter++;
                    }
                    listFreePartitions[listCounter] = new FreePartitions(i, counter);
                    listCounter++;
                }
            }
        }

    }
}

//String hexstr = Integer.toHexString(numberInt).toUpperCase();
