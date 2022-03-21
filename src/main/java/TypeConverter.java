import javax.xml.stream.events.XMLEvent;
import java.util.ArrayList;

public class TypeConverter {
    private TypeConverter(){}

    public static String convert(XMLEvent e){
        String s = e.asCharacters().getData();
        s = s.substring(1);
        s = s.replaceAll(" ", "");
        return s;
    }

    public static ArrayList<Byte> convert(char[] c){
        ArrayList<Byte> bArr = new ArrayList<>();
        String tmp = "";
        for (int i = 0; i < c.length - 1; i++) {
            if (c[i] == ',') {
                byte b = Byte.parseByte(tmp);
                bArr.add(b);
                tmp = "";
            } else {
                tmp+=c[i];
            }
        }
        bArr.add(Byte.valueOf(tmp));
        return bArr;
    }

    public static byte[] convert(ArrayList<Byte> bList){
        byte[] b = new byte[bList.size()];
        for (int i = 0; i < bList.size(); i++) {
            b[i] = bList.get(i);
        }
        return b;
    }
}
