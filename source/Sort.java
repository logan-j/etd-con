package etd_con;

import java.util.ArrayList;

import etd_con.Convert.Guide;

/**This class is used by the Edit Guides tab in etd_con_gui. Takes an inputed text area during batch add mode and
 * will sort the rules into appropriately organized ArrayLists to be added.
 * 
 * @author Logan Jewett with Iowa State University
 *
 */
public class Sort {
	/**ArrayList to store rules to add to the Degree guide*/
	public ArrayList<String> Degree = new ArrayList<String>();
	
	/**ArrayList to store rules to add to the Department guide*/
	public ArrayList<String> Department = new ArrayList<String>();
	
	/**ArrayList to store rules to add to the Discipline guide*/
	public ArrayList<String> Discipline = new ArrayList<String>();
	
	/**Constructs each ArrayList from the given text area. Uses a flag to indicate which ArrayList is being worked on.
	 * 
	 * @param input The text area inputed as an ArrayList.
	 */
	public Sort(ArrayList<String> input){
		Guide flag = Switch(input.get(0));
		if(flag == null) return;
		for(int i = 1; i < input.size(); i++){
			String toAdd = input.get(i);
			Guide temp = Switch(toAdd);
			if(temp == null){
				try{
					Add(flag).add(toAdd);
				}catch(NullPointerException e){
					e.printStackTrace();
				}
			}else flag = temp;
		}
		
	}
	
	/**This method takes a Guide enum parameter  and returns the associated ArrayList.
	 * 
	 * @param flag Guide enum parameter.
	 * @return Returns the associated ArrayList based on the flag; null otherwise.
	 */
	public ArrayList<String> Add(Guide flag){
		if(flag.equals(Guide.DEG)) return Degree;
		if(flag.equals(Guide.DEP)) return Department;
		if(flag.equals(Guide.DIS)) return Discipline;
		else return null;
	}
	
	
	/**This method takes a String input and returns either a Guide enum or null depending on the string input.
	 * 
	 * @param input The input String being checked to see if it's a Guide enum.
	 * @return Returns the associated Guide enum; null if it isn't one.
	 */
	public Guide Switch(String input){
		if(input.equalsIgnoreCase("DEG"))return Guide.DEG;
		if(input.equalsIgnoreCase("DIS"))return Guide.DIS;
		if(input.equalsIgnoreCase("DEP"))return Guide.DEP;
		else return null;
	}
	
	/**Get method for obtaining the Degree ArrayList.
	 * 
	 * @return Returns the Degree ArrayList.
	 */
	public ArrayList<String> getDeg(){
		return Degree;
	}
	
	/**Get method for obtaining the Department ArrayList.
	 * 
	 * @return Returns the Department ArrayList.
	 */
	public ArrayList<String> getDep(){
		return Department;
	}
	
	/**Get method for obtaining the Discipline ArrayList.
	 * 
	 * @return Returns the Discipline ArrayList.
	 */
	public ArrayList<String> getDis(){
		return Discipline;
	}
}
