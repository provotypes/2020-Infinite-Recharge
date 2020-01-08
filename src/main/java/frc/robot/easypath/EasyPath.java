package frc.robot.easypath;


public class EasyPath {

    private static final EasyPath instance = new EasyPath();
    private EasyPathConfig config;
  
    /**
     * Private constructor in order to accomodate a singleton state
     */
    private EasyPath() {
    }
  
    /**
     * @return the singleton state for EasyPath
     */
    public static EasyPath getInstance() {
      return instance;
    }
  
    /**
     * Sets the config for the EasyPath instance
     *
     * @param config the configuration object representing a drive train
     */
    private void setConfig(EasyPathConfig config) {
      this.config = config;
    }
  
    /**
     * Configures EasyPath to use the drive train that you described with an EasyPathConfig object.
     *
     * @param config the EasyPathConfig object representing your drive train
     */
    public static void configure(EasyPathConfig config) {
      getInstance().setConfig(config);
    }
  
    /**
     * @return the configuration object currently being used by EasyPath
     */
    public static EasyPathConfig getConfig() {
      return getInstance().config;
    }
  }
  
