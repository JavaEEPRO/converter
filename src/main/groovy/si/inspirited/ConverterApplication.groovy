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

@SpringBootApplication
class ConverterApplication implements CommandLineRunner{

	static void main(String[] args) {
		SpringApplication.run(ConverterApplication, args)
	}

	@Override
	void run(String... args) throws Exception {
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

        Document doc = docBuilder.newDocument()

        String[] headers = source.get(0);
        source.remove(0)

        String[] subNodes = "name description vendor price features".split()

        Element rootElement = doc.createElement(root)
        doc.appendChild(rootElement) as Document

        for (String[] node : source) {
            Element staff = doc.createElement(root.substring(0, root.length() - 1))
            rootElement.appendChild(staff) as Document

            Attr attr = doc.createAttribute("id")
            attr.setValue(node[0])
            staff.setAttributeNode(attr) as Document

                for (int i = 0; i < 2; i++) {
                    Element subStaff = doc.createElement(subNodes[i])
                    subStaff.appendChild(doc.createTextNode(node[i + 5]))
                    staff.appendChild(subStaff)
                }

                Element vendorStaff = doc.createElement(subNodes[2])
                staff.appendChild(vendorStaff)
                Attr vendorCodeAttr = doc.createAttribute("code")
                vendorCodeAttr.setValue(node[1])
                vendorStaff.setAttributeNode(vendorCodeAttr) as Document
                vendorStaff.appendChild(doc.createTextNode(node[2]))

                Element priceStaff = doc.createElement(subNodes[3])
                staff.appendChild(priceStaff)
                Attr priceAttr = doc.createAttribute("current")
                priceAttr.setValue(node[7])
                priceStaff.setAttributeNode(priceAttr) as Document

                Element featuresStaff = doc.createElement(subNodes[4])
                staff.appendChild(featuresStaff)

                    for (int i = 0; i < 2; i++) {
                        Element featureSubStaff = doc.createElement(subNodes[4].substring(0, subNodes[4].length() - 1))
                        Attr featureNameAttr = doc.createAttribute(subNodes[0])
                        featureNameAttr.setValue(headers[i + 3])
                        featureSubStaff.setAttributeNode(featureNameAttr) as Document
                        featureSubStaff.appendChild(doc.createTextNode(node[i + 3]))
                        featuresStaff.appendChild(featureSubStaff)
                    }
        }
        return doc
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
