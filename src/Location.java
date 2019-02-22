import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Location {




    public static void main (String arr[]) {
        //5B CD 83 4A 5D DC C0 00 00 1F 47 FF FF FB FC
        byte [] b = new byte [19];
        b[0] = (byte) 0x6F;
        b[1] = (byte) 0x08;
        b[2] = (byte) 0xB3;
        b[3] = (byte) 0xBA;
        b[4] = (byte) 0x5B;
        b[5] = (byte) 0xCD;
        b[6] = (byte) 0x83;
        b[7] = (byte) 0x4A;
        b[8] = (byte) 0x5D;
        b[9] = (byte) 0xDC;
        b[10] = (byte) 0xC0;

        int valid;
        int date;
        double latitude;
        double longitude;
        int speed;
        int direction;


        //valid = ((b[0] >>> 7) & 1);

        int day = ((b[0] >>> 2) & 0x1F);
        int month = ((b[0] & 0x03) << 2) + ((b[1] & 0xFF) >>> 6);
        int year = ((((b[1] & 0x3F) << 1) + ((b[2] >>> 7) & 1) + 2000));
        int hour = (((b[2] & 0xFF) >>> 2) & 0x1F);
        int minute = (((b[2] & 0x03) << 4) + ((b[3] >>> 4) & 0x0F));
        int second = ((b[3] & 0x0F) << 2) + ((b[4] >>> 6) & 0x03);

        ZonedDateTime zdt = ZonedDateTime.of(year, month, day, hour, minute, second, 0, ZoneId.of("UTC"));
        //date = Date.from(zdt.toInstant());

        int lat = ((b[7] >>> 5) & 0x07) + ((b[6] & 0xFF) << 3) + ((b[5] & 0xFF) << (3 + 8)) + ((b[4] & 0x1F) << (3 + 8 + 8));
        latitude = (lat / 100000D) - 90D;
        System.out.println(latitude);
        int lon = ((b[10] >>> 3) & 0x1F) + ((b[9] & 0xFF) << 5) + ((b[8] & 0xFF) << (5 + 8)) + ((b[7] & 0x1F) << (5 + 8 + 8));
        longitude = (lon / 100000D) - 180D;
        System.out.println(longitude);
        speed = ((b[11] >>> 2) & 0x3F) + ((b[10] & 0x07) << 6);
        direction = ((b[12] >>> 1) & 0x7F) + ((b[11] & 0xFF) << 7);
        /*altitude = (((b[14] >>> 3) & 0x1F) + ((b[13] & 0xFF) << 5) + ((b[12] & 0x01) << (5 + 8)) - 1000);
        offset = ((b[17] >>> 3) & 0x1F) + ((b[16] & 0xFF) << 5) + ((b[15] & 0xFF) << (5 + 8)) + ((b[14] & 0x07) << (5 + 8 + 8));

        ref = Ref.values()[((b[17] >>> 2) & 1)];
        sat = ((b[18] >>> 5) & 0x07) + ((b[17] & 0x03) << 3);

        gpsState = GpsState.values()[((b[18] >>> 4) & 0x01)];
        offsetValid = OffsetValid.values()[((b[18] >>> 3) & 0x01)];
        accuracy = Accuracy.values()[((b[18] >>> 2) & 0x01)];
    */
    }
}