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

    }

    private List<String[]> parseCsv(String fileToParsePath) {
        List<String[]> res = new ArrayList<>()
        String FILE_TO_PARSE_PATH = "D:/input.csv"
        if (fileToParsePath != null && fileToParsePath != "") { FILE_TO_PARSE_PATH = fileToParsePath; }
        BufferedReader br = new BufferedReader(new FileReader(FILE_TO_PARSE_PATH))
        String line
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",")
            res.add(values)
        }
        return res;
    }
}
