package mvc;
import java.util.logging.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class ProyectoApplication implements CommandLineRunner {

	private static final Logger log = Logger.getLogger(ProyectoApplication.class.getName());
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		// Auto-configura l'aplicació
		new SpringApplicationBuilder(ProyectoApplication.class).run(args);
	}

	// Funció principal
	public void run(String... strings) throws Exception {

	}
}
