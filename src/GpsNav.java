//package ru.nb.system.service.nb.cs.driver.icd42.payloads.gpsnav;

import com.google.common.io.BaseEncoding;
import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;

import java.nio.ByteBuffer;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class GpsNav {

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

    public enum Accuracy {
        LOCATION_IS_INACCURATE, LOCATION_IS_ACCURATE
    }
    public enum OffsetValid {
        INVALID,VALID
    }
    public enum Ref {
        GPS,
        UTC
    }
    public enum Valid {
        INVALID,
        VALID
    }

    public enum GpsState {
        OFF,ON
    }

    public GpsNav() {
    }

    public GpsNav(ByteBuffer bb) {
        byte[] b = new byte[19];
        bb.get(b);
        fill(b);
    }

    public GpsNav(Accuracy accuracy, int altitude, Date date, int direction, GpsState gpsState, double latitude, double longitude, int offset, OffsetValid offsetValid, Ref ref, int sat, int speed, Valid valid) {
        this.accuracy = accuracy;
        this.altitude = altitude;
        this.date = date;
        this.direction = direction;
        this.gpsState = gpsState;
        this.latitude = latitude;
        this.longitude = longitude;
        this.offset = offset;
        this.offsetValid = offsetValid;
        this.ref = ref;
        this.sat = sat;
        this.speed = speed;
        this.valid = valid;
    }

    public static void main(String[] args) {
        GpsNav nav = new GpsNav();
        nav.date = new Date();
        nav.latitude = 55.766668;
        nav.longitude = 37.404408;
        nav.offset = 16777215;
        nav.sat = 31;
        nav.gpsState = GpsState.ON;
        nav.offsetValid = OffsetValid.VALID;
        nav.accuracy = Accuracy.LOCATION_IS_ACCURATE;
        nav.ref = Ref.GPS;
        nav.valid = Valid.INVALID;
        String encode = BaseEncoding.base16().withSeparator(" ", 2).encode(nav.bytes());


        GpsNav aNew = new GpsNav();
        aNew.fill(nav.bytes());

        byte[] b = BaseEncoding.base16().withSeparator(" ", 2).decode("68 89 2D 2B 1B CD 83 4A 5D DC C0 00 00 1F 47 FF FF FB FC");

        nav.fill(b);

        byte[] bytes = nav.bytes();


//        String encode = BaseEncoding.base16().withSeparator(" ", 2).encode(bytes);
//        String encode1 = "1C 05 00 00 D8 2C A2 02 B8 91 00 89 AA E8 19 53 BB 88 E2";
        int i = 0;
    }

    public void fill(byte[] b) {

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

    public byte[] bytes() {

        validate();

        int lat = (int) ((latitude + 90D) * 100000D);
        int lon = (int) ((longitude + 180D) * 100000D);

        ZonedDateTime zdt = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.of("UTC"));

        byte[] b = new byte[19];
        b[0] |= (valid.ordinal() << 7) | (zdt.getDayOfMonth() << 2) | (((zdt.getMonth().getValue() >>> 2) & 0x03));
        b[1] |= (zdt.getMonth().getValue() << 6) | ((zdt.getYear() - 2000) >>> 1);
        b[2] |= ((zdt.getYear() - 2000) << 7) | (zdt.getHour() << 2) | (zdt.getMinute() >>> 4);
        b[3] |= ((zdt.getMinute() << 4)) | (zdt.getSecond() >>> 2);
        b[4] |= (zdt.getSecond() << 6) | (lat >>> 19);
        b[5] |= (lat >>> 11);
        b[6] |= (lat >>> 3);
        b[7] |= (lat << 5) | (lon >> 21);
        b[8] |= (lon >>> 13);
        b[9] |= (lon >>> 5);
        b[10] |= (lon << 3) | (speed >>> 6);
        b[11] |= (speed << 3) | (direction >>> 7);
        b[12] |= (direction << 1) | ((altitude + 1000) >>> 13);
        b[13] |= (altitude + 1000) >>> 5;
        b[14] |= ((altitude + 1000) << 3) | offset >>> 21;
        b[15] |= offset >>> 13;
        b[16] |= offset >>> 5;
        b[17] |= offset << 3 | (ref.ordinal() << 2) | (sat >>> 3);
        b[18] |= (sat << 5) | (gpsState.ordinal() << 4) | (offsetValid.ordinal() << 3) | (accuracy.ordinal() << 2);
        System.out.println((HexBin.encode(b)+"\n"));

        ArrayUtils.reverse(b);
        return b;
    }

    private void validate() {

        int lat = (int) ((latitude + 90D) * 100000D);
        int lon = (int) ((longitude + 180D) * 100000D);

        Validate.notNull(date, "Date is null");
        Validate.notNull(gpsState, "gpsState is null");
        Validate.notNull(offsetValid, "offsetValid is null");
        Validate.notNull(accuracy, "accuracy is null");
        Validate.notNull(ref, "ref is null");
        Validate.notNull(valid, "valid is null");

        Validate.isTrue(Integer.bitCount(lat) <= 25, "Bad latitude %s", latitude);
        Validate.isTrue(Integer.bitCount(lon) <= 26, "Bad longitude %s", longitude);
        Validate.isTrue(speed <= 511, "Bad speed %s max 511", speed);
        Validate.isTrue(direction <= 511, "Bad direction %s max 511", direction);
        Validate.isTrue((altitude + 1000) <= 16383, "Bad altitude %s (+ 1000) max 16383", altitude);
        Validate.isTrue(offset <= 16777215, "Bad offset %s max 16777215", offset);
        Validate.isTrue(sat <= 31, "Bad sat %s max 31", sat);

        if (Integer.bitCount(lat) > 25) {

        }
    }

    public Accuracy getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Accuracy accuracy) {
        this.accuracy = accuracy;
    }

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public GpsState getGpsState() {
        return gpsState;
    }

    public void setGpsState(GpsState gpsState) {
        this.gpsState = gpsState;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public OffsetValid getOffsetValid() {
        return offsetValid;
    }

    public void setOffsetValid(OffsetValid offsetValid) {
        this.offsetValid = offsetValid;
    }

    public Ref getRef() {
        return ref;
    }

    public void setRef(Ref ref) {
        this.ref = ref;
    }

    public int getSat() {
        return sat;
    }

    public void setSat(int sat) {
        this.sat = sat;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Valid getValid() {
        return valid;
    }

    public void setValid(Valid valid) {
        this.valid = valid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GpsNav gpsNav = (GpsNav) o;

        if (Double.compare(gpsNav.latitude, latitude) != 0) return false;
        if (Double.compare(gpsNav.longitude, longitude) != 0) return false;
        if (speed != gpsNav.speed) return false;
        if (direction != gpsNav.direction) return false;
        if (altitude != gpsNav.altitude) return false;
        if (offset != gpsNav.offset) return false;
        if (sat != gpsNav.sat) return false;
        if (valid != gpsNav.valid) return false;
        if (date != null ? !date.equals(gpsNav.date) : gpsNav.date != null) return false;
        if (ref != gpsNav.ref) return false;
        if (gpsState != gpsNav.gpsState) return false;
        if (offsetValid != gpsNav.offsetValid) return false;
        return accuracy == gpsNav.accuracy;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = valid != null ? valid.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + speed;
        result = 31 * result + direction;
        result = 31 * result + altitude;
        result = 31 * result + offset;
        result = 31 * result + (ref != null ? ref.hashCode() : 0);
        result = 31 * result + sat;
        result = 31 * result + (gpsState != null ? gpsState.hashCode() : 0);
        result = 31 * result + (offsetValid != null ? offsetValid.hashCode() : 0);
        result = 31 * result + (accuracy != null ? accuracy.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GpsNav{" +
                "accuracy=" + accuracy +
                ", valid=" + valid +
                ", date=" + date +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", speed=" + speed +
                ", direction=" + direction +
                ", altitude=" + altitude +
                ", offset=" + offset +
                ", ref=" + ref +
                ", sat=" + sat +
                ", gpsState=" + gpsState +
                ", offsetValid=" + offsetValid +
                '}';
    }
}
