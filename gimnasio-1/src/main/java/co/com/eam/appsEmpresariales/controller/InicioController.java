package co.com.eam.appsEmpresariales.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import co.com.eam.appsEmpresariales.domain.loginForm;

@Controller
public class InicioController {

	@GetMapping("/Inicio")
	public String Inicio() {
		return "Inicio";
	}

	@GetMapping("/Login")
	public String Login(loginForm login) {
		return "Login";
	}

	@GetMapping("/Clases")
	public String Clases() {
		return "Clases";
	}

	@GetMapping("/Conocenos")
	public String Conocenos() {
		return "Conocenos";
	}

	@GetMapping("/Contacto")
	public String Contacto() {
		return "Contacto";
	}

}
