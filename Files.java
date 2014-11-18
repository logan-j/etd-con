package etd_con;

import java.io.File;

/**Small class utilized by the Guide history tab in etd_con_gui. Was created to make it easy to both populate drop down
 * menus with filenames as well as get the full path of the file itself.
 * 
 * UPDATE: added functionality so that Files can be utilized by StrBool and etd_con_gui during unzipping and mapping to
 * multiple Digital Repository fields.
 * 
 * @author Logan Jewett with Iowa State University
 *
 */
public class Files {
	
	/**File object of the Files class. Mostly for keeping track of the full path.*/
	public File file;
	
	/**Filename associated with the File object above.*/
	public String name;
	
	/**Boolean to keep track of whether an input is a directory.*/
	public boolean type;
	
	/**Construct a Files object. Sets the File object and the name of the file.
	 * 
	 * @param input The input file you want to create a files object for.
	 */
	public Files(File input){
		file = input;
		name = input.getName();
		if(input.isDirectory() || name.contains("/") || name.contains("\\")){
			type = true;
		}else type = false;
	}
	
	/**Constructor that checks whether a filename contains a (fore/back)ward slash which means it's a directory.
	 * 
	 * @param input Filename String input.
	 */
	public Files(String input){
		file = null;
		name = input;
		if(name.contains("/") || name.contains("\\")){
			type = true;
		}else type = false;
	}
	
	/**Constructor is used in the event that you know a given filename is associated with a directory.
	 * 
	 * @param input Filename String input
	 * @param isDir If the input Filename is an associated directory.
	 */
	public Files(String input, boolean isDir){
		file = null;
		name = input;
		type = isDir;
	}
	
	/**Get method for returning whether this Files object is associated with a directory.
	 * 
	 * @return returns true if the Files object is a directory.
	 */
	public boolean getType(){
		return type;
	}
	
	/**Get method for obtaining the File object.
	 * 
	 * @return returns the file object.
	 */
	public File getFile(){
		return file;
	}
	
	/**Get method for obtaining the file name of the File object.
	 * 
	 * @return returns the file name of the file object.
	 */
	public String toString(){
		return name;
	}
}
