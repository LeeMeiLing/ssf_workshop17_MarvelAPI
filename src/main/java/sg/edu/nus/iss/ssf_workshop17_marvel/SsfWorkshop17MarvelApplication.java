package sg.edu.nus.iss.ssf_workshop17_marvel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import sg.edu.nus.iss.ssf_workshop17_marvel.services.CharacterService;

@SpringBootApplication
public class SsfWorkshop17MarvelApplication implements CommandLineRunner{

	@Autowired
	private CharacterService charSvc;

	public static void main(String[] args) {
		SpringApplication.run(SsfWorkshop17MarvelApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		// load all characters available from marvel API
		charSvc.loadAllCharacters();
	}

}
