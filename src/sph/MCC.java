

public class MCC extends Sph{
    private int mcc;

    public int getMcc() {
        int i1 = (sph[0] & 0xFF) | ((sph[18] & 0xFF) << 8) | ((sph[19] & 0xFF) << 16);
        int mcc = i1 >>> 12;
        return mcc;
    }

    public void setMcc(int mcc) {
        this.mcc = mcc;
    }
}
