package co.com.eam.appsEmpresariales.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import co.com.eam.appsEmpresariales.domain.ClaseImpartir;
import co.com.eam.appsEmpresariales.domain.ClasePuedenImpartirInstructor;
import co.com.eam.appsEmpresariales.domain.Instructor;
import co.com.eam.appsEmpresariales.repository.ClasePuedenImpartirInstructorRepository;
import co.com.eam.appsEmpresariales.service.MessageByLocaleService;

@Controller
public class ClasePuedenImpartirInstructorController {
	
	@Autowired
    MessageByLocaleService messageByLocaleService;
	
	@Autowired// inyeccion de depedencias. Meter un dao
	private ClasePuedenImpartirInstructorRepository ClasePuedenImpartirInstructorRepo;
	
	@Autowired
	public ClasePuedenImpartirInstructorController(ClasePuedenImpartirInstructorRepository ClasePuedenImpartirInstructorRepo) {
		this.ClasePuedenImpartirInstructorRepo = ClasePuedenImpartirInstructorRepo;
	}
	
	
	@GetMapping("/listaTodasClasePuedenImpartirInstructor")
	public String listaTodasClasePuedenImpartirInstructor(Model model){
		model.addAttribute("clasesPuedenImpartirInstructor", ClasePuedenImpartirInstructorRepo.ListarClasePuedenImpartirInstructorOrdenadoInstructor());
		return "ListarClasePuedenImpartirInstructor";
		
	}
	
	
	

	@GetMapping("/IrAgregarClasePuedenImpartirInstructor")
	public String IrAgregarClase(ClasePuedenImpartirInstructor clasePuedeImpartirInstructor,Model model) {
		Iterable<Instructor> ListaInstructores = ClasePuedenImpartirInstructorRepo.ListarInstructores();
		Iterable<ClaseImpartir> ListaClasesImpartir = ClasePuedenImpartirInstructorRepo.ListarClasesImpartir();
		model.addAttribute("istructoresOrdenados", ListaInstructores);
		model.addAttribute("clasesImpartirOrdenadas", ListaClasesImpartir);
		return "AgregarClasePuedenImpartirInstructor";
	}

	@PostMapping("/AgregarClasePuedenImpartirInstructor")
	public String AgregarClasePuedeImaprtir(@Valid ClasePuedenImpartirInstructor clasePuedeImpartirInstructor, BindingResult result, Model model,RedirectAttributes attribute) {
		Iterable<Instructor> ListaInstructores = ClasePuedenImpartirInstructorRepo.ListarInstructores();
		Iterable<ClaseImpartir> ListaClasesImpartir = ClasePuedenImpartirInstructorRepo.ListarClasesImpartir();
		if (result.hasErrors()) {
			
			model.addAttribute("istructoresOrdenados", ListaInstructores);
			model.addAttribute("clasesImpartirOrdenadas", ListaClasesImpartir);
			return "AgregarClasePuedenImpartirInstructor";
		}
	
		ClaseImpartir claseImpartirPorId=ClasePuedenImpartirInstructorRepo.BuscarClaseImpartirPorId(clasePuedeImpartirInstructor.getClaseImpartir().getId());
		Instructor instrcutorPorId=ClasePuedenImpartirInstructorRepo.BuscarInstructorPorId(clasePuedeImpartirInstructor.getInstructor().getId());
		
		if(claseImpartirPorId==null || instrcutorPorId==null) {
			new IllegalArgumentException("Intento de saboteo");
			model.addAttribute("istructoresOrdenados", ListaInstructores);
			model.addAttribute("clasesImpartirOrdenadas", ListaClasesImpartir);
			model.addAttribute("warning",messageByLocaleService.getMessage("CpiiController.sabotage"));
			return "AgregarClasePuedenImpartirInstructor";
		}
		
	
		
		ClasePuedenImpartirInstructor claseQuePuede = ClasePuedenImpartirInstructorRepo.validarNoRepetido(clasePuedeImpartirInstructor.getClaseImpartir().getId(), clasePuedeImpartirInstructor.getInstructor().getId());
		
		if(claseQuePuede!=null) {
			new IllegalArgumentException("{CpiiController.warningitc}");
			model.addAttribute("istructoresOrdenados", ListaInstructores);
			model.addAttribute("clasesImpartirOrdenadas", ListaClasesImpartir);
			model.addAttribute("warning", messageByLocaleService.getMessage("CpiiController.warningitc"));
			return "AgregarClasePuedenImpartirInstructor";
		}

		ClasePuedenImpartirInstructorRepo.save(clasePuedeImpartirInstructor);
		model.addAttribute("clasesPuedenImpartirInstructor", ClasePuedenImpartirInstructorRepo.ListarClasePuedenImpartirInstructorOrdenadoInstructor());
		attribute.addFlashAttribute("success", messageByLocaleService.getMessage("CpiiController.saved"));
		return "redirect:/listaTodasClasePuedenImpartirInstructor";
	}

	@GetMapping("/IrEditarClasePuedenImpartirInstructor/{id}")
	public String IrEditarClasePuedenImpartirInstructor(@PathVariable("id") Integer id, Model model) {
		ClasePuedenImpartirInstructor clasePuedeImpartirInstructor = ClasePuedenImpartirInstructorRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Id Invalido: " + id));
		model.addAttribute("clasePuedeImpartirInstructor", clasePuedeImpartirInstructor);
		return "EditarClasePuedenImpartirInstructor";
	}

	@PostMapping("/EditarClasePuedenImpartirInstructor/{id}")
	public String EditarClasePuedenImpartirInstructor(@PathVariable("id") Integer id, @Valid ClasePuedenImpartirInstructor clasePuedeImpartirInstructor, BindingResult result, Model model) {
		if (result.hasErrors()) {
			clasePuedeImpartirInstructor.setId(id);
			return "EditarClasePuedenImpartirInstructor";
		}

		ClasePuedenImpartirInstructorRepo.save(clasePuedeImpartirInstructor);
		listaTodasClasePuedenImpartirInstructor(model);
		return "ListarClasePuedeImpartirInstructor";
	}

	@GetMapping("/EliminarClasePuedenImpartirInstructor/{id}")
	public String EliminarClasePuedenImpartirInstructor(@PathVariable("id") Integer id, Model model,RedirectAttributes attribute) {
		ClasePuedenImpartirInstructor clasePuedeImpartirInstructor = ClasePuedenImpartirInstructorRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Id Invalido:" + id));
		ClasePuedenImpartirInstructorRepo.delete(clasePuedeImpartirInstructor);
		model.addAttribute("clasesPuedenImpartirInstructor", ClasePuedenImpartirInstructorRepo.ListarClasePuedenImpartirInstructorOrdenadoInstructor());
		attribute.addFlashAttribute("warning", messageByLocaleService.getMessage("CpiiController.remove"));
		return "redirect:/listaTodasClasePuedenImpartirInstructor";
	}
	
	

	@PostMapping("**/instructorFiltradoCorreo")
	public String instructorFiltradoCorreo(ClasePuedenImpartirInstructor clasePuedeImpartirInstructor,@RequestParam("correoFiltrar")String correoFiltrar,Model model) {
	
		Iterable<ClaseImpartir> ListaClasesImpartir = ClasePuedenImpartirInstructorRepo.ListarClasesImpartir();
		model.addAttribute("istructoresOrdenados", ClasePuedenImpartirInstructorRepo.BuscarInstructorPorCorreoElectronico(correoFiltrar));
		model.addAttribute("clasesImpartirOrdenadas", ListaClasesImpartir);

		return "AgregarClasePuedenImpartirInstructor";
	}

	
	
}
