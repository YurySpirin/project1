/*import java.sql.Ref;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import com.google.common.io.BaseEncoding;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;

import java.nio.ByteBuffer;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;


public class Point {

    private int x;
    private int y;

    public Point() {
    }

    public Point(int x, int y) {
        setX(x);
        setY(y);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    private Valid valid;
    private Date date;
    private double latitude;
    private double longitude;
    private int speed;
    private int direction;
    private int altitude;
    private int offset;
    private Ref ref;
    private int sat;
    private GpsState gpsState;
    private OffsetValid offsetValid;
    private Accuracy accuracy;

    public void fill(byte[] b) {
//        nav.fill(b);
//
//        byte[] bytes = nav.bytes();


//        String encode = BaseEncoding.base16().withSeparator(" ", 2).encode(bytes);
//        String encode1 = "1C 05 00 00 D8 2C A2 02 B8 91 00 89 AA E8 19 53 BB 88 E2";
        int i = 0;


        byte[] b = BaseEncoding.base16().withSeparator(" ", 2).decode("FC 04 00 00 E8 2C 82 00 30 93 00 49 AF E8 19 0E C2 08 D7");


        ArrayUtils.reverse(b);

        valid = Valid.values()[((b[0] >>> 7) & 1)];

        int day = ((b[0] >>> 2) & 0x1F);
        int month = ((b[0] & 0x03) << 2) + ((b[1] & 0xFF) >>> 6);
        int year = ((((b[1] & 0x3F) << 1) + ((b[2] >>> 7) & 1) + 2000));
        int hour = (((b[2] & 0xFF) >>> 2) & 0x1F);
        int minute = (((b[2] & 0x03) << 4) + ((b[3] >>> 4) & 0x0F));
        int second = ((b[3] & 0x0F) << 2) + ((b[4] >>> 6) & 0x03);

        ZonedDateTime zdt = ZonedDateTime.of(year, month, day, hour, minute, second, 0, ZoneId.of("UTC"));
        date = Date.from(zdt.toInstant());

        int lat = ((b[7] >>> 5) & 0x07) + ((b[6] & 0xFF) << 3) + ((b[5] & 0xFF) << (3 + 8)) + ((b[4] & 0x1F) << (3 + 8 + 8));
        latitude = (lat / 100000D) - 90D;

        int lon = ((b[10] >>> 3) & 0x1F) + ((b[9] & 0xFF) << 5) + ((b[8] & 0xFF) << (5 + 8)) + ((b[7] & 0x1F) << (5 + 8 + 8));
        longitude = (lon / 100000D) - 180D;

        speed = ((b[11] >>> 2) & 0x3F) + ((b[10] & 0x07) << 6);
        direction = ((b[12] >>> 1) & 0x7F) + ((b[11] & 0xFF) << 7);
        altitude = (((b[14] >>> 3) & 0x1F) + ((b[13] & 0xFF) << 5) + ((b[12] & 0x01) << (5 + 8)) - 1000);
        offset = ((b[17] >>> 3) & 0x1F) + ((b[16] & 0xFF) << 5) + ((b[15] & 0xFF) << (5 + 8)) + ((b[14] & 0x07) << (5 + 8 + 8));

        ref = Ref.values()[((b[17] >>> 2) & 1)];
        sat = ((b[18] >>> 5) & 0x07) + ((b[17] & 0x03) << 3);

        gpsState = GpsState.values()[((b[18] >>> 4) & 0x01)];
        offsetValid = OffsetValid.values()[((b[18] >>> 3) & 0x01)];
        accuracy = Accuracy.values()[((b[18] >>> 2) & 0x01)];
    }
}

*/