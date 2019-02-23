public class LAC extends Sph{
    private int lac;

    public int getLac() {
        lac = (sph[3] & 0xFF) + ((sph[4] & 0xFF) << 8);
        return lac;
    }

    public void setLac(int lac) {
        this.lac = lac;
    }
}
