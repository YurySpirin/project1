public class Packet {
    private byte header;
    private byte fixedCRC;
    private int packetLengh;
    private int seqNumber;
    private int div;
    private byte modelID;
    private long serial;
    private byte [] sph;
    private byte [] gpsGlonassPosition;
    private byte [] payload;



    public Packet (byte header, byte fixedCRC, int packetLengh, int seqNumber){

        this.header = header;
        this.fixedCRC = fixedCRC;
        this.packetLengh = packetLengh;
        this.seqNumber = seqNumber;


    }

    public Packet(byte[] packet) {
        header = packet[0];
        fixedCRC = packet [1];
        packetLengh = ((packet[3] & 0xFF) << 8) + (packet[2] & 0xFF);
        seqNumber = packet[4] + packet[5];
        

    }


    public byte getHeader() {
        return header;
    }
}

