package etd_con;

import java.util.Comparator;
import java.util.Scanner;

/**This is a custom comparator made specifically for handling the guides. Allows the guides to be sorted alphabetically
 * as well as being used as the comparator in binary search operations. 
 * 
 * @author Logan Jewett with Iowa State University
 *
 */
public class AlphaComparator implements Comparator<String>{
	
	/**Overrides the compare class to examine and compare only the ProQuest strings.
	 * 
	 * @return Returns an int representation of the comparison. Ignores Case. 
	 */
	@Override
	public int compare(String arg0, String arg1) {
		Scanner el1 = new Scanner(arg0);
		Scanner el2 = new Scanner(arg1);
		el1.useDelimiter(">");
		el2.useDelimiter(">");
		String first = el1.next();
		String second = el2.next();
		el1.close();
		el2.close();
		return first.compareToIgnoreCase(second);
	}
}
