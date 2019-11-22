package si.inspirited

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.w3c.dom.Attr
import org.w3c.dom.Document
import org.w3c.dom.Element
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import java.time.LocalDateTime

@SpringBootApplication
class ConverterApplication implements CommandLineRunner{

    String[] SUB_NODES = "name description vendor price features".split()
    String[] SEMANTIC_UTILS = "id code current".split()
    String DEFAULT_DEST_PATH_TO_STORE_XML = "D:/result.xml"
    String ROOT_ELEMENT = "products"

	static void main(String[] args) {
		SpringApplication.run(ConverterApplication, args)
	}

	@Override
	void run(String... args) throws Exception {
        if (args.length < 1) {
            println LocalDateTime.now().toString() + " WARN restart app with at least one necessary input param (required: path to .csv source file to parse)"
            println "optional is second param (destination path to file, where result xml document should be stored, ex: D:/result.xml), else default dest. path is: " + DEFAULT_DEST_PATH_TO_STORE_XML
        }
        else if (args.length > 0) {
            List receivedData = parseCsv(args[0])
            Document structuredAsXml = getXml(receivedData, ROOT_ELEMENT)
            persistXmlAsFile(structuredAsXml, args.length > 1 ? args[1] : DEFAULT_DEST_PATH_TO_STORE_XML)
        }
    }

    def List<String[]> parseCsv(String fileToParsePath) {
        List<String[]> res = new ArrayList<>()
        if (fileToParsePath != null && fileToParsePath != "") {
            BufferedReader br = new BufferedReader(new FileReader(fileToParsePath))
            String line
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",\"")
                res.add(values)
            }
        }
        return res;
    }

    def Document getXml(List<String[]> source, String root) {   //root:products
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance()
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder()
        Document res = docBuilder.newDocument()

        String[] headers = source.get(0);
        source.remove(0)

        Element rootElement = res.createElement(root)
        res.appendChild(rootElement) as Document

        for (String[] node : source) {
            Element staff = res.createElement(root.substring(0, root.length() - 1))
            rootElement.appendChild(staff) as Document

            Attr attr = res.createAttribute(SEMANTIC_UTILS[0])
            attr.setValue(node[0])
            staff.setAttributeNode(attr) as Document

                for (int i = 0; i < 2; i++) {
                    Element subStaff = res.createElement(SUB_NODES[i])
                    subStaff.appendChild(res.createTextNode(node[i + 5].replace("\"", "")))
                    staff.appendChild(subStaff)
                }

                Element vendorStaff = res.createElement(SUB_NODES[2])
                staff.appendChild(vendorStaff)
                Attr vendorCodeAttr = res.createAttribute(SEMANTIC_UTILS[1])
                vendorCodeAttr.setValue(node[1].replace("\"", ""))
                vendorStaff.setAttributeNode(vendorCodeAttr) as Document
                vendorStaff.appendChild(res.createTextNode(node[2].replace("\"", "")))

                Element priceStaff = res.createElement(SUB_NODES[3])
                staff.appendChild(priceStaff)
                Attr priceAttr = res.createAttribute(SEMANTIC_UTILS[2])
                priceAttr.setValue(node[7].replace("\"", ""))
                priceStaff.setAttributeNode(priceAttr) as Document

                Element featuresStaff = res.createElement(SUB_NODES[4])
                staff.appendChild(featuresStaff)

                    for (int i = 0; i < 2; i++) {
                        Element featureSubStaff = res.createElement(SUB_NODES[4].substring(0, SUB_NODES[4].length() - 1))
                        Attr featureNameAttr = res.createAttribute(SUB_NODES[0])
                        featureNameAttr.setValue(headers[i + 3].replace("\"", ""))
                        featureSubStaff.setAttributeNode(featureNameAttr) as Document
                        featureSubStaff.appendChild(res.createTextNode(node[i + 3].replace("\"", "")))
                        featuresStaff.appendChild(featureSubStaff)
                    }
        }
        return res
    }

    def persistXmlAsFile(Document documentToBeSaved, String targetPath) {
        if  (documentToBeSaved != null && targetPath != null && targetPath != "") {
            TransformerFactory transformerFactory = TransformerFactory.newInstance()
            Transformer transformer = transformerFactory.newTransformer()
            DOMSource src = new DOMSource(documentToBeSaved)

            StreamResult result = new StreamResult(new File(targetPath))
            transformer.transform(src, result)
        }
    }
}
