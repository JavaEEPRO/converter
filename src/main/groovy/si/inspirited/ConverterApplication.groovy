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
}
