package co.com.eam.appsEmpresariales.controller;



import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import co.com.eam.appsEmpresariales.domain.loginForm;
import co.com.eam.appsEmpresariales.repository.InstructorRepository;
import co.com.eam.appsEmpresariales.domain.Instructor;


public class LoginController {
	
	@Autowired
	InstructorRepository instructorRepo;
	
//	@GetMapping("/ListarLogin")
//	public String irLogin(loginForm login) {
//		return "Login";
//	}
//	
//	@PostMapping("/logearse")
//	public String login(@Valid loginForm login, BindingResult result, Model model) {
//		if (result.hasErrors()) {
//			return "logearse";
//		}
//		
//		Instructor instructorLogin=(Instructor) instructorRepo.loginInstructor(login.getCorreo(), login.getContrasena());
//		
//		if(instructorLogin==null) {
//			model.addAttribute("warning", "Correo o Contraseña erroneos");
//			return "logearse"; 
//		}
//		
//		
//		return "MenuInstructor";
//		
//	}
//	
//	@GetMapping("/IrMenuInstructor/{correo}")
//	public String irMenuInstructor(@PathVariable("correo") String correo, Model model) {
//		Instructor instructor = instructorRepo.BuscarInstructorPorCorreo(correo);
//		model.addAttribute("instructor", instructor);
//		return "MenuInstructor";
//	}
//	
	
	
}
