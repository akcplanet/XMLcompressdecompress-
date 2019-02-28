package org.testing.project.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.testing.project.entity.Nugget;
import org.testing.project.entity.NuggetField;
import org.testing.project.utils.TAR;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import java.util.zip.GZIPOutputStream;

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
				String path = (nugget.getOutputFolderPath() + "/" + timestamp.getTime());
				logger.debug(path);
				for (File file : listOfFiles) {
					String filename = file.getName();
					if (filename.endsWith(".xml") || filename.endsWith(".XML")) {
						DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
						DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
						Document doc = docBuilder.parse(file.getAbsolutePath());

						if (StringUtils.isNotBlank(nugget.getCusip())
								&& doc.getElementsByTagName(NuggetField.CUSIP.nugget()).getLength() > 0)
							doc.getElementsByTagName(NuggetField.CUSIP.nugget()).item(0)
									.setTextContent(nugget.getCusip());

						if (StringUtils.isNotBlank(nugget.getInvnum())
								&& doc.getElementsByTagName(NuggetField.INVNUM.nugget()).getLength() > 0) {
							Integer result = Integer.valueOf(nugget.getInvnum()).intValue();
							result = result + j;
							doc.getElementsByTagName(NuggetField.INVNUM.nugget()).item(0)
									.setTextContent(String.valueOf(result));
						}

						if (StringUtils.isNotBlank(nugget.getPortfoliosPortfolioName()) && doc
								.getElementsByTagName(NuggetField.PORTFOLIOS_PORTFOLIO_NAME.nugget()).getLength() > 0)
							doc.getElementsByTagName(NuggetField.PORTFOLIOS_PORTFOLIO_NAME.nugget()).item(0)
									.setTextContent(nugget.getPortfoliosPortfolioName());

						if (StringUtils.isNotBlank(nugget.getTouchCount())
								&& doc.getElementsByTagName(NuggetField.TOUCH_COUNT.nugget()).getLength() > 0) {
							doc.getElementsByTagName(NuggetField.TOUCH_COUNT.nugget()).item(0)
									.setTextContent(nugget.getTouchCount());
						}
						TransformerFactory transformerFactory = TransformerFactory.newInstance();
						Transformer transformer = transformerFactory.newTransformer();
						DOMSource source = new DOMSource(doc);
						new File(path).mkdirs();
						StreamResult result = new StreamResult(new File(path + "/" + filename));
						transformer.transform(source, result);	
					}
				}
				TAR.compress(path+ ".tar", new File(path));
				TAR.gzipFile(path+ ".tar", path+ ".gzip");
			//	Files.deleteIfExists(Paths.get(path+ ".tar"));
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
