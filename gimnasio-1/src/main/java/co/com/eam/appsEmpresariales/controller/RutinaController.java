package co.com.eam.appsEmpresariales.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cloudinary.utils.ObjectUtils;

import co.com.eam.appsEmpresariales.CloudinaryConfig;
import co.com.eam.appsEmpresariales.domain.Instructor;
import co.com.eam.appsEmpresariales.domain.Rutina;
import co.com.eam.appsEmpresariales.domain.Socio;
import co.com.eam.appsEmpresariales.repository.RutinaRepository;
import co.com.eam.appsEmpresariales.service.MessageByLocaleService;

@Controller
public class RutinaController {
	@Autowired // inyeccion de depedencias. Meter un dao
	private RutinaRepository rutinaRepo;

	@Autowired
	private CloudinaryConfig cloudc;

	@Autowired
    MessageByLocaleService messageByLocaleService;
	
	@Autowired
	public RutinaController(RutinaRepository rutinaRepo) {
		this.rutinaRepo = rutinaRepo;
	}

	@GetMapping("/ListarRutina")
	public String listarTodasRutinas(Model model) {
		model.addAttribute("Rutinas", rutinaRepo.ListarRutinasOrdenadaPorSocio());

		return "ListaRutinas";
	}

	@GetMapping("/IrAgregarRutina")
	public String IrAgregarRutina(Rutina rutina, Model model) {
		Iterable<Socio> ListaSociosOrdenados = rutinaRepo.ListarSociosOrdenados();
		Iterable<Instructor> ListaIntructoresOrdenados = rutinaRepo.ListarInstructoresOrdenados();

		model.addAttribute("sociosOrdenados", ListaSociosOrdenados);
		model.addAttribute("istructoresOrdenados", ListaIntructoresOrdenados);
		return "AgregarRutina";
	}

	@PostMapping("/AgregarRutina")
	public String AgregarRutina(@Valid Rutina rutina, BindingResult result, Model model,
			@RequestParam("file") MultipartFile file, RedirectAttributes attribute) {
		Iterable<Socio> ListaSociosOrdenados = rutinaRepo.ListarSociosOrdenados();
		Iterable<Instructor> ListaIntructoresOrdenados = rutinaRepo.ListarInstructoresOrdenados();

		if (result.hasErrors()) {
			model.addAttribute("sociosOrdenados", ListaSociosOrdenados);
			model.addAttribute("istructoresOrdenados", ListaIntructoresOrdenados);
			return "AgregarRutina";
		}

		try {
			Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
			System.out.println(uploadResult.get("url").toString());
			rutina.setUrlFoto(uploadResult.get("url").toString());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			new IllegalArgumentException(messageByLocaleService.getMessage("RutinaController.pictureWarning"));
			model.addAttribute("warning",messageByLocaleService.getMessage("RutinaController.pictureWarning"));
			model.addAttribute("sociosOrdenados", ListaSociosOrdenados);
			model.addAttribute("istructoresOrdenados", ListaIntructoresOrdenados);
			return "AgregarRutina";
		}
	

		rutinaRepo.save(rutina);
		model.addAttribute("Rutinas", rutinaRepo.ListarRutinasOrdenadaPorSocio());
		attribute.addFlashAttribute("success",messageByLocaleService.getMessage("RutinaController.savedRoutineSucces"));
		return "redirect:/ListarRutina";
	}

	@GetMapping("/IrEditarRutina/{id}")
	public String irEditarRutina(@PathVariable("id") Integer id, Model model) {
		Rutina rutina = rutinaRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Id invalido: " + id));
		model.addAttribute("rutina", rutina);
		Iterable<Socio> ListaSociosOrdenados = rutinaRepo.ListarSociosOrdenados();
		Iterable<Instructor> ListaIntructoresOrdenados = rutinaRepo.ListarInstructoresOrdenados();

		model.addAttribute("sociosOrdenados", ListaSociosOrdenados);
		model.addAttribute("istructoresOrdenados", ListaIntructoresOrdenados);
		return "EditarRutina";
	}

	@PostMapping("/EditarRutina/{id}")
	public String EditarRutina(@PathVariable("id") Integer id, @Valid Rutina rutina, BindingResult result, Model model,
			@RequestParam("file") MultipartFile file, @RequestParam("cambioUrl") boolean cambioUrl,
			RedirectAttributes attribute) {
	
		if (result.hasErrors()) {
			rutina.setId(id);
			
			return "EditarRutina";
		}
		if (cambioUrl) {
			try {
				Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
				System.out.println(uploadResult.get("url").toString());
				rutina.setUrlFoto(uploadResult.get("url").toString());

			} catch (Exception e) {
				System.out.println(e.getMessage());
				new IllegalArgumentException(messageByLocaleService.getMessage("RutinaController.pictureWarning"));
				model.addAttribute("warning",messageByLocaleService.getMessage("RutinaController.pictureWarning"));
				rutina.setId(id);
				return "EditarRutina";
			}
		}
		rutinaRepo.save(rutina);
		model.addAttribute("Rutinas", rutinaRepo.ListarRutinasOrdenadaPorSocio());
		attribute.addFlashAttribute("info", messageByLocaleService.getMessage("RutinaController.modifyRoutineSucces") );
		return "redirect:/ListarRutina";
	}

	@GetMapping("/EliminarRutina/{id}")
	public String EliminarRutina(@PathVariable("id") Integer id, Model model, RedirectAttributes attribute) {
		Rutina rutina = rutinaRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Id invalido: " + id));
		rutinaRepo.delete(rutina);
		model.addAttribute("Rutinas", rutinaRepo.ListarRutinasOrdenadaPorSocio());
		attribute.addFlashAttribute("warning",  messageByLocaleService.getMessage("RutinaController.removeRoutineSucces"));
		return "redirect:/ListarRutina";
	}
	
	@PostMapping("**/rutinaFiltradoCorreo")
	public String SocioFiltrado(@RequestParam("correoFiltrar")String correoFiltrar,Model model) {
		model.addAttribute("Rutinas", rutinaRepo.findByCorreoElectronicoSocio(correoFiltrar));
		return "ListaRutinas";
	}
	
	@PostMapping("**/socioFiltradoCorreo")
	public String socioFiltradoCorreo(Rutina rutina,@RequestParam("correoFiltrar")String correoFiltrar,Model model) {
	
		Iterable<Instructor> ListaIntructoresOrdenados = rutinaRepo.ListarInstructoresOrdenados();
		model.addAttribute("sociosOrdenados", rutinaRepo.BuscarSocioPorCorreo(correoFiltrar));
		model.addAttribute("istructoresOrdenados", ListaIntructoresOrdenados);

		return "AgregarRutina";
	}
	
	@GetMapping("/ListarRutinasSocio")
	public String ListarRutinasSocio(Model model) {
	
		return "ListarRutinasSocios";
	}
	
	@PostMapping("**/rutinaFiltradoCorreoContra")
	public String RutinaFiltradCorreoContra(@RequestParam("correoFiltrar")String correoFiltrar,@RequestParam("contraFiltrar")String contraFiltrar,Model model) {
		model.addAttribute("Rutinas", rutinaRepo.findByCorreoElectronicoSocioContra(correoFiltrar,contraFiltrar));
		return "ListarRutinasSocios";
	}
}
