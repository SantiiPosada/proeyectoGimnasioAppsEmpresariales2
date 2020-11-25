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
import co.com.eam.appsEmpresariales.domain.Administrador;
import co.com.eam.appsEmpresariales.domain.Instructor;
import co.com.eam.appsEmpresariales.repository.InstructorRepository;
import co.com.eam.appsEmpresariales.service.MessageByLocaleService;

@Controller
public class InstructorController {
	@Autowired
	MessageByLocaleService messageByLocaleService;
	@Autowired // inyeccion de depedencias. Meter un dao
	private InstructorRepository instructorRepo;

	@Autowired
	private CloudinaryConfig cloudc;

	@Autowired
	public InstructorController(InstructorRepository instructorRepo) {
		this.instructorRepo = instructorRepo;
	}
	// posadita

	@GetMapping("/MenuInstructor")
	public String MenuIntructo() {
		return "Plantilla/MenuInstructor";
	}

	@GetMapping("/ListarInstructores")
	public String listarTodosInstructores(Model model) {
		model.addAttribute("Instructores", instructorRepo.findAll());
		return "ListaInstructores";
	}

	@GetMapping("/IrAgregarInstructor")
	public String IrAgregarInstructor(Instructor instructor) {
		return "AgregarInstructor";
	}

	@PostMapping("/AgregarInstructor")
	public String AgregarInstructor(@Valid Instructor instructor, BindingResult result, Model model,
			@RequestParam("file") MultipartFile file, RedirectAttributes attribute) {

		if (result.hasErrors()) {
			return "AgregarInstructor";
		}

		if (!instructor.getContrasena().equals(instructor.getRepContrasena())) {
			new IllegalArgumentException(messageByLocaleService.getMessage("InstructorController.passwordWarning"));
			model.addAttribute("warning", messageByLocaleService.getMessage("InstructorController.passwordWarning"));
			return "AgregarInstructor";
		}

		Administrador administradorPorCorreo = instructorRepo
				.BuscarAdministradorPorCorreo(instructor.getCorreoelectronico());

		Instructor instrcutorPorCorreo = instructorRepo.BuscarInstrcutorPorCorreo(instructor.getCorreoelectronico());

		if (administradorPorCorreo != null || instrcutorPorCorreo != null) {
			new IllegalArgumentException(messageByLocaleService.getMessage("InstructorController.emailWarning"));
			model.addAttribute("warning", messageByLocaleService.getMessage("InstructorController.emailWarning"));
			return "AgregarSocio";
		}

		Instructor instructorPorCedula = (Instructor) instructorRepo.BuscarInstructorPorCedula(instructor.getCedula());

		if (instructorPorCedula != null) {
			new IllegalArgumentException(
					messageByLocaleService.getMessage("InstructorController.duplicateCeduladWarning"));
			model.addAttribute("warning",
					messageByLocaleService.getMessage("InstructorController.duplicateCeduladWarning"));
			return "AgregarInstructor";
		}
		Instructor instructorPorCorreo = (Instructor) instructorRepo
				.BuscarInstructorPorCorreo(instructor.getCorreoelectronico());
		if (instructorPorCorreo != null) {
			new IllegalArgumentException(
					messageByLocaleService.getMessage("InstructorController.duplicateEmailWarning"));
			model.addAttribute("warning",
					messageByLocaleService.getMessage("InstructorController.duplicateEmailWarning"));
			return "AgregarInstructor";
		}
		try {
			Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
			System.out.println(uploadResult.get("url").toString());
			instructor.setUrlFoto(uploadResult.get("url").toString());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			new IllegalArgumentException(messageByLocaleService.getMessage("InstructorController.pictureWarning"));
			model.addAttribute("warning", messageByLocaleService.getMessage("InstructorController.pictureWarning"));
			return "AgregarInstructor";
		}

		instructorRepo.save(instructor);
		model.addAttribute("Instructores", instructorRepo.findAll());
		attribute.addFlashAttribute("success",
				messageByLocaleService.getMessage("InstructorController.savedPartnerSucces"));
		return "redirect:/ListarInstructores";
	}

	@GetMapping("/IrEditarInstructor/{id}")
	public String irEditarInstructor(@PathVariable("id") Integer id, Model model) {
		Instructor instructor = instructorRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Id invalido: " + id));
		model.addAttribute("instructor", instructor);
		return "EditarInstructor";
	}

	@PostMapping("/EditarInstructor/{id}")
	public String EditarInstructor(@PathVariable("id") Integer id, @Valid Instructor instructor, BindingResult result,
			Model model, @RequestParam("file") MultipartFile file, @RequestParam("cambioUrl") boolean cambioUrl,
			RedirectAttributes attribute) {
		if (result.hasErrors()) {
			instructor.setId(id);
			return "EditarInstructor";
		}

		Instructor instructorPorId = instructorRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("id Invalido: " + id));

		if (!instructor.getContrasena().equals(instructor.getRepContrasena())) {
			new IllegalArgumentException(messageByLocaleService.getMessage("InstructorController.passwordWarning"));
			model.addAttribute("warning", messageByLocaleService.getMessage("InstructorController.passwordWarning"));
			instructor.setId(id);
			return "EditarInstructor";
		}
		if (!instructorPorId.getCedula().equals(instructor.getCedula())) {
			new IllegalArgumentException(
					messageByLocaleService.getMessage("InstructorController.tryChangeCedulaWarning"));
			model.addAttribute("warning",
					messageByLocaleService.getMessage("InstructorController.tryChangeCedulaWarning"));
			instructor.setId(id);
			return "EditarInstructor";
		}

		if (!instructorPorId.getCorreoelectronico().equals(instructor.getCorreoelectronico())) {
			new IllegalArgumentException(
					messageByLocaleService.getMessage("InstructorController.tryChangeEmailWarning"));
			model.addAttribute("warning",
					messageByLocaleService.getMessage("InstructorController.tryChangeEmailWarning"));
			instructor.setId(id);
			return "EditarInstructor";
		}

		if (cambioUrl) {
			try {
				Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
				System.out.println(uploadResult.get("url").toString());
				instructor.setUrlFoto(uploadResult.get("url").toString());
			} catch (Exception e) {
				System.out.println(e.getMessage());
				instructor.setId(id);
				new IllegalArgumentException(messageByLocaleService.getMessage("InstructorController.pictureWarning"));
				model.addAttribute("warning", messageByLocaleService.getMessage("InstructorController.pictureWarning"));
				return "EditarInstructor";
			}
		}

		instructorRepo.save(instructor);
		model.addAttribute("Instructores", instructorRepo.findAll());
		attribute.addFlashAttribute("info",
				messageByLocaleService.getMessage("InstructorController.modifyPartnerSucces"));
		return "redirect:/ListarInstructores";
	}

	@GetMapping("/EliminarInstructor/{id}")
	public String EliminarInstructor(@PathVariable("id") Integer id, Model model, RedirectAttributes attribute) {
		Instructor instructor = instructorRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Id invalido: " + id));
		instructorRepo.delete(instructor);
		model.addAttribute("Instructores", instructorRepo.findAll());
		attribute.addFlashAttribute("warning",
				messageByLocaleService.getMessage("InstructorController.removePartnerSucces"));
		return "redirect:/ListarInstructores";
	}

	@PostMapping("**/InstructorFiltradoCorreo")
	public String InstructorFiltrado(@RequestParam("correoFiltrar") String correoFiltrar, Model model) {
		model.addAttribute("Instructores", instructorRepo.findByCorreoElectronico(correoFiltrar));
		return "ListaInstructores";
	}
}
