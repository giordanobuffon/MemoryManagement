public class OccupiedPartitions {

    private int initialAddress;
    private int finalAddress;
    private int runtime;

    public OccupiedPartitions(int initialAddress, Process process) {
        this.initialAddress = initialAddress;
        this.finalAddress = initialAddress + process.getProcessSize();
        this.runtime = process.getRuntime();
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

    public void setInitialAddress(int initialAddress) {
        this.initialAddress = initialAddress;
    }

    public void setFinalAddress(int finalAddress) {
        this.finalAddress = finalAddress;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }
}
