import java.util.Random;

public class Process {

    private int processSize;
    private int runtime;
    private int id;

    public Process(int memorySize, int id) {
        Random random = new Random();
        processSize = random.nextInt(memorySize) + 1;
        runtime = random.nextInt(20) + 1;
        this.id = id + 1;
    }

    public int getProcessSize() {
        return processSize;
    }

    public int getRuntime() {
        return runtime;
    }

    public int getId() {
        return id;
    }
}
