import java.util.ArrayList;

public class Main {

    public static void main(String args[]) {

        int memorySize = 10000;
        int numberProcess = 10000;
        Process[] processes = new Process[numberProcess];

        // Creation of the process
        for (int i = 0; i < numberProcess; i++) {
            processes[i] = new Process(memorySize, i);
        }

        // Creation of lists of partitions free and occupied
        ArrayList<OccupiedPartitions> occupiedPartArray = new ArrayList<>();
        ArrayList<FreePartitions> freePartArray = new ArrayList<>();
        OccupiedPartitions occupiedPartitions = new OccupiedPartitions(0, processes[0]);
        occupiedPartArray.add(occupiedPartitions);

        // Memory allocation for the first time
        boolean test = false;
        int j = 1;
        int lastAddress = 0;
        while (!test) {
            lastAddress = occupiedPartArray.get(j - 1).getFinalAddress();
            if ((lastAddress + processes[j].getProcessSize()) < memorySize) {
                occupiedPartitions = new OccupiedPartitions(lastAddress, processes[j]);
                occupiedPartArray.add(occupiedPartitions);
                j++;
            } else {
                test = true;
            }
        }

        // Space remaining
        FreePartitions freePartitions = new FreePartitions(lastAddress, memorySize);
        freePartArray.add(freePartitions);

        boolean negative = false;
        boolean createPart;
        int c;
        int distance;
        int position;
        int n;
        while (true) {
            for (int i = 0; i < occupiedPartArray.size(); i++) {
                createPart = true;
                c = occupiedPartArray.get(i).getRuntime() - 1;
                occupiedPartArray.get(i).setRuntime(c);
                // Checks adjacent partitions
                if (c == 0) {
                    for (int k = 0; k < freePartArray.size(); k++) {
                        if (occupiedPartArray.get(i).getFinalAddress() + 1 == freePartArray.get(k).getInitialAddress()) {
                            if (freePartArray.get(k - 1).getFinalAddress() + 1 == occupiedPartArray.get(i).getInitialAddress()) {
                                freePartArray.get(k).setInitialAddress(freePartArray.get(k - 1).getInitialAddress());
                                freePartArray.remove(k - 1);
                            } else {
                                freePartArray.get(k).setInitialAddress(occupiedPartArray.get(i).getInitialAddress());
                            }
                            createPart = false;
                            break;
                        } else if (occupiedPartArray.get(i).getInitialAddress() - 1 == freePartArray.get(k).getFinalAddress()) {
                            if (freePartArray.get(k + 1).getInitialAddress() - 1 == occupiedPartArray.get(i).getFinalAddress()) {
                                freePartArray.get(k).setFinalAddress(freePartArray.get(k + 1).getFinalAddress());
                                freePartArray.remove(k + 1);
                            } else {
                                freePartArray.get(k).setFinalAddress(occupiedPartArray.get(i).getFinalAddress());
                            }
                            createPart = false;
                            break;
                        }

                    }
                    // Checks where the object will be add in the list of free partitions
                    distance = memorySize;
                    position = 0;
                    if (createPart) {
                        freePartitions = new FreePartitions(occupiedPartArray.get(i).getInitialAddress(), occupiedPartArray.get(i).getFinalAddress());
                        for (int k = 0; k < freePartArray.size(); k++) {
                            n = freePartArray.get(k).getInitialAddress() - freePartitions.getInitialAddress();
                            if (distance > Math.abs(n)) {
                                if (n < 0) {
                                    negative = true;
                                }
                                distance = Math.abs(n);
                                position = k;
                            } else {
                                break;
                            }
                        }
                        // Adds the object to the list of free partitions
                        if (negative) {
                            freePartArray.add(position + 1, freePartitions);
                        } else {
                            freePartArray.add(position, freePartitions);
                        }
                    }
                    occupiedPartArray.remove(i);
                }
            }



        }
    }
}

//String hexstr = Integer.toHexString(numberInt).toUpperCase();
