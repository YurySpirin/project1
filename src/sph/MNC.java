public class MNC extends Sph{
    private int mnc;

    public int getMnc() {
        int i1 = (sph[0] & 0xFF) | ((sph[1] & 0xFF) << 8) | ((sph[2] & 0xFF) << 16);
        mnc = i1 & 0xFFF;
        return mnc;
    }

    public void setMnc(int mnc) {
        this.mnc = mnc;
    }
}
