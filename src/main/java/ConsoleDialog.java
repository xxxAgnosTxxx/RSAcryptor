import java.util.Scanner;

public class ConsoleDialog {
    private ConsoleDialog() {
    }

    public static void start() {
        System.out.println("Enter the action:\n" +
                "1. Generate keys.\n" +
                "2. Export keys to XML.\n" +
                "3. Import keys from XML.\n" +
                "4. Encrypt the file.\n" +
                "5. Decrypt the file.\n" +
                "-1. Exit.");
    }

    public static void actions(int a) {
        Scanner scanner = new Scanner(System.in);
        switch (a) {
            case 1:
                RSAcryptor.generateKeys();
                System.out.println("Keys were generated.\n");
                break;
            case 2:
                System.out.println("Enter the absolute path of file to save:");
                FileUtils.setExportFile(scanner.next());
                RSAcryptor.exportXML(FileUtils.getExportXML());
                break;
            case 3:
                System.out.println("Enter the absolute path of file to import:");
                FileUtils.setImportFile(scanner.next());
                RSAcryptor.importXML(FileUtils.getImportXML());
                break;
            case 4:
                System.out.println("Enter the absolute path of file to encode:");
                FileUtils.setEncodeFile(scanner.next());
                RSAcryptor.encode(FileUtils.getEncodeFile());
                break;
            case 5:
                System.out.println("Enter the absolute path of file to decode:");
                FileUtils.setDecodeFile(scanner.next());
                RSAcryptor.decode(FileUtils.getDecodeFile());
                break;
            case -1:
                System.out.println("Closing...");
                break;
        }
    }
}
