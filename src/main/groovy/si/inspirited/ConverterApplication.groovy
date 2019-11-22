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
        if (fileToParsePath != null && fileToParsePath != "") {
            BufferedReader br = new BufferedReader(new FileReader(fileToParsePath))
            String line
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",")
                res.add(values)
            }
        }
        return res;
    }
}
