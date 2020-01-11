package frc.robot;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

public class ColorSensor {
 
    private static ColorSensor instance;
    private  final I2C.Port i2cPort = I2C.Port.kOnboard;
    private  final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);
    private  final ColorMatch m_colorMatcher = new ColorMatch();
    
    Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
    Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
    Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
    Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);
      
    private ColorSensor() {}

    // private static Color[] kColors = new Color[] {kBlueTarget, kGreenTarget, kRedTarget, kYellowTarget};
     
	public void matchColors() {
      m_colorMatcher.addColorMatch(kBlueTarget);
      m_colorMatcher.addColorMatch(kGreenTarget);
      m_colorMatcher.addColorMatch(kRedTarget);
      m_colorMatcher.addColorMatch(kYellowTarget); 
	}

    public static ColorSensor getInstance() {
        if(instance == null) {
            instance = new ColorSensor();
        }
        return instance;
    }

    public  void update() {
        String colorString;
        Color detectedColor = m_colorSensor.getColor();     
        ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

        if (match.color == kBlueTarget) {
            colorString = "Blue";
          } else if (match.color == kRedTarget) {
            colorString = "Red";
          } else if (match.color == kGreenTarget) {
            colorString = "Green";
          } else if (match.color == kYellowTarget) {
            colorString = "Yellow";
          } else {
            colorString = "Unknown";
          }
         
          SmartDashboard.putNumber("Red", detectedColor.red);
          SmartDashboard.putNumber("Green", detectedColor.green);
          SmartDashboard.putNumber("Blue", detectedColor.blue);
          SmartDashboard.putNumber("Confidence", match.confidence);
          SmartDashboard.putString("Detected Color", colorString);
    }
  }