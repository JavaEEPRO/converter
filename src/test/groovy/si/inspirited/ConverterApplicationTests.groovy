package si.inspirited

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ResourceLoader
import org.w3c.dom.Document
import static org.junit.jupiter.api.Assertions.*

@SpringBootTest
class ConverterApplicationTests {

	@Autowired
	ResourceLoader resourceLoader

	String ROOT_ELEMENT = "products"
	String PATH_TO_CSV_FILE_TO_BE_PARSED

	@Test
	void contextLoads() {
	}

	@Test
	void parseCsv_whenReceivedListIsNotEmpty_thenCorrect() {
		PATH_TO_CSV_FILE_TO_BE_PARSED = resourceLoader.getResource('file:src/main/resources/csv/input.csv').getFile().getAbsolutePath()
		ConverterApplication converterApplication = new ConverterApplication()
		List<String[]> receivedList = converterApplication.parseCsv(PATH_TO_CSV_FILE_TO_BE_PARSED)

		assertNotNull(receivedList)
		assertNotEquals(0, receivedList.size())
	}

	@Test
	void getXml_whenReceivedDocumentIsNotEmpty_thenCorrect() {
		PATH_TO_CSV_FILE_TO_BE_PARSED = resourceLoader.getResource('file:src/main/resources/csv/input.csv').getFile().getAbsolutePath()
		ConverterApplication converterApplication = new ConverterApplication();
		Document returnedDocument = converterApplication.getXml(converterApplication.parseCsv(PATH_TO_CSV_FILE_TO_BE_PARSED), ROOT_ELEMENT)

		assertNotNull(returnedDocument);
	}

	@Test
	void persistXmlAsFile_whenSavedFileExists_thenCorrect() {
		PATH_TO_CSV_FILE_TO_BE_PARSED = resourceLoader.getResource('file:src/main/resources/csv/input.csv').getFile().getAbsolutePath()
		String PATH_WHERE_XML_EXPECTED_TO_BE_SAVED = PATH_TO_CSV_FILE_TO_BE_PARSED.replace("\\csv\\input.csv", "\\xml\\result.xml")
		ConverterApplication converterApplication = new ConverterApplication()
		converterApplication.persistXmlAsFile(converterApplication.getXml(converterApplication.parseCsv(PATH_TO_CSV_FILE_TO_BE_PARSED), ROOT_ELEMENT), PATH_WHERE_XML_EXPECTED_TO_BE_SAVED)
		File resultXml = new File(PATH_WHERE_XML_EXPECTED_TO_BE_SAVED);

		assertTrue(resultXml.exists())
		//assertTrue(resultXml.delete())
	}
}
