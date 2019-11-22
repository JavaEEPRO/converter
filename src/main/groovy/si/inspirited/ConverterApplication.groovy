package si.inspirited

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class ConverterApplication implements CommandLineRunner{

	static void main(String[] args) {
		SpringApplication.run(ConverterApplication, args)
	}

	@Override
	void run(String... args) throws Exception {
		String FILE_TO_PARSE_PATH = "D:/input.csv"
		if (args.length > 0) { FILE_TO_PARSE_PATH = args[0]; }
		List<String> records = new ArrayList<>()
		BufferedReader br = new BufferedReader(new FileReader(FILE_TO_PARSE_PATH))
			String line
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",")
				records.add(Arrays.asList(values))
			}
	}
}
