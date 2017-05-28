import java.util.ArrayList;

public class Main {

    public static void main(String args[]) {

        int memorySize = 100;
        int numberProcess = 5;

        ArrayList<Process> processesReady = new ArrayList<>();
        ArrayList<Process> processesBlocked = new ArrayList<>();

        // Creation of the process
        for (int i = 0; i < numberProcess; i++) {
            Process process = new Process(memorySize, i);
            processesReady.add(process);
            System.out.printf("Processo %d: %d Kb\n", i + 1, process.getProcessSize());
        }

        System.out.printf("Criacao de %d processos concluida\n", processesReady.size());

        // Creation of lists of partitions free and occupied
        ArrayList<OccupiedPartitions> occupiedPartArray = new ArrayList<>();
        ArrayList<FreePartitions> freePartArray = new ArrayList<>();

        FreePartitions freePartitions = new FreePartitions(0, memorySize - 1);
        freePartArray.add(freePartitions);

        int lastPosition = 0;
        while (true) {
            Operations.deallocateProcess(freePartArray, occupiedPartArray, memorySize);

            Operations.firstFit(processesBlocked, processesReady, freePartArray, occupiedPartArray);

//            Operations.bestFit(processesBlocked, processesReady, freePartArray, occupiedPartArray, memorySize);

//            Operations.worstFit(processesBlocked, processesReady, freePartArray, occupiedPartArray, memorySize);

//            lastPosition = Operations.circularFit(processesBlocked, processesReady, freePartArray, occupiedPartArray, lastPosition);
        }
    }
}
