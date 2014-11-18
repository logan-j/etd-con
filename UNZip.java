package etd_con;
import java.util.zip.*;
import java.io.*;
import java.util.*;

/**The class handles all of the unzipping of individual zipped files. Used by BatchUnzip.
 * UPDATE: Functionality of BatchUnzip absorbed by etd_con_gui and removed. Class used by etd_con_gui.
 * 
 * @author Logan Jewett with Iowa State University
 *
 */
public class UNZip {
	/**Stores a reference to the zipped file to be unzipped.*/
	public File input;
	
	/**Static method for copying an input stream.
	 * 
	 * @param in The InputStream to be copied.
	 * @param out OuputStream of where you'd like the InputStream to be copied.
	 * @throws IOException Throws IOException if there's an error while copying the stream.
	 */
	public static final void copyInputStream(InputStream in, OutputStream out)throws IOException{
				byte[] buffer = new byte[1024];
				int len;
				while((len = in.read(buffer)) >= 0)
					out.write(buffer, 0, len);
					in.close();
					out.close();
			  }
	
	/**Constructor for setting the instance variable.
	 * 
	 * @param file File object that needs to be unzipped.
	 */
	public UNZip(File file){
		input = file;
	}
	
	/**Method for unzipping file specified in the constructor.
	 * 
	 * @param dir String of the temp folder directory path contents are being unzipped to.
	 * @return StrBool From = file unzipped, To = Files array of files' filenames, Status = whether the unzip was successful.
	 */
	public StrBool commenceUnzip(String dir){
		Enumeration<? extends ZipEntry> entries;
		ZipFile zipFile;
		
		try {
			zipFile = new ZipFile(input);
			entries = zipFile.entries();
			ArrayList<Files> files = new ArrayList<Files>();
			while(entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry)entries.nextElement();
				
				if(entry.getName().contains("/") || entry.getName().contains("\\")) {
					if(entry.isDirectory()){
						files.add(new Files(entry.getName(), true));
						(new File(dir + entry.getName())).mkdirs();
						continue;
				        }try{
				        	copyInputStream(zipFile.getInputStream(entry),
				    		new BufferedOutputStream(new FileOutputStream(dir + entry.getName())));
				        }catch(Exception e){
				        	files.add(new Files(entry.getName(), true));
							Scanner input = new Scanner(entry.getName());
							input.useDelimiter("/");
							ArrayList<String> directory = new ArrayList<String>();
							while(input.hasNext())directory.add(input.next() + "/");
							String pathname = "";
							for(int i = 0; i < directory.size() - 1; i++){
								pathname += directory.get(i);
							}
							input.close();
							(new File(dir + pathname)).mkdirs();
				        }
					}

				files.add(new Files(entry.getName(), false));
				copyInputStream(zipFile.getInputStream(entry),
				new BufferedOutputStream(new FileOutputStream(dir + entry.getName())));

			}
			
			zipFile.close();
			return new StrBool(input.getName(), files, true);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return new StrBool(input.getName(), (ArrayList<Files>) null, false);
		}
	}
}