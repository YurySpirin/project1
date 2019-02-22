public class Rectangle {
    public static void main(String[] args) {
        String line = "b53b3a3d6ab90ce0268229151c9bde11";
        String[] SS = new String[line.length() / 2];
        int k = 0;  //счетчик для индексов  массива
        for (int i = 0; i < line.length() - 1; i = i + 2) { // проход по массиву через каждые 2 символа для нахождения новой подстроки
            String S1 = line.substring(i, i + 2); // нахождение подстроки с длиной в 2 символа
            SS[k++] = S1; // присваивание  подстроки к элементу массива
        }
        byte[] data = new byte[SS.length];
        for (int i = 0; i < SS.length; i++) {
            data[i] = (byte) ((byte) Integer.parseInt(SS[i], 16) & 0xFF);
        }
        long sum = 0;
        for (int i = 0; i <= data.length ; i++) {
            sum += data[i];


        }
        System.out.println(data);
    }
}


