package si.inspirited

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import static org.junit.jupiter.api.Assertions.*

@SpringBootTest
class ConverterApplicationTests {

	@Autowired
	ResourceLoader resourceLoader;

	@Test
	void contextLoads() {
	}

	@Test
	void parseCsv_whenReceivedListIsNotEmpty_thenCorrect() {
		Resource resource = resourceLoader.getResource("file:src/main/resources/csv/input.csv")
		String fileName = resource.getFile().getAbsolutePath()
		ConverterApplication converterApplication = new ConverterApplication();
		List<String[]> receivedList = converterApplication.parseCsv(fileName)

		assertNotNull(receivedList)
		assertNotEquals(0, receivedList.size())
	}

}
