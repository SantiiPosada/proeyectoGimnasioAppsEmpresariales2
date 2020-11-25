package co.com.eam.appsEmpresariales.controller;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import co.com.eam.appsEmpresariales.domain.Clase;
import co.com.eam.appsEmpresariales.domain.ClasePuedenImpartirInstructor;
import co.com.eam.appsEmpresariales.domain.Instructor;
import co.com.eam.appsEmpresariales.domain.Sala;
import co.com.eam.appsEmpresariales.domain.Socio;
import co.com.eam.appsEmpresariales.repository.ClaseRepository;
import co.com.eam.appsEmpresariales.service.MessageByLocaleService;
import co.com.eam.appsEmpresariales.util.UtilConvertidor;

@Controller
public class ClaseController {

	@Autowired
	MessageByLocaleService messageByLocaleService;

	private ClaseRepository claseRepo;

	@Autowired // inyeccion de depedencias. Meter un dao

	public ClaseController(ClaseRepository claseRepo) {
		this.claseRepo = claseRepo;
	}

	@GetMapping("/ListarClase")
	public String ListarClase(Model model) {

		model.addAttribute("clases", claseRepo.findAll());

		return "ListaClase";
	}

	@GetMapping("/IrAgregarClase")
	public String IrAgregarClase(Clase clase, Model model) {
		Iterable<Sala> ListaSala = claseRepo.ListarSala();
		Iterable<ClasePuedenImpartirInstructor> ListaClaseHabilitadas = claseRepo.ListarClaseHabilitadas();
		model.addAttribute("salasOrdenadas", ListaSala);
		model.addAttribute("clasehabilitadasOrdenadas", ListaClaseHabilitadas);
		return "AgregarClase";

	}

	UtilConvertidor utilConvertidor;

	@PostMapping("/AgregarClase")
	public String AgregarClase(@Valid Clase clase, BindingResult result, Model model, RedirectAttributes attribute) {
		Iterable<Sala> ListaSala = claseRepo.ListarSala();
		Iterable<ClasePuedenImpartirInstructor> ListaClaseHabilitadas = claseRepo.ListarClaseHabilitadas();
		if (result.hasErrors()) {
			model.addAttribute("salasOrdenadas", ListaSala);
			model.addAttribute("clasehabilitadasOrdenadas", ListaClaseHabilitadas);
			return "AgregarClase";
		}
		System.out.println("Fecha cadena: " + clase.getFechacadena());
		Date fecha = utilConvertidor.parseFecha(clase.getFechacadena());
		clase.setDiaHora(fecha);
		claseRepo.save(clase);
		model.addAttribute("clases", claseRepo.findAll());
		attribute.addFlashAttribute("success",
				messageByLocaleService.getMessage("DomainClaseController.savedPartnerSucces"));
		return "redirect:/ListarClase";
	}

	@GetMapping("/IrEditarClase/{id}")
	public String IrEditarClase(@PathVariable("id") Integer id, Model model) {
		Clase clase = claseRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Id Invalido: " + id));
		Iterable<ClasePuedenImpartirInstructor> ListaClaseHabilitadas = claseRepo.ListarClaseHabilitadas();
		Iterable<Sala> ListaSala = claseRepo.ListarSala();
		model.addAttribute("salasOrdenadas", ListaSala);
		model.addAttribute("clasehabilitadasOrdenadas", ListaClaseHabilitadas);
		model.addAttribute("clase", clase);
		return "EditarClase";
	}

	@Bean
	public UtilConvertidor utilConvertidor() {
		utilConvertidor = new UtilConvertidor();

		return utilConvertidor;
	}

	@PostMapping("/EditarClase/{id}")
	public String EditarClase(@PathVariable("id") Integer id, @Valid Clase clase, BindingResult result, Model model,
			RedirectAttributes attribute) {
		Iterable<Sala> ListaSala = claseRepo.ListarSala();
		Iterable<ClasePuedenImpartirInstructor> ListaClaseHabilitadas = claseRepo.ListarClaseHabilitadas();
		if (result.hasErrors()) {
			clase.setId(id);

			model.addAttribute("salasOrdenadas", ListaSala);
			model.addAttribute("clasehabilitadasOrdenadas", ListaClaseHabilitadas);
			model.addAttribute("clase", clase);
			return "EditarClase";
		}

		Date fecha = utilConvertidor.parseFecha(clase.getFechacadena());
		clase.setDiaHora(fecha);

		claseRepo.save(clase);
		attribute.addFlashAttribute("success",
				messageByLocaleService.getMessage("DomainClaseController.modifyPartnerSucces"));
		model.addAttribute("clases", claseRepo.findAll());
		return "redirect:/ListarClase";
	}

	@GetMapping("/EliminarClase/{id}")
	public String EliminarClase(@PathVariable("id") Integer id, Model model, RedirectAttributes attribute) {
		Clase clase = claseRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Id Invalido:" + id));
		claseRepo.delete(clase);
		attribute.addFlashAttribute("success",
				messageByLocaleService.getMessage("DomainClaseController.removePartnerSucces"));
		ListarClase(model);
		return "redirect:/ListarClase";
	}

	@PostMapping("**/ClaseFiltradoDia")
	public String ClaseFiltradoDia(@RequestParam("claseFiltrarDia") Date claseFiltrarDia, Model model) {
		model.addAttribute("clases", claseRepo.findByDiaHora(claseFiltrarDia));
		return "ListaClase";
	}

	@PostMapping("**/ClaseFiltradoCorreoInstructor")
	public String ClaseFiltradoCorreoSocio(@RequestParam("claseFiltrarCorreoInstructor") String claseFiltrarCorreoSocio,
			Model model) {
		model.addAttribute("clases", claseRepo.ListarCLasesPorCorreoInstructor(claseFiltrarCorreoSocio));
		return "ListaClase";
	}

	@GetMapping("/buscaSocioPorIdc")
	public String buscaSocioPorId(Socio socio, Model m) {
		Socio socioPorId = (Socio) claseRepo.BuscarSocioPorId(socio.getId());
		m.addAttribute("socio", socioPorId);

		return "AgregarClase";

	}

	@GetMapping("/buscarInstructorPorIdc")
	public String buscarInstructorPorId(Instructor instructor, Model m) {
		Instructor instructorPorId = (Instructor) claseRepo.BuscarInstructorPorId(instructor.getId());
		m.addAttribute("instructor", instructorPorId);

		return "AgregarClase";

	}

	@GetMapping("/buscarClasePorFechac")
	public String buscarClasePorFecha(Date fecha, Model m) {
		m.addAttribute("clases", claseRepo.findByDiaHora(fecha));
		return "AgregarClase";

	}

	@GetMapping("/buscarClaseEntreDosFechasc")
	public String buscarClaseEntreDosFechas(Date fecha1, Date fecha2, Model m) {
		m.addAttribute("clases", claseRepo.findByDiaHoraBetween(fecha1, fecha2));
		return "AgregarClase";

	}

	@GetMapping("/LisarClasesPorTipoSalac")
	public String LisarClasesPorTipoSala(String tipoSala, Model m) {

		m.addAttribute("clases", claseRepo.LisarClasesPorTipoSala(tipoSala));

		return "AgregarClase";

	}

	@GetMapping("/LisarClasesPorCedulaInstructorc")
	public String LisarClasesPorCedulaInstructor(String cedulaInstructor, Model m) {

		m.addAttribute("clases", claseRepo.ListarCLasesPorCedulaInstructor(cedulaInstructor));

		return "AgregarClase";

	}

}
