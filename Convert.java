package etd_con;

import java.io.*;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.*;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**Class that handles the conversion of single XML files using XSL transform as well as convert the specific ProQuest
 * Degree, Department, and Discipline fields to Digital Repository. 
 * 
 * @author Logan Jewett with Iowa State University
 *
 */
public class Convert{
	/**The inputed XSL transform code to be used.*/
	Source xsl;
	
	/**The inputed XML file to be converted.*/
	File xmlInput;
	
	/**The inputed destination for the converted XML file.*/
	File xmlOutput;
	
	/**The transformer to be implemented in the process of conversion.*/
	Transformer transformer;
	
	/**Flag enum to indicated guide types*/
	public enum Guide{
		DIS, DEG, DEP
	}
	
	/**Constructs the transformer object to be used to convert XML files.
	 * 
	 * @param XSL The file of the XSL transform code to be used.
	 */
	public Convert(InputStream XSL) throws TransformerConfigurationException{
		xsl = new StreamSource(XSL);
		System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
		TransformerFactory factory = TransformerFactory.newInstance();
		transformer = factory.newTransformer(xsl);
		
	}
	
	/**Small setter class for the XML input and output. Allows for multiple files to be converted with the same Convert
	 * object. 
	 * 
	 * @param xmlI The XML file to be converted.
	 * @param xmlO The destination file for the converted XML.
	 */
	public void declare(File xmlI, File xmlO){
		xmlInput = xmlI;
		xmlOutput = xmlO;
	}
	
	/**This class evaluates the path of the type specified and converts the string(s) in the field(s) from ProQuest to
	 * Digital Repository using the reference guide that is also specified.
	 * 
	 * @param guide The reference guide to be used.
	 * @param type Which type the reference guide is (DEG, DEP, or DIS)
	 * @return Returns an ArrayList of type StrBool for the strings of all relevant fields affected.
	 */
	public ArrayList<StrBool> normalize(File guide, Guide type){
		String path;
		ArrayList<StrBool> output = new ArrayList<StrBool>();
		switch(type){
			case DEG:
				path = "//documents/document/degree_name";
				break;
			case DIS:
				path = "//disciplines/discipline";
				break;
			case DEP:
				path = "//document/department";
				break;
			default:
				System.err.println("invalid guide type");
				return null;
		}
		try{
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(new InputSource(xmlOutput.getAbsolutePath()));
	        
			XPath xpath = XPathFactory.newInstance().newXPath();
			NodeList nodes = (NodeList)xpath.evaluate(path, doc, XPathConstants.NODESET);
			PQ2DC lookup = new PQ2DC(guide);			
			//This for loop is for adds that map to multiple fields.
			for(int i = 0; i < nodes.getLength(); i++){
				output.add(lookup.convert(nodes.item(i).getTextContent()));
				if(output.get(i).getSize() > 1){
					nodes.item(i).setTextContent(output.get(i).getToArray().get(0).toString());
					for(int j = 1; j < output.get(i).getToArray().size(); j++){
						Node parentNode = nodes.item(i).getParentNode();
						NodeList childNodeList = parentNode.getChildNodes();
						Element toAdd;
						if(type.equals(Guide.DEP))toAdd = doc.createElement("department");
						if(type.equals(Guide.DEG))toAdd = doc.createElement("degree");
						else toAdd = doc.createElement("discipline");
						boolean check = false;
						toAdd.appendChild(doc.createTextNode(output.get(i).getToArray().get(j).toString()));
						for(int k = 0; k < childNodeList.getLength(); k++){
							if(toAdd.getTextContent().equals(childNodeList.item(k).getTextContent())){
								check = true;
							}	
						}
						if(!check && !toAdd.getTextContent().isEmpty())parentNode.appendChild(toAdd);
					}
				}else{
					nodes.item(i).setTextContent(output.get(i).getTo());
					if(nodes.item(i).getTextContent().isEmpty())nodes.item(i).getParentNode().removeChild(nodes.item(i));
				}
			}
			Transformer xformer = TransformerFactory.newInstance().newTransformer();
			xformer.transform(new DOMSource(doc), new StreamResult(xmlOutput));
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return output;
	}
	
	/**Implements the transformer and runs the conversion.
	 * 
	 * @return A boolean to indicate if the conversion was successful.
	 */
	public boolean convert(){
		try{
			transformer.transform(new StreamSource(xmlInput), new StreamResult(xmlOutput));
		}catch(TransformerException e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
