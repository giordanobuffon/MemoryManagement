public class FreePartitions {

    private int initialAddress;
    private int finalAddress;

    public FreePartitions(int initialAddress, int finalAddress) {
        this.initialAddress = initialAddress;
        this.finalAddress = finalAddress;
    }

    public int getInitialAddress() {
        return initialAddress;
    }

    public void setInitialAddress(int initialAddress) {
        this.initialAddress = initialAddress;
    }

    public int getFinalAddress() {
        return finalAddress;
    }

    public void setFinalAddress(int finalAddress) {
        this.finalAddress = finalAddress;
    }

}
