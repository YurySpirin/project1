public class RemoteStart {

    public RemoteStart(String onOff, String result){

        int q = Adress.p[61];
        switch (q){
            case 0x00:
                onOff = "Заглушить";
                break;
            case 0xAA:
                onOff = "Завести";
        }

    }
}
