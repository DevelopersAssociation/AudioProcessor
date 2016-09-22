package fruitbasket.com.audioprocessor;

import java.io.File;

final public class DataIOHelper {
	private static final DataIOHelper dataIOHelper=new DataIOHelper();
	
	private DataIOHelper(){}
	
	public DataIOHelper getInstance(){
		return dataIOHelper;
	}
	
	/**
	 * get a recorded file name by time
	 * @return  the file name
	 */
	public static String getRecordedFileName(String extensionName){
		return AppCondition.APP_FILE_DIR+File.separator+System.currentTimeMillis()+"."+extensionName;
	}
}
