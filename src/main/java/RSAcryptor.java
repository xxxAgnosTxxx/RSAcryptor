import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Objects;

public class RSAcryptor {
    private static KeyPairGenerator generator;
    private static PublicKey publicKey;
    private static PrivateKey privateKey;

    static {
        try {
            generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Algorithm not found.\n");
        }
    }

    private RSAcryptor() {
    }

    public static void generateKeys() {
        KeyPair pair = generator.genKeyPair();
        publicKey = pair.getPublic();
        privateKey = pair.getPrivate();
    }

    public static void exportXML(File file) {
        if (publicKey == null) {
            System.out.println("Before export keys to a file generate them.\n");
            return;
        }

        byte[] publicKeyArr = publicKey.getEncoded();
        byte[] privateKeyArr = privateKey.getEncoded();
        /*
         * byte[] publicKeyArr = SerializationUtils.serialize(publicKey);
         * byte[] privateKeyArr = SerializationUtils.serialize(privateKey);
         */
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write("<KeyGroup>\n");
            writer.write("  <public>");
            writer.write(Arrays.toString(publicKeyArr));
            writer.write("</public>\n");
            writer.write("  <private>");
            writer.write(Arrays.toString(privateKeyArr));
            writer.write("</private>\n");
            writer.write("</KeyGroup>\n");
            writer.flush();
            writer.close();
            System.out.println("Keys successfully exported.\n");
        } catch (IOException e) {
            System.out.println("Writing error.\n");
        }
    }

    public static void importXML(File file) {
        String publicKeyString = null, privateKeyString = null;
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        try {
            XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream(file));
            while (reader.hasNext()) {
                XMLEvent nextEvent = reader.nextEvent();
                if (nextEvent.isStartElement()) {
                    StartElement startElement = nextEvent.asStartElement();
                    switch (startElement.getName().getLocalPart()) {
                        case "public":
                            nextEvent = reader.nextEvent();
                            publicKeyString = TypeConverter.convert(nextEvent);
                            break;
                        case "private":
                            nextEvent = reader.nextEvent();
                            privateKeyString = TypeConverter.convert(nextEvent);
                            break;
                    }
                }
            }
            try {
                KeyFactory factory = KeyFactory.getInstance("RSA");
                EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(TypeConverter.convert(TypeConverter.convert(Objects.requireNonNull(publicKeyString).toCharArray())));
                EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(TypeConverter.convert(TypeConverter.convert(Objects.requireNonNull(privateKeyString).toCharArray())));
                publicKey = factory.generatePublic(publicKeySpec);
                privateKey = factory.generatePrivate(privateKeySpec);
            } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                System.out.println("Invalid algorithm or keys.\n");
            }
            /*
             * publicKey = SerializationUtils.deserialize(TypeConverter.convert(TypeConverter.convert(publicKeyString.toCharArray())));
             * privateKey = SerializationUtils.deserialize(TypeConverter.convert(TypeConverter.convert(privateKeyString.toCharArray())));
             */
            System.out.println("Keys was imported from XML-file.\n");
        } catch (XMLStreamException |
                FileNotFoundException e) {
            System.out.println("File not found or importing error from XML-file.\n");
        }
    }

    public static void encode(File f) {
        try {
            byte[] fByte = Files.readAllBytes(f.toPath());
            Cipher encryptCipher = Cipher.getInstance(generator.getAlgorithm());
            encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptByte = encryptCipher.doFinal(fByte);
            try (FileOutputStream fos = new FileOutputStream(f)) {
                fos.write(encryptByte);
            }
            System.out.println("File was encoded.\n");
        } catch (NoSuchPaddingException | BadPaddingException e) {
            System.out.println("Padding exception\n");
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            System.out.println("Algorithm or key not founds.\n");
        } catch (IllegalBlockSizeException e) {
            System.out.println("File size is big for encode\n");
        } catch (IOException e) {
            System.out.println("File not found.\n");
        }
    }

    public static void decode(File f) {
        try {
            byte[] fByte = Files.readAllBytes(f.toPath());
            Cipher decryptCipher = Cipher.getInstance(generator.getAlgorithm());
            decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptByte = decryptCipher.doFinal(fByte);
            try (FileOutputStream fos = new FileOutputStream(f)) {
                fos.write(decryptByte);
            }
            System.out.println("File was decoded.\n");
        } catch (NoSuchPaddingException | BadPaddingException e) {
            System.out.println("Padding exception\n");
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            System.out.println("Algorithm or key not founds.\n");
        } catch (IllegalBlockSizeException e) {
            System.out.println("File size is big for encode\n");
        } catch (IOException e) {
            System.out.println("File not found.\n");
        }
    }
}
