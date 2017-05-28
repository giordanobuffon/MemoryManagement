public class OccupiedPartitions {

    private int initialAddress;
    private int finalAddress;
    private int runtime;
    private int id_process;

    public OccupiedPartitions(int initialAddress, Process process) {
        this.initialAddress = initialAddress;
        this.finalAddress = (initialAddress + process.getProcessSize()) - 1;
        this.runtime = process.getRuntime();
        this.id_process = process.getId();
    }

    public int getInitialAddress() {
        return initialAddress;
    }

    public int getFinalAddress() {
        return finalAddress;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public int getId_process() {
        return id_process;
    }
}
