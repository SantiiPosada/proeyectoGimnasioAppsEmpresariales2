package co.com.eam.appsEmpresariales.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import co.com.eam.appsEmpresariales.domain.Clase;
import co.com.eam.appsEmpresariales.domain.Socio;
import co.com.eam.appsEmpresariales.domain.SocioClase;

import co.com.eam.appsEmpresariales.repository.SocioClaseRepository;


@Controller
public class SocioClaseController {

	//hola...
	private SocioClaseRepository socioClaseRepo;
	@Autowired// inyeccion de depedencias. Meter un dao
	
	public SocioClaseController(SocioClaseRepository socioClaseRepo) {
		this.socioClaseRepo = socioClaseRepo;
	}
	
	@GetMapping("/ListarSocioClase")
	public String ListarSocioClase(Model model) {
		model.addAttribute("socioClases", socioClaseRepo.findAll());
		return "ListaSocioClase";
	}

	@GetMapping("/IrAgregarSocioCLase")
	public String IrAgregarSocioCLase(SocioClase socioClase) {
		return "AgregarSocioCLase";
	}

	@PostMapping("/AgregarSocioCLase")
	public String AgregarSocioCLase(@Valid SocioClase socioClase, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "AgregarSocioCLase";
		}

		socioClaseRepo.save(socioClase);
		ListarSocioClase(model);
		return "ListaSocioClase";
	}

	@GetMapping("/IrEditarSocioClase/{id}")
	public String IrEditarSocioClase(@PathVariable("id") Integer id, Model model) {
		SocioClase socioClase = socioClaseRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Id Invalido: " + id));
		model.addAttribute("socioClase", socioClase);
		return "EditarSocioClase";
	}

	@PostMapping("/EditarSocioClase/{id}")
	public String EditarSocioClase(@PathVariable("id") Integer id, @Valid SocioClase socioClase, BindingResult result, Model model) {
		if (result.hasErrors()) {
			socioClase.setId(id);
			return "EditarSocioClase";
		}

		socioClaseRepo.save(socioClase);
		ListarSocioClase(model);
		return "ListaSocioClase";
	}

	@GetMapping("/EliminarSocioClase/{id}")
	public String EliminarSocioClase(@PathVariable("id") Integer id, Model model) {
		SocioClase socioClase = socioClaseRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Id Invalido:" + id));
		socioClaseRepo.delete(socioClase);
		ListarSocioClase(model);
		return "ListaSocioClase";
	}
	
	@GetMapping("/buscaSocioPorIdsc")
	public String buscaSocioPorId( Socio socio,Model m) {
		Socio socioPorId = (Socio) socioClaseRepo
				.BuscarSocioPorId(socio.getId());
		m.addAttribute("socio", socioPorId);
		
		return "AgregarSocioCLase";
		
	}
	
	@GetMapping("/BuscarClasePorIdc")
	public String BuscarClasePorId( Clase clase,Model m) {
		Clase clasePorId = (Clase) socioClaseRepo
				.BuscarClasePorId(clase.getId());
		m.addAttribute("clase", clasePorId);
		
		return "AgregarSocioCLase";
		
	}
	
	
	
	@GetMapping("/ListarSocioClasesPorTipoSalasc")
	public String ListarSocioClasesPorTipoSalasc( String nombre,Model m) {
		
		m.addAttribute("sociosClases", socioClaseRepo.ListarSocioClasesPorTipoSala(nombre));
		
		return "AgregarSocioCLase";
		
	}
	
	@GetMapping("/ListarSocioClasesPorCedulaSocio")
	public String ListarSocioClasesPorCedulaSocio( String cedula,Model m) {
		
		m.addAttribute("sociosClases", socioClaseRepo.ListarSocioClasesPorCedulaSocio(cedula));
		
		return "AgregarSocioCLase";
		
	}
	
	
	
	
}
