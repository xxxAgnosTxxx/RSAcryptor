import java.io.File;

public class FileUtils {
    private static File importXML;
    private static File exportXML;
    private static File encodeFile;
    private static File decodeFile;

    public static File getEncodeFile() {
        return encodeFile;
    }

    public static File getDecodeFile() {
        return decodeFile;
    }

    private FileUtils(){
    }

    public static void setExportFile(String path){
        exportXML = new File(path);
    }

    public static void setImportFile(String path){
        importXML = new File(path);
    }

    public static File getExportXML(){
        return exportXML;
    }

    public static File getImportXML(){
        return importXML;
    }

    public static void setEncodeFile(String path){
        encodeFile = new File(path);
    }

    public static void setDecodeFile(String path){
        decodeFile = new File(path);
    }
}
