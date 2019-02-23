public class CELL_ID extends Sph {
    private int cell_id;

    public int getCell_id() {
        cell_id = (sph[5] & 0xFF) + ((sph[6] & 0xFF) << 8);
        return cell_id;
    }

    public void setCell_id(int sell_id) {
        this.cell_id = sell_id;
    }
}
