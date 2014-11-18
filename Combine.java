package etd_con;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**Class used expressly for combining the converted XML into one file ready for batch upload.
 * 
 * @author Logan Jewett with Iowa State University
 *
 */
public class Combine {
	
	/**Location directory of the XML files to be combined*/
	public File dir;
	
	/**Constructor that assigns the specified file as the working directory
	 * 
	 * @param file Directory location of the relevant XML files.
	 */
	public Combine(File file){
		dir = file;
	}
	
	/**Method for combining the XML files. Just uses a scanner to pull all text inbetween the relevant tags and output 
	 * it to a destination XML file.
	 * 
	 * @throws IOException Will throw exception if there's an error while trying to read/write files.
	 */
	public void combine() throws IOException{
		if(dir.isDirectory()){
			File[] list = dir.listFiles();
			Date today = new Date();
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			File xmlO = new File(dir.getAbsolutePath() + "\\ETDmetadata" + dateFormat.format(today) + ".xml");
			PrintWriter out = new PrintWriter(xmlO, "iso-8859-1");
			out.write("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>");
			out.write("\n<documents xmlns:str=\"http://www.metaphoricalweb.org/xmlns/string-utilities\" " +
					"xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" " +
					"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
					"xsi:noNamespaceSchemaLocation=\"http://www.bepress.com/document-import.xsd\">");
			for(int i = 0; i < list.length; i++){
				
				if(!list[i].getName().startsWith("ETDmetadata") && list[i].getName().endsWith(".xml")){
					String read = "";
					String write = "";
					BufferedReader buff = new BufferedReader(new FileReader(list[i]));
					while((write = buff.readLine()) != null){
						read += write + "\n";
					}
					buff.close();
					
					Pattern docOpen = Pattern.compile("<document>");
					Pattern docClose = Pattern.compile("</document>");
					Matcher doc2Open = docOpen.matcher(read);
					Matcher doc2Close = docClose.matcher(read);
//					if(!doc2Open.find() && !doc2Close.find())throw new IOException("Improperly Formatted XML. Does Not Contain One or Both Document Tags.");
					int start = 0;
					int end = 0;
					boolean check1 = false;
					boolean check2 = false;
					while(doc2Open.find()){
						start = doc2Open.start();
						check1 = true;
					}
					while(doc2Close.find()){
						end = doc2Close.end();
						check2 = true;
					}
					if(!check1 && !check2)throw new IOException("Improperly Formatted XML. Does Not Contain One or Both Document Tags.");
					
					out.write("\n\t" + read.substring(start, end) + "\n\t");
					
//					Scanner first = new Scanner(list[i]);
//					first.useDelimiter("<document>");
//					if(first.hasNext()){
//						first.next();
//						if(first.hasNext()){
//							String test = first.next();
//							System.err.println(test + first.hasNext());
//							Scanner second = new Scanner(test);
//							second.useDelimiter("</document>");
//							out.write("\n<document>");
//							String next = second.next();
//							out.write(next);
//							second.close();
//							first.close();
//							out.write("\n</document>");
//							list[i].delete();
//						}				
//					}
					
				}
			}
			out.write("\n</documents>");
			out.close();
		}
	}
}
