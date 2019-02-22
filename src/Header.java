public class Header {
    private byte transport;
    private byte fixedCRC;
    private int packetLengh;
    private int seqNumber;
    private int msg_id;
    private byte modelID;
    private long serial;

    public byte getTransport() {
        return transport;
    }

    public void setTransport(byte transport) {
        this.transport = transport;
    }

    public byte getFixedCRC() {
        return fixedCRC;
    }

    public void setFixedCRC(byte fixedCRC) {
        this.fixedCRC = fixedCRC;
    }

    public int getPacketLengh() {
        packetLengh = (Adress.p[2] & 0xFF) + ((Adress.p[3] << 8) & 0xFF);
        return packetLengh;
    }

    public void setPacketLengh(int packetLengh) {
        this.packetLengh = packetLengh;
    }

    public int getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(int seqNumber) {
        this.seqNumber = seqNumber;
    }

    public int getMsg_id() {
        msg_id = ((Adress.p[7] & 0x0F) << 6) + ((Adress.p[6] & 0xFC) >>> 2);
        return msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }

    public byte getModelID() {
        modelID = Adress.p[7];
        return modelID;
    }

    public void setModelID(byte modelID) {
        this.modelID = modelID;
    }

    public long getSerial() {
        return serial;
    }

    public void setSerial(long serial) {
        this.serial = serial;
    }


}



