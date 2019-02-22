/*import static java.lang.System.out;

public class MyClass {

    public static void main(String[] args) {
        Point p = new Point ();
        p.setX(10);
        p.setY(20);
        out.println(p.getY());
        Point p1 = new Point();
        out.println(p1.getX() + " " + p1.getY());
        Point p2 = new Point(20, 30);
        out.println(p2.getX() + " " + p2.getY());
        byte [] div;
        div = new byte [17];
        div [7] = (byte) 0x13;
        div [6] = (byte) 0x3C;

        int MSG_ID = ((div[7] & 0x0F) << 6) + ((div[6] & 0xFC) >>> 2);
        int MSG_TYPE = (div[6] & 0x03);
        int MSG_VER = ((div[7] & 0xF0) >>> 4);
        out.println("Message_id " + MSG_ID);
        out.println("Message_type " + MSG_TYPE);
        out.println("Message_ver " + MSG_VER);



        Packet packet = new Packet((byte) 1,(byte)9,17,19);

        packet.getHeader();


        /*
        Rectangle rect = new Rectangle();
        rect.topLeft.x = 0;
        rect.topLeft.y = 0;
        rect.bottomRight.x = 100;
        rect.bottomRight.y = 100;
        System.out.println(rect.topLeft.x + " " + rect.topLeft.y + " " + rect.bottomRight.x + " " + rect.bottomRight.y);

    }
}
*/