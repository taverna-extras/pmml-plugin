package uk.ac.manchester.cs.pmmlplugin.taverna;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * PmmlPlugin activity configuration bean.
 * 
 */
public class PmmlPluginActivityConfigurationBean implements Serializable {

	/*
	 * TODO: Remove this comment.
	 * 
	 * The configuration specifies the variable options and configurations for
	 * an activity that has been added to a workflow. For instance for a WSDL
	 * activity, the configuration contains the URL for the WSDL together with
	 * the method name. String constant configurations contain the string that
	 * is to be returned, while Beanshell script configurations contain both the
	 * scripts and the input/output ports (by subclassing
	 * ActivityPortsDefinitionBean).
	 * 
	 * Configuration beans are serialised as XML (currently by using XMLBeans)
	 * when Taverna is saving the workflow definitions. Therefore the
	 * configuration beans need to follow the JavaBeans style and only have
	 * fields of 'simple' types such as Strings, integers, etc. Other beans can
	 * be referenced as well, as long as they are part of the same plugin.
	 */
	
	// TODO: Remove the example fields and getters/setters and add your own	
	/*private String exampleString;

	private URI exampleUri;*/
	
	private String pmmlFile;

	/*
	public String getExampleString() {
		return exampleString;
	}

	public void setExampleString(String exampleString) {
		this.exampleString = exampleString;
	}

	public URI getExampleUri() {
		return exampleUri;
	}

	public void setExampleUri(URI exampleUri) {
		this.exampleUri = exampleUri;
	}*/
	
	public String getPmmlFile() {
		return pmmlFile;
	}
	
	public void setPmmlFile(String pmmlFile) {
		this.pmmlFile = pmmlFile;
	}
	
	public String getPMMLVersion() {
		String version = null;
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Document document = null;
		try {
			document = db.parse(new File(pmmlFile));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        NodeList nodeList = document.getElementsByTagName("PMML");
        for(int x=0,size= nodeList.getLength(); x<size; x++) {
            version = (nodeList.item(x).getAttributes().getNamedItem("version").getNodeValue());
        }
        
        return version;
	}

	public String getPMMLCopyright() {
		String copyright = null;
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Document document = null;
		try {
			document = db.parse(new File(pmmlFile));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        NodeList nodeList = document.getElementsByTagName("Header");
        for(int x=0,size= nodeList.getLength(); x<size; x++) {
            copyright = (nodeList.item(x).getAttributes().getNamedItem("copyright").getNodeValue());
        }
        
        return copyright;
	}
	
	public String getPMMLApplicationName() {
		String appName = null;
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Document document = null;
		try {
			document = db.parse(new File(pmmlFile));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        NodeList nodeList = document.getElementsByTagName("Application");
        for(int x=0,size= nodeList.getLength(); x<size; x++) {
            appName = (nodeList.item(x).getAttributes().getNamedItem("name").getNodeValue());
        }
        
        return appName;
	}
	
	public String getPMMLApplicationVersion() {
		String appVersion = null;
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Document document = null;
		try {
			document = db.parse(new File(pmmlFile));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        NodeList nodeList = document.getElementsByTagName("Application");
        for(int x=0,size= nodeList.getLength(); x<size; x++) {
            appVersion = (nodeList.item(x).getAttributes().getNamedItem("version").getNodeValue());
        }
        
        return appVersion;
	}
	
	/* Working fine but errors if Timestamp element is missing
	public String getPMMLTimestamp() {
		String timestamp = "";
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Document document = null;
		try {
			document = db.parse(new File(pmmlFile));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        NodeList nodeList = document.getElementsByTagName("Timestamp");
        timestamp = nodeList.item(0).getTextContent();
        
        return timestamp;
	}*/
}
