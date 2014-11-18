package etd_con;

import java.util.ArrayList;
import java.util.Scanner;

/** 
 * StrBool is a small wrapper class utilized by PQ2DC.java and Convert.java to track whether a look up in a ProQuest
 * conversion guide has returned a new item and to make it easy to keep track of what the items are converted to.
 * 
 * UPDATE: added functionality so that StrBool can be utilized by etd_con_gui during unzipping and mapping to multiple 
 * Digital Repository fields. **Different classes utilize some fields differently.
 * 
 * @author Logan Jewett with Iowa State University
 *
 */
public class StrBool {
	
	/**The Conversion Status (whether a lookup has returned the associated Digital Repository string)
	 * UPDATE: Also used to store a file's unzipped status
	 */
	public boolean status;
	
	/**The ProQuest string being converted*/
	public String from;
	
	/**The Digital Repository field(s) that the ProQuest field is converted to*/
	public ArrayList<Files> to = new ArrayList<Files>();
	
	
	/**Constructor to manually set the status of an StrBool.
	 * 
	 * @param f	The ProQuest string.
	 * @param t	The Digital repository string.
	 * @param changed The conversion status of the ProQuest string.
	 */
	public StrBool(String f, String t, Boolean changed){
		from = f;
		to = parseInput(t);
		status = changed;
	}
	
	/**Basic constructor to generate a StrBool.
	 * 
	 * @param f	The ProQuest string.
	 * @param t	The Digital repository string.
	 */
	public StrBool(String f, String t){
		new StrBool(f, t, false);
	}
	
	/**Constructor that makes an StrBool object for zipped files.
	 * 
	 * @param zipped filename of the file that is zipped.
	 * @param contents An ArrayList of Files objects for the files contained in the zipped file.
	 * @param success Whether the unzip of the zipped file was successful.
	 */
	public StrBool(String zipped, ArrayList<Files> contents, boolean success){
		from = zipped;
		to = contents;
		status = success;
	}
	
	/**Get method for retrieving the ProQuest String.
	 * 
	 * @return Returns the ProQuest String. 
	 */
	public String getFrom(){
		return from;
	}
	
	/**Get method for retrieving the Digital Repository String.
	 * 
	 * @return Returns the Digital Repository String the ProQuest String is converted to. 
	 */
	public String getTo(){
		String output = "";
		for(int i = 0; i < to.size(); i++){
			if(i > 0){
				output += ";";
			}
			output += to.get(i);
		}
		return output;
	}
	
	public ArrayList<Files> getToArray(){
		return to;
	}
	
	/**Get method for checking the status of a conversion.
	 * 
	 * @return Returns the status of the conversion. 
	 */
	public boolean getStatus(){
		return status;
	}
	
	/**Getter method for returning the number of fields a ProQuest reference maps to.
	 * 
	 * @return	The number of fields mapped to.
	 */
	public int getSize(){
		return to.size();
	}
	
	/**Sets the 'from' and 'to' strings and indicates that they've been changed since the StrBool object had been created
	 * 
	 * @param f What the 'from' ProQuest string is being changed to.
	 * @param t	What the 'to' Digital Repository string is being changed to.
	 * */
	public void setStrings(String f, String t){
		from = f;
		to = parseInput(t);
		status = true;
	}
	
	/**Method for parsing a Digital Repository input String from a guide.
	 * 
	 * @param input The String input from a guide that 
	 * @return A Files ArrayList of all the Digital Repository fields that a ProQuest reference maps to.
	 */
	public ArrayList<Files> parseInput(String input){
		Scanner parse = new Scanner(input);
		parse.useDelimiter(";");
		ArrayList<Files> output = new ArrayList<Files>();
		while(parse.hasNext())output.add(new Files(parse.next()));
		parse.close();
		return output;
	}
}
