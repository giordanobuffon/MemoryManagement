import java.util.ArrayList;

public class Operations {

    public static void deallocateProcess(ArrayList<FreePartitions> freePartArray, ArrayList<OccupiedPartitions> occupiedPartArray, int memorySize) {
        boolean negative = false;
        boolean createPart;
        int c;
        int distance;
        int position;
        int n;
        for (int i = 0; i < occupiedPartArray.size(); i++) {
            createPart = true;
            c = occupiedPartArray.get(i).getRuntime() - 1;
            occupiedPartArray.get(i).setRuntime(c);
            // Checks adjacent partitions
            if (c == 0) {
                System.out.printf("P%d esta concluido\n", occupiedPartArray.get(i).getId_process());
                for (int k = 0; k < freePartArray.size(); k++) {
                    if (occupiedPartArray.get(i).getFinalAddress() + 1 == freePartArray.get(k).getInitialAddress()) {
                        if (k > 0 && freePartArray.get(k - 1).getFinalAddress() + 1 == occupiedPartArray.get(i).getInitialAddress()) {
                            freePartArray.get(k).setInitialAddress(freePartArray.get(k - 1).getInitialAddress());
                            freePartArray.remove(k - 1);
                        } else {
                            freePartArray.get(k).setInitialAddress(occupiedPartArray.get(i).getInitialAddress());
                        }
                        System.out.printf("Particao foi reajustada devido as adjacentes: %d a %d\n", freePartArray.get(k).getInitialAddress(), freePartArray.get(k).getFinalAddress());
//                        System.out.printf("Particao foi reajustada devido as adjacentes: %s a %s\n", hexstr(freePartArray.get(k).getInitialAddress()), hexstr(freePartArray.get(k).getFinalAddress()));
                        createPart = false;
                        break;
                    } else if (occupiedPartArray.get(i).getInitialAddress() - 1 == freePartArray.get(k).getFinalAddress()) {
                        if (k < freePartArray.size() - 1 && freePartArray.get(k + 1).getInitialAddress() - 1 == occupiedPartArray.get(i).getFinalAddress()) {
                            freePartArray.get(k).setFinalAddress(freePartArray.get(k + 1).getFinalAddress());
                            freePartArray.remove(k + 1);
                        } else {
                            freePartArray.get(k).setFinalAddress(occupiedPartArray.get(i).getFinalAddress());
                        }
                        System.out.printf("Particao foi reajustada devido as adjacentes: %d a %d\n", freePartArray.get(k).getInitialAddress(), freePartArray.get(k).getFinalAddress());
                        createPart = false;
                        break;
                    }
                }
                // Checks where the object will be add in the list of free partitions
                distance = memorySize;
                position = 0;
                if (createPart) {
                    FreePartitions freePartitions = new FreePartitions(occupiedPartArray.get(i).getInitialAddress(), occupiedPartArray.get(i).getFinalAddress());
                    System.out.printf("Nova particao livre criada, com o endereco: %d a %d\n", freePartitions.getInitialAddress(), freePartitions.getFinalAddress());
                    for (int k = 0; k < freePartArray.size(); k++) {
                        n = freePartArray.get(k).getInitialAddress() - freePartitions.getInitialAddress();
                        if (distance > Math.abs(n)) {
                            if (n < 0) {
                                negative = true;
                            }
                            distance = Math.abs(n);
                            position = k;
                        } else break;
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

    public static void firstFit(ArrayList<Process> processesBlocked, ArrayList<Process> processesReady,
                                ArrayList<FreePartitions> freePartArray, ArrayList<OccupiedPartitions> occupiedPartArray) {
        firstCircularFit(processesBlocked, processesReady, freePartArray, occupiedPartArray, 0);
    }

    public static int circularFit(ArrayList<Process> processesBlocked, ArrayList<Process> processesReady,
                                  ArrayList<FreePartitions> freePartArray, ArrayList<OccupiedPartitions> occupiedPartArray, int lastPosition) {

        return firstCircularFit(processesBlocked, processesReady, freePartArray, occupiedPartArray, lastPosition);
    }

    public static void bestFit(ArrayList<Process> processesBlocked, ArrayList<Process> processesReady,
                               ArrayList<FreePartitions> freePartArray, ArrayList<OccupiedPartitions> occupiedPartArray, int memorySize) {

        bestWorstFit(processesBlocked, processesReady, freePartArray, occupiedPartArray, memorySize, true);
    }

    public static void worstFit(ArrayList<Process> processesBlocked, ArrayList<Process> processesReady,
                                ArrayList<FreePartitions> freePartArray, ArrayList<OccupiedPartitions> occupiedPartArray, int memorySize) {

        bestWorstFit(processesBlocked, processesReady, freePartArray, occupiedPartArray, memorySize, false);
    }

    private static int firstCircularFit(ArrayList<Process> processesBlocked, ArrayList<Process> processesReady,
                                        ArrayList<FreePartitions> freePartArray, ArrayList<OccupiedPartitions> occupiedPartArray, int startingPosition) {
        boolean added;
        int position = startingPosition;

        for (int f = startingPosition; f < freePartArray.size(); f++) {
            added = false;

            for (int b = 0; b < processesBlocked.size(); b++) {
                added = allocateProcess(processesBlocked, freePartArray, occupiedPartArray, f, b);
                if (added) {
                    position = f;
                    break;
                } else {
                    System.out.printf("P%d (%d Kb)(bloq) nao foi alocado por falta de espaco nas particoes livres\n", processesBlocked.get(b).getId(), processesBlocked.get(b).getProcessSize());
                }
            }

            if (!added) {
                for (int r = 0; r < processesReady.size(); r++) {
                    added = allocateProcess(processesReady, freePartArray, occupiedPartArray, f, r);
                    if (added) {
                        position = f;
                        break;
                    } else {
                        System.out.printf("P%d (%d Kb) foi para a fila de bloqueados porque nao pode ser alocado\n", processesReady.get(r).getId(), processesReady.get(r).getProcessSize());
                        processesBlocked.add(processesReady.get(r));
                        processesReady.remove(r);
                        r--;
                    }
                }
            }
            if (added) break;
        }
        return position;
    }

    private static void bestWorstFit(ArrayList<Process> processesBlocked, ArrayList<Process> processesReady, ArrayList<FreePartitions> freePartArray,
                                     ArrayList<OccupiedPartitions> occupiedPartArray, int memorySize, boolean best) {
        boolean added = false;
        for (int b = 0; b < processesBlocked.size(); b++) {
            if (best) {
                added = smaller(freePartArray, occupiedPartArray, processesBlocked, memorySize, b);
            } else {
                added = larger(freePartArray, occupiedPartArray, processesBlocked, b);
            }

            if (added) break;
            else {
                System.out.printf("P%d (%d Kb)(bloq) nao foi alocado por falta de espaco nas particoes livres\n", processesBlocked.get(b).getId(), processesBlocked.get(b).getProcessSize());
            }
        }
        if (!added) {
            for (int r = 0; r < processesReady.size(); r++) {
                if (best) {
                    added = smaller(freePartArray, occupiedPartArray, processesReady, memorySize, r);
                } else {
                    added = larger(freePartArray, occupiedPartArray, processesReady, r);
                }

                if (added) break;
                else {
                    System.out.printf("P%d (%d Kb) foi para a fila de bloqueados porque nao pode ser alocado\n", processesReady.get(r).getId(), processesReady.get(r).getProcessSize());
                    processesBlocked.add(processesReady.get(r));
                    processesReady.remove(r);
                    r--;
                }
            }
        }
    }

    private static boolean smaller(ArrayList<FreePartitions> freePartArray, ArrayList<OccupiedPartitions> occupiedPartArray,
                                   ArrayList<Process> processes, int memorySize, int p) {

        int initAddressFree;
        int finalAddressFree;
        int position = -1;
        int smaller = memorySize;
        int procSize = processes.get(p).getProcessSize();

        for (int f = 0; f < freePartArray.size(); f++) {
            initAddressFree = freePartArray.get(f).getInitialAddress();
            finalAddressFree = freePartArray.get(f).getFinalAddress() + 1;
            if (finalAddressFree - initAddressFree >= procSize && finalAddressFree - initAddressFree <= smaller) {
                smaller = finalAddressFree - initAddressFree;
                position = f;
            }
        }
        if (position > -1) {
            allocateProcess(processes, freePartArray, occupiedPartArray, position, p);
            return true;
        } else return false;
    }

    private static boolean larger(ArrayList<FreePartitions> freePartArray, ArrayList<OccupiedPartitions> occupiedPartArray,
                                  ArrayList<Process> processes, int p) {

        int initAddressFree;
        int finalAddressFree;
        int position = -1;
        int larger = 0;
        int procSize = processes.get(p).getProcessSize();

        for (int f = 0; f < freePartArray.size(); f++) {
            initAddressFree = freePartArray.get(f).getInitialAddress();
            finalAddressFree = freePartArray.get(f).getFinalAddress() + 1;
            if (finalAddressFree - initAddressFree >= procSize && finalAddressFree - initAddressFree >= larger) {
                larger = finalAddressFree - initAddressFree;
                position = f;
            }
        }
        if (larger > procSize) {
            allocateProcess(processes, freePartArray, occupiedPartArray, position, p);
            return true;
        } else return false;
    }

    private static boolean allocateProcess(ArrayList<Process> processes, ArrayList<FreePartitions> freePartArray,
                                           ArrayList<OccupiedPartitions> occupiedPartArray, int f, int p) {

        int initAddressFree = freePartArray.get(f).getInitialAddress();
        int finalAddressFree = freePartArray.get(f).getFinalAddress();
        int procSize = processes.get(p).getProcessSize();
        if (procSize - 1 <= finalAddressFree - initAddressFree) {
            OccupiedPartitions occupiedPartitions = new OccupiedPartitions(initAddressFree, processes.get(p));
            occupiedPartArray.add(occupiedPartitions);
            System.out.printf("P%d (%d Kb) alocado em uma particao livre, do endereco %d a %d \n", processes.get(p).getId(), procSize, occupiedPartitions.getInitialAddress(), occupiedPartitions.getInitialAddress() + processes.get(p).getProcessSize() - 1);
            if (((finalAddressFree - initAddressFree) - procSize) > 0) {
                freePartArray.get(f).setInitialAddress(initAddressFree + procSize);
                System.out.printf("Espaco restante da particao apos alocacao: %d Kb, do endereco %d a %d\n", freePartArray.get(f).getFinalAddress() - freePartArray.get(f).getInitialAddress() + 1, freePartArray.get(f).getInitialAddress(), freePartArray.get(f).getFinalAddress());

            } else {
                freePartArray.remove(f);
            }
            processes.remove(p);
            return true;
        } else return false;
    }

//    private static String hexstr(int number) {
//        return Integer.toHexString(number).toUpperCase();
//    }


}