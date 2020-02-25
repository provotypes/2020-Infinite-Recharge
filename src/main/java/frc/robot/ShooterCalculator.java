package frc.robot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

import edu.wpi.first.wpilibj.Filesystem;

//csv format [distance,rpm,angle] (ball relitave)

public final class ShooterCalculator {

    private static boolean initialized = false;

    private static Map<Integer, Double> powerTable = new HashMap<>();
    private static Map<Integer, Double> angleTable = new HashMap<>();

    private ShooterCalculator() {
        throw new UnsupportedOperationException("ShooterCalculator is a utility class! If you're seeing this, the code is in what I thought was an unreachable state. I could give you advice for what to do. But honestly, why should you trust me? I clearly screwed this up. I'm writing a message that should never appear, yet I know it will probably appear someday. On a deep level, I know I'm not up to this task. I'm so sorry.");
    }

    public static double calculateRPM(double distance) {
        if (!initialized) {
            init();
        }
        int roundDistance = roundDis(distance);
        Double tableVal = powerTable.get(roundDistance);
        return (tableVal != null ? tableVal.doubleValue() : 0);
    }

    public static double calculateAngle(double distance) {
        if (!initialized) {
            init();
        }
        int roundDistance = roundDis(distance);
        Double tableVal = angleTable.get(roundDistance);
        return (tableVal != null ? tableVal.doubleValue() : 0);
    }

    private static int roundDis(double in) {
        int out = (int)in;
        out = (out / 12) * 12;
        return out;
    }

    public static void init() {
        if (!initialized) {

            File dataFile = new File(Filesystem.getDeployDirectory().getPath().concat("/ShooterTable.csv"));
            System.out.println("---------------" + dataFile.toString());
            powerTable.clear();
            angleTable.clear();

            try {
                List<double[]> records = new ArrayList<>();
                BufferedReader br = new BufferedReader(new FileReader(dataFile));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    double[] nums = new double[values.length];
                    for (int i = 0; i < values.length; i++){
                        nums[i] = Double.valueOf(values[i]);
                    }
                    records.add(nums);
                }
                br.close();
        
                for (int i = 0; i < records.size(); i++) {
                    powerTable.put((int)records.get(i)[0], records.get(i)[1]);
                    angleTable.put((int)records.get(i)[0], records.get(i)[2]);
                }
            } catch (Exception e) {
                e.printStackTrace();

            }

            initialized = true;
        }
    }

}
