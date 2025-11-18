package Utils;

import java.util.Random;
import java.util.UUID;

public class testDataGenerator {


    public static String generateExternalId() {
        return "EXT_" + new Random().nextInt(100000);
    }

    public static String generateStationName() {
        return "Station_" + new Random().nextInt(100000);
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

}
