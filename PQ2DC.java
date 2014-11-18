package etd_con;

import java.io.*;
import java.util.*;

/**The PQ2DC (ProQuest to Digital Repository) class handles looking up the mapping from a ProQuest field to a Digital
 * Repository one.
 * 
 * @author Logan Jewett with Iowa State University.
 *
 */
public class PQ2DC {
	/**ArrayList of all ProQuest fields in the specific guide.*/
	public ArrayList<String> search;
	
	/**ArrayList of all Digital Repository fields the ProQuest fields are mapped to.*/
	public ArrayList<String> result;
	
	/**Constructor which sorts the guide and initializes the ArrayList PQ2DC variables.
	 * 
	 * @param guide The reference guide to be used to convert the fields.
	 * @throws IOException Throws if there's an error trying to scan the guide file.
	 */
	public PQ2DC(File guide) throws IOException{
		UpdateGuide.sort(guide);
		
		Scanner line = new Scanner(guide);
		search = new ArrayList<String>();
		result = new ArrayList<String>();
		
		while(line.hasNextLine()){
			Scanner word = new Scanner(line.nextLine());
			word.useDelimiter(">");
			if(word.hasNext()){
				search.add(word.next());
				if(!word.hasNext()){
					result.add(" ");
				}else result.add(word.next());
			}
			word.close();
		}
		line.close();
	}
	
	/**Get method for the ProQuest fields ArrayList.
	 * 
	 * @return ArrayList of all the ProQuest fields.
	 */
	public ArrayList<String> getSearch(){
		return search;
	}
	
	/**Get method for the Digital Repository field mapping ArrayList.
	 * 
	 * @return ArrayList of what all the ProQuest fields map to in Digital Repository.
	 */
	public ArrayList<String> getResult(){
		return result;
	}
	
	/**Method for converting a ProQuest String object into a Digital Repository one.
	 * 
	 * @param proquest a String object of the field that needs to be converted from ProQuest to Digital Repository
	 * @return An StrBool object that contains what was looked up, what it converted to, and whether it was in the guide.
	 */
	public StrBool convert(String proquest){
		int contains = binarySearch(search, proquest, 0, search.size());
		if(contains < 0){
			return new StrBool(proquest, proquest, false);
		}
		else return new StrBool(proquest, result.get(contains), true);
	}
	
	/**Method for determining the index of a particular String in a sorted ArrayList. Ignores case.
	 * 
	 * @param array The ArrayList that you're looking through.
	 * @param proquest The String whose index you want to determine.
	 * @param left left bound for binarySearch algorithm.
	 * @param right right bound for binarySearch algorithm.
	 * @return The index a String is located at, or -1 if it couldn't be found.
	 */
	public static int binarySearch(ArrayList<String> array, String proquest, int left, int right){
		if(left > right) return -1;
		int middle = (left + right) / 2;
		int compare = proquest.compareToIgnoreCase(array.get(middle));
		if (compare == 0) return middle;
		else if(compare < 0) return binarySearch(array, proquest, left, middle - 1);
		else return binarySearch(array, proquest, middle + 1, right);
	}
}
