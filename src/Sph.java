import static java.lang.System.arraycopy;

public class Sph extends Adress{
    byte[] sph;

    public byte[] getSph() {
        arraycopy(p, 17, sph, 0, 44);
        return sph;
    }

    public void setSph(byte[] sph) {
        this.sph = sph;
    }
}
