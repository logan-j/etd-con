package etd_con;

import java.io.*;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**This class was written to handle updating/editing all plain text guides. Utilized by the edit guides tab in etd_con_gui.
 * UPDATE: combined sort and check and removed an outdated/unused methods.
 * 
 * @author Logan Jewett with Iowa State University
 *
 */
public class UpdateGuide {
	/**Stores the inputted guide that is going to be edited.*/
	public File guideToUpdate;
	
	/**Stores The associated archive directory of the guide being updated.*/
	public File archiveDirectory;
	
	/**Stores the date/time information.*/
	public Date date;
	
	/**Stores the associated format for the date/time information*/
	public DateFormat dateFormat;
	
	/**Constructor that initializes all of the instance variables to be used. Also stores a copy of the guide being updated
	 * to the archive directory.
	 * 
	 * @param oldGuide The guide that is being worked on.
	 * @param archiveDir The associated archive directory for that guide.
	 * @throws IOException Throws an IOException if the guide/archive directory couldn't be found, or if copyFile fails.
	 */
	public UpdateGuide(File oldGuide, File archiveDir) throws IOException{
		date = new Date();
		dateFormat = new SimpleDateFormat("yyyyMMdd@HHmmss");
		archiveDir.mkdirs();
		File copy = new File(archiveDir.getAbsolutePath() + "\\" + oldGuide.getName() + dateFormat.format(date));
		copyFile(oldGuide, copy);
		guideToUpdate = oldGuide;
		archiveDirectory = archiveDir;
	}
	
	/**Method for adding a list of rules to the guide being updated.
	 * 
	 * @param toAdd An ArrayList of all the rules that are being added to the guide.
	 * @return Returns true if the add to the the guide was successful.
	 */
	public boolean add(ArrayList<String> toAdd){
		try{
			if(toAdd.isEmpty() || toAdd == null)return true;
			
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(guideToUpdate, true)));
			PrintWriter addlist = new PrintWriter(new BufferedWriter(new FileWriter(archiveDirectory.getAbsolutePath() + "\\Add Log", true)));
			addlist.println(dateFormat.format(date) + "\n");
			for(String string : toAdd){
				addlist.println(string + "\n");
				out.println(string + "\n");
			}
			addlist.println("");
			addlist.close();
			out.close();
			sort(guideToUpdate);
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**Static method for sorting a guide.
	 * 
	 * @param guide A File object of the guide to be sorted.
	 * @throws IOException Will throw this exception of the file can't be found or written to for whatever reason.
	 */
	public static void sort(File guide) throws IOException{
		Scanner sort = new Scanner(guide);
		ArrayList<String> list = new ArrayList<String>();
		while(sort.hasNextLine()){
			String next = sort.nextLine();
			if(!next.isEmpty() || next.equals(" ") || next.equals("\t"))list.add(next);
		}
		Comparator<String> alpha = new AlphaComparator();
		Collections.sort(list, alpha);
		FileWriter fstream = new FileWriter(guide);
		BufferedWriter out = new BufferedWriter(fstream);
		for(String string : list){
			out.write(string);
			out.newLine();
		}
		sort.close();
		out.close();
	}
	
	/**Static method for copying a file.
	 * 
	 * @param sourceFile The File object of the file being copied.
	 * @param destFile The File object of where the copied contents are going.
	 * @throws IOException Throws this exception if there's an error while copying with the IO streams.
	 */
	public static void copyFile(File sourceFile, File destFile) throws IOException {
		if(!destFile.exists()) {
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;
    
		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		}
		finally {
			if(source != null) {
				source.close();
			}
			if(destination != null) {
				destination.close();
			}
		}
	}
}