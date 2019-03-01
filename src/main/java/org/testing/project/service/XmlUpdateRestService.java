package org.testing.project.service;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringUtils;
import org.rauschig.jarchivelib.ArchiveFormat;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;
import org.rauschig.jarchivelib.CompressionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.testing.project.entity.Nugget;
import org.testing.project.entity.NuggetField;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Service
public class XmlUpdateRestService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public void updateXMLNugget(Nugget nugget) {
		try {
			File folder = new File(nugget.getInputFolderPath());
			File[] listOfFiles = folder.listFiles();
			int nCount = nugget.getNuggetCount() > 1 ? nugget.getNuggetCount() : 1;
			for (int j = 0; j < nCount; j++) {
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				String foldername = "/PERF" + timestamp.getTime();
				String path = (nugget.getOutputFolderPath() + foldername);
				logger.debug(path);
				for (File file : listOfFiles) {
					String filename = file.getName();
					if (filename.endsWith(".xml") || filename.endsWith(".XML")) {
						DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
						DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
						Document doc = docBuilder.parse(file.getAbsolutePath());

						NodeList nodeCUSIP = doc.getElementsByTagName(NuggetField.CUSIP.nugget());
						if (StringUtils.isNotBlank(nugget.getCusip()) && nodeCUSIP.getLength() > 0) {
							for (int i = 0; i < nodeCUSIP.getLength(); i++) {
								nodeCUSIP.item(i).setTextContent(nugget.getCusip());
							}
						}

						NodeList nodeINVNUM = doc.getElementsByTagName(NuggetField.INVNUM.nugget());
						if (StringUtils.isNotBlank(nugget.getInvnum()) && nodeINVNUM.getLength() > 0) {
							Integer result = Integer.valueOf(nugget.getInvnum()).intValue();
							result = result + j;
							for (int i = 0; i < nodeINVNUM.getLength(); i++) {
								nodeINVNUM.item(i).setTextContent(String.valueOf(result));
							}
						}

						NodeList nodeportfolioname = doc
								.getElementsByTagName(NuggetField.PORTFOLIOS_PORTFOLIO_NAME.nugget());
						if (StringUtils.isNotBlank(nugget.getPortfoliosPortfolioName())
								&& nodeportfolioname.getLength() > 0) {
							for (int i = 0; i < nodeportfolioname.getLength(); i++) {
								nodeportfolioname.item(i).setTextContent(nugget.getPortfoliosPortfolioName());
							}
						}

						NodeList nodetouchcount = doc.getElementsByTagName(NuggetField.TOUCH_COUNT.nugget());
						if (StringUtils.isNotBlank(nugget.getTouchCount()) && nodetouchcount.getLength() > 0) {
							for (int i = 0; i < nodeportfolioname.getLength(); i++) {
								nodeportfolioname.item(i).setTextContent(nugget.getTouchCount());
							}
						}
						TransformerFactory transformerFactory = TransformerFactory.newInstance();
						Transformer transformer = transformerFactory.newTransformer();
						DOMSource source = new DOMSource(doc);
						new File(path).mkdirs();
						StreamResult result = new StreamResult(new File(path + "/" + filename));
						transformer.transform(source, result);
					}
				}
			//	Archiver tar = ArchiverFactory.createArchiver(ArchiveFormat.TAR);
				Archiver gzip = ArchiverFactory.createArchiver(ArchiveFormat.TAR, CompressionType.GZIP);
			//	tar.create(foldername, new File(nugget.getOutputFolderPath()), new File(path));
				gzip.create(foldername, new File(nugget.getOutputFolderPath()+ "/executivefile"), new File(path));
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
