import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import javax.xml.bind.DatatypeConverter;
import java.util.Base64;
import java.util.Scanner;

import static java.lang.Math.pow;
import static java.lang.System.arraycopy;
import static java.lang.System.out;
import static org.apache.commons.lang3.ArrayUtils.reverse;


public class Adress {

    static byte [] p;

    public static void main(String[] args) {


        String s = "21BD14000000D31105A6801BFE3F410100009BB9";
        Scanner in = new Scanner(System.in);
        out.print("Message: ");

        String text = in.nextLine();
        char channel = text.charAt(0);
        text = text.replaceAll("\\s+", "");

        if (channel == '!') {
            byte[] i = Base64.getDecoder().decode((text).substring(1));
            p = new byte[i.length + 1];
            p[0] = ((byte) 0x21);
            arraycopy(i, 0, p, 1, i.length);
            out.println(HexBin.encode(p) + "\n");

        } else if (channel == 'A'| channel == 'a') {
            out.println("TCP");
            p = DatatypeConverter.parseHexBinary(text);
          }
         else {
            out.println("Bad password");
        }

        //Разбираем DIV ищем ид типы и направление сообшения
        int MSG_ID = ((p[7] & 0x0F) << 6) + ((p[6] & 0xFC) >>> 2);
        int MSG_TYPE = (p[6] & 0x03);
        int MSG_VER = ((p[7] & 0xF0) >>> 4);
        out.println("Message_id " + MSG_ID);
        out.println("Message_type " + MSG_TYPE);
        out.println("Message_ver " + MSG_VER);

        //получаем IMEI
        long LSB9 = p[9];
        long LSB10 = p[10];
        long LSB11 = p[11];
        long LSB12 = p[12];
        long LSB13 = p[13];
        long LSB14 = p[14];
        long LSB15 = p[15];
        long LSB16 = p[16];

        long serial;
        serial = (((LSB16 & 0xFF) << 56) + ((LSB15 & 0xFF) << 48) + ((LSB14 & 0xFF) << 40) + ((LSB13 & 0xFF) << 32) + ((LSB12 & 0xFF) << 24) + ((LSB11 & 0xFF) << 16) + ((LSB10 & 0xFF) << 8) + (LSB9 & 0xFF));
        out.println("IMEI " + serial);

        if (MSG_TYPE == 2){
            byte [] payload = new byte[55];
            arraycopy(p,17,payload,0,54);
            System.out.println(HexBin.encode(payload));
        }

        // получаем MCC|MNC
        //int MCC = (p[17]&0xFF)+(p[18]&0xF0);


        int i1 = (p[17] & 0xFF) | ((p[18] & 0xFF) << 8) | ((p[19] & 0xFF) << 16);

        int mcc = i1 >>> 12;
        int mnc = i1 & 0xFFF;
        out.println("MCC " + mcc);
        out.println("MNC " + mnc);


        //разбираем пакет GPRS STAT|GSM STAT|SS

        int SS = (p[24] & 0x1F);
        int GSM_STAT = ((p[24] & 0x60) >>> 5);
        int GPRS = ((p[24] & 0x80) >>> 7);

        out.println("SS " + SS);
        out.println("GSM_STAT " + GSM_STAT);
        out.println("GPRS " + GPRS);


        //Разбираем GPS STAT

        int GPS_State = (p[25] & 0x03);
        int Age_Data = ((p[25] & 0x0C) >>> 2);
        int Satt_Count = ((p[25] & 0xF0) >>> 4);

        out.println("GPS_State " + GPS_State);
        out.println("Age_Data " + Age_Data);
        out.println("Satt_Count " + Satt_Count);

        //Получаем снутр АКБ

        int BACKUP_BATT_VOLTAGE = ((p[27] & 0x3F) << 8) + (p[26] & 0xFF);
        int BACKUP_BATT_STATE = ((p[27] & 0xC0) >>> 6);

        out.println("BACKUP_BATT_VOLTAGE " + BACKUP_BATT_VOLTAGE);
        out.println("BACKUP_BATT_STATE " + BACKUP_BATT_STATE);

        //получаем статус АКБ авто


        int VEH_BATT_VOLTAGE = ((p[29] & 0x3F) << 8) + (p[28] & 0xFF);
        int VEH_BATT_STATE = ((p[29] & 0xC0) >>> 6);

        out.println("VEH_BATT_VOLTAGE " + VEH_BATT_VOLTAGE);
        out.println("VEH_BATT_STATE " + VEH_BATT_STATE);


        //получаем данный метки

        out.println(String.format("%02X", p[34], p[33], p[32], p[31], p[30]));
        int DID_SOURCE = (p[35] & 0x01);
        int DID_GRANTED = ((p[35] & 0x06) >>> 1);
        int MOBILISE = ((p[35] & 0x08) >>> 3);
        int CAR_ARM_ST = ((p[35] & 0x10) >>> 4);
        int DID_ID_T1 = ((p[35] & 0x20) >>> 5);
        int DID_ID_T2 = ((p[35] & 0x40) >>> 6);
        int TRANSP_PANIC = ((p[35] & 0x80) >>> 7);

        out.println("DID_SOURCE " + DID_SOURCE);
        out.println("DID_GRANTED " + DID_GRANTED);
        out.println("MOBILISE " + MOBILISE);
        out.println("CAR_ARM_ST " + CAR_ARM_ST);
        out.println("DID_ID_T1 " + DID_ID_T1);
        out.println("DID_ID_T2 " + DID_ID_T2);
        out.println("TRANSP_PANIC " + TRANSP_PANIC);


        //TEMP

        int temp = (((p[37] & 0xFF) << 8) + (p[36] & 0xFF) - 0x0080);
        out.println("TEMP " + temp);


        //получаем дату и время

        out.println("UTC TIME " + (p[40] + 3) + ":" + p[39] + ":" + p[38]);
        out.println("UTC DATE " + p[41] + "." + p[42] + "." + "20" + p[43]);


        //FW version
        int fw1 = p[48] & 0xFF;
        int fw2 = p[49] & 0xFF;

        out.println("FW_VER " + fw1 + "." + fw2);

        //Параметры авторизации

        int id_type1 = p[50] & 0xFF;
        int id_type2 = p[51] & 0xFF;


        out.println("TYPE_AUTH1 " + id_type1);
        out.println("TYPE_AUTH2 " + id_type2);

        //CAN

        int can_ver = (p[52] & 0xFF) + ((p[53] & 0xFF) << 8);
        out.println("CAN " + can_ver);


        //HW Version

        int hw_ver = (p[54] & 0xFF);
        out.println("HW Ver. " + hw_ver);


        // Status UNIT

        int unit_mode = (p[55] & 0x03);
        switch (unit_mode) {
            case 0:
                out.println("Функциональный");
                break;
            case 1:
                out.println("Транспортный");
                break;
            case 2:
            case 3:
                out.println("RESERVED");
                break;
            default:
                out.println("Bad Password");
        }

        int spec_unit_mode = ((p[55] & 0x1C) >>> 2);
        switch (spec_unit_mode) {
            case 0:
                out.println("ARM_MODE");
                break;
            case 1:
                out.println("SERVICE");
                break;
            case 2:
                out.println("RESERVE");
                break;
            case 3:
                out.println("SUPER_ARM_MODE");
                break;
            case 4:
            case 5:
            case 6:
            case 7:
                out.println("RESERVED");
                break;
            default:
                out.println("Bad Password");
        }

        int acc = ((p[55] & 0x20) >>> 5);
        switch (acc) {
            case 0:
                out.println("ACC Не подключен");
                break;
            case 1:
                out.println("ACC Подключен");
                break;
        }

        int wire_cut = ((p[55] & 0xC0) >>> 6);
        switch (wire_cut) {
            case 0:
                out.println("Нет запрограммированных блокировок или нет информации");
                break;
            case 1:
                out.println("– Есть запрограммированные Б/П блокировки, но как минимум 1 не контролируется ");
                break;
            case 2:
                out.println("Все запрограммированные блокировки контролируются");
                break;
            case 3:
                out.println("RESERVE");
                break;
        }

        int wire_cut_mode = (p[56] & 0x01);
        switch (wire_cut_mode) {
            case 0:
                out.println("Упрощенный режим");
                break;
            case 1:
                out.println("Полнофункциональны режим");
                break;
        }

        int cut_mode = ((p[56] & 0x02) >>> 1);
        switch (cut_mode) {
            case 0:
                out.println("Нормально открытая NO");
                break;
            case 1:
                out.println("Нормально закрытая NC");
                break;
        }

        int can_stat = ((p[56] & 0x0C) >>> 2);
        switch (can_stat) {
            case 0:
                out.println("не привязан или значение недоступно");
                break;
            case 1:
                out.println("Привязан, но не идентифицирован ");
                break;
            case 2:
                out.println("Привязан и идентифицирован ");
                break;
            default:
                out.println("Bad password");
        }

        int ignition = ((p[56] & 0x10) >>> 4);
        switch (ignition) {
            case 0:
                out.println("Зажигание выключено или значение недоступно ");
                break;
            case 1:
                out.println("Зажигание включено");
                break;
        }

        int crash = ((p[56] & 0x20) >>> 5);
        switch (crash) {
            case 0:
                out.println("CRASH Не активен или значение недоступно");
                break;
            case 1:
                out.println("CRASH Активен");
                break;
        }
        int add_inp1 = ((p[56] & 0x40) >>> 6);
        switch (add_inp1) {
            case 0:
                out.println("ADD_INP1_Не активен или значение недоступно ");
                break;
            case 1:
                out.println("ADD_INP1_Активен");
                break;
        }

        int add_inp2 = ((p[56] & 0x80) >>> 7);
        switch (add_inp2) {
            case 0:
                out.println("ADD_INP2_Не активен или значение недоступно");
                break;
            case 1:
                out.println("ADD_INP2_Активен");
                break;
        }

        int lock_doors_out = ((p[57] & 0x01));
        switch (lock_doors_out) {
            case 0:
                out.println("LOCK_DOORS_Выход не активен или состояние недоступно ");
                break;
            case 1:
                out.println("LOCK_DOORS_Выход активен");
                break;
        }

        int bonnet = ((p[57] & 0x02) >>> 1);
        switch (bonnet) {
            case 0:
                out.println("BONNET_Выход не активен или состояние недоступно ");
                break;
            case 1:
                out.println("BONNET_Активен");
                break;
        }

        int unlock_doors_out = ((p[57] & 0x04) >>> 2);
        switch (unlock_doors_out) {
            case 0:
                out.println("UNLOCL_DOORS_Выход не активен или состояние недоступно ");
                break;
            case 1:
                out.println("UNLOCL_DOORS_Выход активен");
                break;
        }

        int panic_button = ((p[57] & 0x08) >>> 3);
        switch (panic_button) {
            case 0:
                out.println("PANNIC_Не нажата или состояние недоступно ");
                break;
            case 1:
                out.println("PANNIC_Нажата");
                break;
        }

        int true_button = ((p[57] & 0x10) >>> 4);
        switch (true_button) {
            case 0:
                out.println("TRUE_Не нажата или состояние недоступно ");
                break;
            case 1:
                out.println("TRUE_Нажата");
                break;
        }

        int driver_door = ((p[57] & 0x20) >>> 5);
        switch (driver_door) {
            case 0:
                out.println("DRIVER_DOOR_Закрыта или состояние недоступно ");
                break;
            case 1:
                out.println("DRIVER_DOOR_Открыта");
                break;
        }

        int all_doors = ((p[57] & 0x40) >>> 6);
        switch (all_doors) {
            case 0:
                out.println("ALL_DOORS_Закрыты или состояние недоступно ");
                break;
            case 1:
                out.println("ALL_DOORS_Открыты");
                break;
        }

        int rem_start_inp = ((p[57] & 0x80) >>> 7);
        switch (rem_start_inp) {
            case 0:
                out.println("REM_START_IN_Не активен или состояние недоступно ");
                break;
            case 1:
                out.println("REM_START_IN_Активен");
                break;
        }

        int sign_disarm = (p[58] & 0x01);
        switch (sign_disarm) {
            case 0:
                out.println("SIGN_Не активен или состояние недоступно ");
                break;
            case 1:
                out.println("SIGN_Активен");
                break;
        }

        int rem_start_out = ((p[58] & 0x02) >>> 1);
        switch (rem_start_out) {
            case 0:
                out.println("REM_START_OUT_Выход активен или состояние недоступно ");
                break;
            case 1:
                out.println("REM_START_OUT_Выход не активен");
                break;
        }

        int trunk = ((p[58] & 0x04) >>> 2);
        switch (trunk) {
            case 0:
                out.println("TRUNK_Не активен или состояние недоступно ");
                break;
            case 1:
                out.println("TRUNK_Активен");
                break;
        }

        int buz_out = ((p[58] & 0x08) >>> 3);
        switch (buz_out) {
            case 0:
                out.println("BUZ_OUT_Не активен или состояние недоступно ");
                break;
            case 1:
                out.println("BUZ_OUT_Активен");
                break;
        }

        int brake_inp = ((p[58] & 0x10) >>> 4);
        switch (brake_inp) {
            case 0:
                out.println("BRAKE_IN_Не нажат или состояние недоступно ");
                break;
            case 1:
                out.println("BRAKE_IN_Нажат");
                break;
        }

        int serv_speed_out = ((p[58] & 0x20) >>> 5);
        switch (serv_speed_out) {
            case 1:
                out.println("блок выходит из сервисного режима по скорости");
                break;
            case 0:
                out.println("блок никогда не выходит из сервисного режима по скорости");
                break;
        }

        int siren_out = ((p[58] & 0x40) >>> 6);
        switch (siren_out) {
            case 0:
                out.println("Выход на сирену не активен");
                break;
            case 1:
                out.println("Выход на сирену активен");
                break;
        }

        int exrt_panic = ((p[58] & 0x80) >>> 7);
        switch (exrt_panic) {
            case 0:
                out.println("EXTR_PANIC_Выключено");
                break;
            case 1:
                out.println("EXTR_PANIC_Включено");
                break;
        }




        /*out.println(unit_mode);
        out.println(spec_unit_mode);
        out.println(acc);
        out.println(wire_cut);
        out.println(wire_cut_mode);
        out.println(cut_mode);
        out.println(can_stat);
        out.println(ignition);
        out.println(crash);
        out.println(add_inp1);
        out.println(add_inp2);
        out.println(lock_doors_out);
        out.println(bonnet);
        out.println(unlock_doors_out);
        out.println(panic_button);
        out.println(true_button);
        out.println(driver_door);
        out.println(all_doors);
        out.println(rem_start_inp);
        out.println(sign_disarm);
        out.println(rem_start_out);
        out.println(trunk);
        out.println(buz_out);
        out.println(brake_inp);
        out.println(serv_speed_out);
        out.println(siren_out);
        out.println(exrt_panic);
        */


        //Уровень топлива
        //2F434553415230372F2C312E62696E2C667470726561642C313233343546722100
            /*int fuel = ((p[59] & 0xFF)) / 10;
            out.println("Топлива в баке " + fuel);
            byte[] arr1 = new byte[]{0x2F, 0x43, 0x45, 0x30, 0x53, 0x14, 0x31, 0x30, 0x30, 0x36, 0x35, 0x30, 0x33, 0x36, 0x39, 0x30, 0x30, 0x34, 0x33, 0x37, 0x21};
            String isid = new String(arr1);
            out.println(isid);

            String iccid;
            iccid = "8940011706474260344";
            byte[] arr2 = iccid.getBytes();
            out.printf(HexBin.encode(arr2) + "\n");*/


        switch (MSG_ID) {

            case 601:
                int onOf = (p[61] & 0xFF);
                switch (onOf) {
                    case 0x00:
                        out.println("Заглушить");
                        break;
                    case 0xAA:
                        out.println("Завести");
                }
                int result = p[62] & 0xFF;
                switch (result) {
                    case 0x00:
                        out.println("Успешно (Команда выполнена)");
                        break;
                    case 0xB3:
                        out.println("Автозапуск запрещён в данном режиме блока");
                        break;
                    case 0x3B:
                        out.println("Ошибка удалённого запуска, двигатель уже запущен");
                        break;
                    case 0x3C:
                        out.println("Ошибка удалённого глушения, двигатель уже заглушен");
                        break;
                    case 0xF1:
                        out.println("Сообщение устарело");
                        break;
                    case 0x1F:
                        out.println("Устройство не настроено для работы с автозапуском");
                }
                break;

            case 811:
                out.println("Запрос местоположения");
                int lockStatus = p[63] & 0xFF;
                switch (lockStatus) {
                    case 0x00:
                        out.println("Lock Status Inactiv");
                        break;
                    case 0x01:
                        out.println("Lock Status Reserved");
                        break;
                    case 0x02:
                        out.println("Lock Status 2D fix acquired");
                        break;
                    case 0x03:
                        out.println("Lock Status 3D fix acquired");
                }
                double lat1 = p[64] & 0xFF;
                double lat2 = p[65] & 0xFF;
                double lat3 = p[66] & 0xFF;
                double lat4 = p[67] & 0xFF;
                double latitude = ((((lat1) + (lat2 * pow(2, 8)) + (lat3 * pow(2, 16)) + (lat4 * pow(2, 24))) / 23860929d) - 90);
                out.println("Широта " + latitude);

                double lon1 = p[68] & 0xFF;
                double lon2 = p[69] & 0xFF;
                double lon3 = p[70] & 0xFF;
                double lon4 = p[71] & 0xFF;
                double longitude = ((((lon1) + (lon2 * pow(2, 8)) + (lon3 * pow(2, 16)) + (lon4 * pow(2, 24))) / 11930464d) - 180);
                out.println("Долгота " + longitude);

                int headling = (p[72] & 0xFF) + ((p[73] & 0xFF) << 8);
                out.println("Направление " + headling);

                int speed = (p[74] & 0xFF);
                out.println("Скорость " + speed);

                out.println("UTC TIME " + (p[77] + 3) + ":" + p[76] + ":" + p[75]);
                out.println("UTC DATE " + p[78] + "." + p[79] + "." + "20" + p[80]);

                int hDop = p[81] & 0xFF;
                int satCount = p[82] & 0xFF;
                out.println("Количество видимых спутников " + satCount);
                break;

            case 801:
                out.println("Обновить координаты GPS");
                int lockStatus2 = p[63] & 0xFF;
                switch (lockStatus2) {
                    case 0x00:
                        out.println("Lock Status Inactiv");
                        break;
                    case 0x01:
                        out.println("Lock Status Reserved");
                        break;
                    case 0x02:
                        out.println("Lock Status 2D fix acquired");
                        break;
                    case 0x03:
                        out.println("Lock Status 3D fix acquired");
                }
                double lat11 = p[64] & 0xFF;
                double lat12 = p[65] & 0xFF;
                double lat13 = p[66] & 0xFF;
                double lat14 = p[67] & 0xFF;
                double latitude1 = ((((lat11) + (lat12 * pow(2, 8)) + (lat13 * pow(2, 16)) + (lat14 * pow(2, 24))) / 23860929d) - 90);
                out.println("Широта " + latitude1);

                double lon11 = p[68] & 0xFF;
                double lon12 = p[69] & 0xFF;
                double lon13 = p[70] & 0xFF;
                double lon14 = p[71] & 0xFF;
                double longitude1 = ((((lon11) + (lon12 * pow(2, 8)) + (lon13 * pow(2, 16)) + (lon14 * pow(2, 24))) / 11930464d) - 180);
                out.println("Долгота " + longitude1);

                int headling1 = (p[72] & 0xFF) + ((p[73] & 0xFF) << 8);
                out.println("Направление " + headling1);

                int speed1 = (p[74] & 0xFF);
                out.println("Скорость " + speed1);

                out.println("UTC TIME " + (p[77] + 3) + ":" + p[76] + ":" + p[75]);
                out.println("UTC DATE " + p[78] + "." + p[79] + "." + "20" + p[80]);

                int hDop1 = p[81] & 0xFF;
                out.println("hDop = " + hDop1);

                int satCount1 = p[82] & 0xFF;
                out.println("Количество видимых спутников " + satCount1);
                break;

            case 802:

                out.println("Запрос кодов меток");
                int totalTaq = p[61] & 0xFF;
                out.print("Запраграммировано " + totalTaq + " меток" + "\n");

                byte[] taq1 = new byte[5];
                arraycopy(p, 62, taq1, 0, 5);
                reverse(taq1);
                out.println(HexBin.encode(taq1) + "\n");
                int taq1Status = p[67] & 0xFF;
                if (taq1Status == 0x55) {
                    out.println("Метка " + HexBin.encode(taq1) + " в зоне");
                } else if (taq1Status == 0x00) {
                    out.println("Метка " + HexBin.encode(taq1) + " вне зоны");
                } else {
                    out.println("Bad params");
                }


                byte[] taq2 = new byte[5];
                arraycopy(p, 68, taq2, 0, 5);
                reverse(taq2);
                out.println(HexBin.encode(taq2));

                int taq2Status = p[73] & 0xFF;
                if (taq2Status == 0x55) {
                    out.println("Метка " + HexBin.encode(taq2) + " в зоне");
                } else if (taq2Status == 0x00) {
                    out.println("Метка " + HexBin.encode(taq2) + " вне зоны");
                } else {
                    out.println("Bad params");
                }

                byte[] taq3 = new byte[5];
                arraycopy(p, 74, taq3, 0, 5);
                reverse(taq3);
                out.println(HexBin.encode(taq3));

                int taq3Status = p[79] & 0xFF;
                if (taq3Status == 0x55) {
                    out.println("Метка " + HexBin.encode(taq3) + " в зоне");
                } else if (taq3Status == 0x00) {
                    out.println("Метка " + HexBin.encode(taq3) + " вне зоны");
                } else {
                    out.println("Bad params");
                }
                break;

            case 807:

                out.println("Запрос кодом беспроводных реле");
                int totalRelay = p[61] & 0xFF;
                out.print("Запраграммировано " + totalRelay + " меток" + "\n");

                byte[] relay1 = new byte[5];
                arraycopy(p, 62, relay1, 0, 5);
                reverse(relay1);
                out.println(HexBin.encode(relay1));
                int relay1Status = p[67] & 0xFF;
                if (relay1Status == 0x55) {
                    out.println("Блокировка " + HexBin.encode(relay1) + " в зоне");
                } else if (relay1Status == 0x00) {
                    out.println("Блокировка " + HexBin.encode(relay1) + " вне зоны");
                } else {
                    out.println("Bad params");
                }


                byte[] relay2 = new byte[5];
                arraycopy(p, 68, relay2, 0, 5);
                reverse(relay2);
                out.println(HexBin.encode(relay2));
                int relay2Status = p[73] & 0xFF;
                if (relay2Status == 0x55) {
                    out.println("Блокировка " + HexBin.encode(relay2) + " в зоне");
                } else if (relay2Status == 0x00) {
                    out.println("Блокировка " + HexBin.encode(relay2) + " вне зоны");
                } else {
                    out.println("Bad params");
                }


                byte[] relay3 = new byte[5];
                arraycopy(p, 74, relay3, 0, 5);
                reverse(relay3);
                out.println(HexBin.encode(relay3) + "\n");
                int relay3Status = p[79] & 0xFF;
                if (relay3Status == 0x55) {
                    out.println("Блокировка " + HexBin.encode(relay3) + " в зоне");
                } else if (relay3Status == 0x00) {
                    out.println("Блокировка " + HexBin.encode(relay3) + " вне зоны");
                } else {
                    out.println("Bad params");
                }
                break;

            case 605:

                out.println("Ответ на команду открытия/закрытия дверей");
                switch (p[61] & 0xFF) {
                    case 0x00:
                        System.out.println("Считать");
                        break;
                    case 0x01:
                        System.out.println("Настроить");
                        break;
                    default:
                        System.out.println("Bad Params");
                        break;
                }

                int lockUnlockResult = p[62] & 0xFF;

                if (lockUnlockResult == 0x00) {
                    out.println("выполнена успешно");
                } else if (lockUnlockResult == 0xF1) {
                    out.println("не выполнена (команда устарела)");
                } else {
                    out.println("Bad params");
                }
                break;

            case 806:

                out.println("Find me");

                switch (p[61] & 0xFF) {
                    case 0x00:
                        System.out.println("Считать");
                        break;
                    case 0x01:
                        System.out.println("Настроить");
                        break;
                    default:
                        System.out.println("Bad Params");
                        break;
                }

                int findMeResult = p[62] & 0xFF;
                if (findMeResult == 0x00) {
                    out.println("выполнена успешно");
                } else if (findMeResult == 0xF1) {
                    out.println("не выполнена (сообщение устарело)");
                } else {
                    out.println("Bad params");
                }
                break;

            case 401:
                out.println("Настройка порогов АКБ");
                switch (p[61] & 0xFF) {
                    case 0x00:
                        System.out.println("Считать");
                        break;
                    case 0x01:
                        System.out.println("Настроить");
                        break;
                    default:
                        System.out.println("Bad Params");
                        break;
                }

                int battResult = p[62] & 0xFF;
                if (battResult == 0x00) {
                    out.println("успешно");
                } else {
                    out.println("не выполнена");
                }

                double vehBatVolt = (p[63] & 0xFF) / 10.0;
                out.println("АКБ Автомобиля разряжен: " + vehBatVolt + "B");
                double vehBatStat = (p[64] & 0xFF) / 10.0;
                out.println("АКБ Автомобиля отключен: " + vehBatStat + "B");
                double backBatVolt = (p[65] & 0xFF) / 10.0;
                out.println("АКБ Блока разряжен: " + backBatVolt + "B");
                double backBatStat = (p[66] & 0xFF) / 10.0;
                out.println("АКБ Блока отключен: " + backBatStat + "B");
                break;

            case 402:
                out.println("Настройка мощности метки");
                switch (p[61] & 0xFF) {
                    case 0x00:
                        System.out.println("Считать");
                        break;
                    case 0x01:
                        System.out.println("Настроить");
                        break;
                    default:
                        System.out.println("Bad Params");
                        break;
                }
                int taqPowReuslt = p[62] & 0xFF;
                if (taqPowReuslt == 0x00) {
                    out.println("успешно");
                } else {
                    out.println("не выполнено");
                }
                int taqPower = p[63] & 0xFF;
                out.println("Мощность метки " + taqPower);
                break;

            case 300:
                out.println("Параметры сервера");
                switch (p[61] & 0xFF) {
                    case 0x00:
                        System.out.println("Считать");
                        break;
                    case 0x01:
                        System.out.println("Настроить");
                        break;
                    default:
                        System.out.println("Bad Params");
                        break;
                }
                int serverResult = p[62] & 0xFF;
                if (serverResult == 0x00) {
                    out.println("успешно");
                } else {
                    out.println("не выполнено");
                }
                int connectType = p[63] & 0xFF;
                byte[] serverInfo = new byte[packetLenght - (17 + 44 + 5)];
                arraycopy(p, 64, serverInfo, 0, packetLenght - 17 - 44 - 5);
                String servInfo = new String(serverInfo);
                out.println("Параматры сервера: " + servInfo);
                break;

            case 425:
                out.println("Параметры APN");
                switch (p[61] & 0xFF) {
                    case 0x00:
                        System.out.println("Считать");
                        break;
                    case 0x01:
                        System.out.println("Настроить");
                        break;
                    default:
                        System.out.println("Bad Params");
                        break;
                }
                int apnResult = p[62] & 0xFF;
                if (apnResult == 0x00) {
                    out.println("успешно");
                } else if (apnResult == 0xAA) {
                    out.println("не выполнено");
                } else {
                    System.out.println("Bad params");
                }

                byte[] apn = new byte[packetLenght - (17 + 44 + 4)];
                arraycopy(p, 63, apn, 0, packetLenght - 17 - 44 - 4);
                String apnInfo = new String(apn);
                out.println("Параматры сервера: " + apnInfo);
                break;

            case 417:
                out.println("Параметры отчетного сообщения");
                switch (p[61] & 0xFF) {
                    case 0x00:
                        System.out.println("Считать");
                        break;
                    case 0x01:
                        System.out.println("Настроить");
                        break;
                    default:
                        System.out.println("Bad Params");
                        break;
                }
                int heartbeatResult = p[62] & 0xFF;
                if (heartbeatResult == 0x00) {
                    out.println("успешно");
                } else {
                    out.println("не выполнено");
                }
                int heartbeat = p[63] & 0xFF;
                System.out.println("Период отчетного сообщения: " + heartbeat + " дней");
                break;

            case 406:
                System.out.println("Режим работы блока");
                switch (p[61] & 0xFF) {
                    case 0x00:
                        System.out.println("Считать");
                        break;
                    case 0x01:
                        System.out.println("Настроить");
                        break;
                    default:
                        System.out.println("Bad Params");
                        break;
                }
                int unitModeResult = p[62] & 0xFF;
                if (unitModeResult == 0x00) {
                    out.println("успешно");
                } else {
                    out.println("не выполнено");
                }
                int unitMode = p[63] & 0xFF;
                switch (unitMode) {
                    case 0x69:
                        System.out.println("Функциональный");
                        break;
                    case 0x96:
                        System.out.println("Транспортный");
                        break;
                }
                break;

            case 408:
                System.out.println("Настройка подрежима");
                switch (p[61] & 0xFF) {
                    case 0x00:
                        System.out.println("Считать");
                        break;
                    case 0x01:
                        System.out.println("Настроить");
                        break;
                    default:
                        System.out.println("Bad Params");
                        break;
                }
                int subModeResult = p[62] & 0xFF;
                if (subModeResult == 0x00) {
                    out.println("успешно");
                } else {
                    out.println("не выполнено");
                }
                int subMode = p[63] & 0xFF;
                switch (subMode) {
                    case 0x3B:
                        System.out.println("Режим охрана");
                        break;
                    case 0xF0:
                        System.out.println("Режим суперохрана");
                        break;
                    case 0xB3:
                        System.out.println("Сервисный режим");
                        break;
                }
                break;

            case 432:
                System.out.println("Полянрности входов");
                switch (p[61] & 0xFF) {
                    case 0x00:
                        System.out.println("Считать");
                        break;
                    case 0x01:
                        System.out.println("Настроить");
                        break;
                    default:
                        System.out.println("Bad Params");
                        break;
                }
                int inputPolarResult = p[62] & 0xFF;
                if (inputPolarResult == 0x00) {
                    out.println("успешно");
                } else {
                    out.println("не выполнено");
                }
                byte[] inputPolar = new byte[6];
                arraycopy(p, 63, inputPolar, 0, inputPolar.length);

                switch (inputPolar[0] & 0xFF) {
                    case 0x00:
                        System.out.println("Все двери - отрицательная");
                        break;
                    case 0xCC:
                        System.out.println("Все двери - положительная");
                        break;
                    default:
                        System.out.println("Bad Params");
                        break;
                }
                switch (inputPolar[1] & 0xFF) {
                    case 0x00:
                        System.out.println("Подтверждение запуска - отрицательная");
                        break;
                    case 0xCC:
                        System.out.println("Подтверждение запуска - положительная");
                        break;
                    default:
                        System.out.println("Bad Params");
                        break;
                }
                switch (inputPolar[2] & 0xFF) {
                    case 0x00:
                        System.out.println("Внешнее тревожное устройство - отрицательная");
                        break;
                    case 0xCC:
                        System.out.println("Внешнее тревожное устройство - положительная");
                        break;
                    default:
                        System.out.println("Bad Params");
                        break;
                }
                switch (inputPolar[3] & 0xFF) {
                    case 0x00:
                        System.out.println("Дополнительный вход - отрицательная");
                        break;
                    case 0xCC:
                        System.out.println("Дополнительный вход - положительная");
                        break;
                    default:
                        System.out.println("Bad Params");
                        break;
                }
                switch (inputPolar[4] & 0xFF) {
                    case 0x00:
                        System.out.println("Паника - отрицательная");
                        break;
                    case 0xCC:
                        System.out.println("Паника - положительная");
                        break;
                    default:
                        System.out.println("Bad Params");
                        break;
                }
                switch (inputPolar[5] & 0xFF) {
                    case 0x00:
                        System.out.println("Истина - отрицательная");
                        break;
                    case 0xCC:
                        System.out.println("Истина - положительная");
                        break;
                    default:
                        System.out.println("Bad Params");
                        break;
                }
                break;

            case 441:
                System.out.println("Полярность проводной блокировки");
                switch (p[61] & 0xFF) {
                    case 0x00:
                        System.out.println("Считать");
                        break;
                    case 0x01:
                        System.out.println("Настроить");
                        break;
                    default:
                        System.out.println("Bad Params");
                        break;
                }
                int wiredCutResult = p[62] & 0xFF;
                if (wiredCutResult == 0x00) {
                    out.println("успешно");
                } else {
                    out.println("не выполнено");
                }
                switch (p[63] & 0xFF) {
                    case 0x00:
                        System.out.println("Нормально открытая");
                        break;
                    case 0xCC:
                        System.out.println("Нормально закрытая");
                        break;
                    default:
                        System.out.println("Bad Params");
                        break;
                }
                break;
            case 433:
                System.out.println("Вторжение по дверям");
                switch (p[61] & 0xFF) {
                    case 0x00:
                        System.out.println("Считать");
                        break;
                    case 0x01:
                        System.out.println("Настроить");
                        break;
                    default:
                        System.out.println("Bad Params");
                        break;
                }
                int doorIntrusionResult = p[62] & 0xFF;
                if (doorIntrusionResult == 0x00) {
                    out.println("успешно");
                } else {
                    out.println("не выполнено");
                }
                switch (p[63] & 0xFF) {
                    case 0x00:
                        System.out.println("Выключено");
                        break;
                    case 0xAA:
                        System.out.println("Включено");
                        break;
                    default:
                        System.out.println("Bad Params");
                        break;
                }
                break;

            case 434:
                System.out.println("Вторжение по зажиганию");
                switch (p[61] & 0xFF) {
                    case 0x00:
                        System.out.println("Считать");
                        break;
                    case 0x01:
                        System.out.println("Настроить");
                        break;
                    default:
                        System.out.println("Bad Params");
                        break;
                }
                int ignIntrusionResult = p[62] & 0xFF;
                if (ignIntrusionResult == 0x00) {
                    out.println("успешно");
                } else {
                    out.println("не выполнено");
                }
                switch (p[63] & 0xFF) {
                    case 0x00:
                        System.out.println("Выключено");
                        break;
                    case 0xAA:
                        System.out.println("Включено");
                        break;
                    default:
                        System.out.println("Bad Params");
                        break;
                }
                break;

            case 435:
                System.out.println("Вторжение по багажнику");
                switch (p[61] & 0xFF) {
                    case 0x00:
                        System.out.println("Считать");
                        break;
                    case 0x01:
                        System.out.println("Настроить");
                        break;
                    default:
                        System.out.println("Bad Params");
                        break;
                }
                int trunkIntrusionResult = p[62] & 0xFF;
                if (trunkIntrusionResult == 0x00) {
                    out.println("успешно");
                } else {
                    out.println("не выполнено");
                }
                switch (p[63] & 0xFF) {
                    case 0x00:
                        System.out.println("Выключено");
                        break;
                    case 0xAA:
                        System.out.println("Включено");
                        break;
                    default:
                        System.out.println("Bad Params");
                        break;
                }
                break;

            case 452:
                System.out.println("Вторжение по капоту");
                switch (p[61] & 0xFF) {
                    case 0x00:
                        System.out.println("Считать");
                        break;
                    case 0x01:
                        System.out.println("Настроить");
                        break;
                    default:
                        System.out.println("Bad Params");
                        break;
                }
                int bonnetIntrusionResult = p[62] & 0xFF;
                if (bonnetIntrusionResult == 0x00) {
                    out.println("успешно");
                } else {
                    out.println("не выполнено");
                }
                switch (p[63] & 0xFF) {
                    case 0x00:
                        System.out.println("Выключено");
                        break;
                    case 0xAA:
                        System.out.println("Включено");
                        break;
                    default:
                        System.out.println("Bad Params");
                        break;
                }
                break;

            case 461:
                System.out.println("Настройка входа антиджаммер");
                switch (p[61] & 0xFF) {
                    case 0x00:
                        System.out.println("Считать");
                        break;
                    case 0x01:
                        System.out.println("Настроить");
                        break;
                    default:
                        System.out.println("Bad Params");
                        break;
                }
                int hoodIntrusResult = p[62] & 0xFF;
                if (hoodIntrusResult == 0x00) {
                    out.println("успешно");
                } else {
                    out.println("не выполнено");
                }
                switch (p[63] & 0xFF) {
                    case 0x00:
                        System.out.println("Выключено");
                        break;
                    case 0xAA:
                        System.out.println("Включено");
                        break;
                    default:
                        System.out.println("Bad Params");
                        break;
                }
                break;

            case 454:
                System.out.println("Настройка таймера вторжения");
                switch (p[61] & 0xFF) {
                    case 0x00:
                        System.out.println("Считать");
                        break;
                    case 0x01:
                        System.out.println("Настроить");
                        break;
                    default:
                        System.out.println("Bad Params");
                        break;
                }
                int timerResult = p[62] & 0xFF;
                if (timerResult == 0x00) {
                    out.println("успешно");
                } else {
                    out.println("не выполнено");
                }
                int timer = p[63] & 0xFF;
                System.out.println("Таймер заадержки вторжения" + timer + "сек");
                break;

            case 453:
                System.out.println("Настройка задержки перехода в охрану");
                switch (p[61] & 0xFF) {
                    case 0x00:
                        System.out.println("Считать");
                        break;
                    case 0x01:
                        System.out.println("Настроить");
                        break;
                    default:
                        System.out.println("Bad Params");
                        break;
                }
                int armDelayResult = p[62] & 0xFF;
                if (armDelayResult == 0x00) {
                    out.println("успешно");
                } else {
                    out.println("не выполнено");
                }
                int armDelay = p[63] & 0xFF;
                System.out.println("Задержка перехода в охрану " + armDelay + "сек");
                break;

            case 456:
                System.out.println("");
                switch (p[61] & 0xFF) {
                    case 0x00:
                        System.out.println("Считать");
                        break;
                    case 0x01:
                        System.out.println("Настроить");
                        break;
                    default:
                        System.out.println("Bad Params");
                        break;
                }
                int canInputResult = p[62] & 0xFF;
                if (canInputResult == 0x00) {
                    out.println("успешно");
                } else {
                    out.println("не выполнено");
                }
                byte[] canInput = new byte[8];

                System.out.print("Все двери - ");
                switch (canInput[0]) {
                    case 0x00:
                        System.out.println("Отключено");
                        break;
                    case 0x01:
                        System.out.println("CAN");
                        break;
                    case 0x02:
                        System.out.println("Физический вход");
                        break;
                    case 0x03:
                        System.out.println("CAN + Физический вход");
                        break;
                    default:
                        System.out.println("Bad Params");
                        break;
                }
                System.out.print("Дверь водителя - ");
                switch (canInput[1]) {
                    case 0x00:
                        System.out.println("Отключено");
                        break;
                    case 0x01:
                        System.out.println("CAN");
                        break;
                    default:
                        System.out.println("Bad params");
                        break;
                }
                System.out.print("Зажигание - ");
                switch (canInput[2]) {
                    case 0x00:
                        System.out.println("Отключено");
                        break;
                    case 0x01:
                        System.out.println("CAN");
                        break;
                    case 0x02:
                        System.out.println("Физический вход");
                        break;
                    case 0x03:
                        System.out.println("CAN + Физический вход");
                        break;
                    default:
                        System.out.println("Bad params");
                        break;
                }
                System.out.print("");
                switch (canInput[3]) {
                    case 0x00:
                        System.out.println("");
                        break;
                    case 0x01:
                        System.out.println("");
                        break;
                    case 0x02:
                        System.out.println("");
                        break;
                    default:
                        System.out.println("");
                        break;
                }
                System.out.println("");


            case 814:
                /*out.println("Параметры APN");
                switch (p[61] & 0xFF){
                    case 0x00:
                        System.out.println("Считать");
                        break;
                    case 0x01:
                        System.out.println("Настроить");
                        break;
                    default:
                        System.out.println("Bad Params");
                        break;
                }
                int apnResult = p[61] & 0xFF;
                if (apnResult == 0x00){
                    out.println("успешно");
                }
                else if (apnResult == 0xAA){
                    out.println("не выполнено");
                }
                else {
                    System.out.println("Bad params");
                }*/

                byte[] updateCan = new byte[packetLenght - (17 + 8)];
                arraycopy(p, 24, updateCan, 0, packetLenght - 17 - 8);
                String servCanInfo = new String(updateCan);
                out.println("Параматры сервера: " + servCanInfo);
                break;


        }

        /*if (MSG_ID == 811){
            System.out.println("GPS Position");
            int locQueRes = p[61];
            int locTypeParam = p[62];
            int loscStatus = p[63];
            double lat1 = p[64] & 0xFF;
            double lat2 = p[65] & 0xFF;
            double lat3 = p[66] & 0xFF;
            double lat4 = p[67] & 0xFF;

            double latitude = ((lat1 + (lat2 * (pow (2,8))) + (lat3 * pow (2,16)) + (lat4 * (pow (2,24)))) /23860929.0)-90;
            out.println("LAT " + latitude);
            //out.printf(HexBin.encode(new byte[]{p[67]})+"\n");


            double lon1 = p[68] & 0xFF;
            double lon2 = p[69] & 0xFF;
            double lon3 = p[70] & 0xFF;
            double lon4 = p[71] & 0xFF;

            double longitude = ((lon1 + (lon2 * (pow (2,8))) + (lon3 * pow (2,16)) + (lon4 * (pow (2,24)))) /11930464.0)-180;
            out.println("LON " + longitude);

            int headling = (p[72] & 0xFF) + ((p[0x73] & 0xFF) << 8);

            int speed = p[74];


        }*/

        //switch ()













        /*if (MSG_ID == 802) {
            TagAsk.tags(TagAsk.id, TagAsk.tagSum);
            System.out.println(TagAsk.tagSum);
            System.out.println(TagAsk.id);
        }*/

    }
}
