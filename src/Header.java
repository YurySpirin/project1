public class Header extends Adress{
    private byte transport;
    private byte fixedCRC;
    private int packetLength;
    private int seqNumber;
    private int msg_id;
    private int msg_type;
    private int msg_ver;
    private byte modelID;
    private long serial;

    public byte getTransport() {
        return transport;
    }

    public void setTransport(byte transport) {
        this.transport = transport;
    }

    public byte getFixedCRC() {
        int l;
        for (l = 2; p[l] <= 15; l++) ;
        {fixedCRC += p[l];}
        return fixedCRC;
    }

    public void setFixedCRC(byte fixedCRC) {
        this.fixedCRC = fixedCRC;
    }

    public int getPacketLengh() {
        packetLength = (p[2] & 0xFF) + ((p[3] << 8) & 0xFF);
        return packetLength;
    }

    public void setPacketLengh(int packetLengh) {
        this.packetLength = packetLengh;
    }

    public int getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(int seqNumber) {
        this.seqNumber = seqNumber;
    }

    public int getMsg_id() {
        msg_id = ((p[7] & 0x0F) << 6) + ((p[6] & 0xFC) >>> 2);
        return msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }

    public int getMsg_type() {
        msg_type = (p[6] & 0x03);
        return msg_type;
    }

    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }

    public int getMsg_ver() {
        msg_ver = ((p[7] & 0xF0) >>> 4);
        return msg_ver;
    }

    public void setMsg_ver(int msg_ver) {
        this.msg_ver = msg_ver;
    }

    public byte getModelID() {
        modelID = p[7];
        return modelID;
    }

    public void setModelID(byte modelID) {
        this.modelID = modelID;
    }

    public long getSerial() {
        long[] lsb = new long[8];
        for (int i = 0; i < 7; i++) { lsb[i] = p[i + 9]; }
        serial = (((lsb[7] & 0xFF) << 56) + ((lsb[6] & 0xFF) << 48) + ((lsb[5] & 0xFF) << 40) + ((lsb[4] & 0xFF) << 32) + ((lsb[3] & 0xFF) << 24) + ((lsb[2] & 0xFF) << 16) + ((lsb[1] & 0xFF) << 8) + (lsb[0] & 0xFF));
        return serial;
    }

    public void setSerial(long serial) {
        this.serial = serial;
    }
}