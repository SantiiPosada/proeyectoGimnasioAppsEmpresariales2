package co.com.eam.appsEmpresariales.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import co.com.eam.appsEmpresariales.domain.ClaseImpartir;
import co.com.eam.appsEmpresariales.domain.ClasePuedenImpartirInstructor;
import co.com.eam.appsEmpresariales.repository.ClaseImpartirRepository;
import co.com.eam.appsEmpresariales.service.MessageByLocaleService;

@Controller
public class ClaseImpartirController {
	@Autowired
	MessageByLocaleService messageByLocaleService;

	@Autowired // inyeccion de depedencias. Meter un dao
	private ClaseImpartirRepository claseImpartirRepo;

	@Autowired
	public ClaseImpartirController(ClaseImpartirRepository claseImpartirRepo) {
		this.claseImpartirRepo = claseImpartirRepo;
	}

	@GetMapping("/ListarClaseImpartir")
	public String listarTodasClasesImpartir(Model model) {
		model.addAttribute("claseimpartir", claseImpartirRepo.findAll());
		return "ListaClaseImpartir";
	}

	@GetMapping("/IrAgregarClaseImpartir")
	public String IrAgregarClaseImpartir(ClaseImpartir claseimpartir) {
		return "AgregarClaseImpartir";
	}

	@PostMapping("/AgregarClasesImpartir")
	public String AgregarClaseImpartir(@Valid ClaseImpartir claseimpartir, BindingResult result, Model model,
			RedirectAttributes attribute) {
		if (result.hasErrors()) {
			return "AgregarClaseImpartir";
		}

		ClaseImpartir claseImpartirPorNombre = (ClaseImpartir) claseImpartirRepo
				.buscarClaseImpartirPorNombre(claseimpartir.getNombre());

		if (claseImpartirPorNombre != null) {
			new IllegalArgumentException(
					messageByLocaleService.getMessage("DomainClaseImpartirController.duplicateNameWarning"));
			model.addAttribute("warning",
					messageByLocaleService.getMessage("DomainClaseImpartirController.duplicateNameWarning"));
			return "AgregarClaseImpartir";
		}

		claseImpartirRepo.save(claseimpartir);
		model.addAttribute("claseimpartir", claseImpartirRepo.findAll());
		attribute.addFlashAttribute("success",
				messageByLocaleService.getMessage("DomainClaseImpartirController.savedPartnerSucces"));
		return "redirect:/ListarClaseImpartir";
	}

	@GetMapping("/IrEditarClaseImpartir/{id}")
	public String IrEditarClaseImpartir(@PathVariable("id") Integer id, Model model) {
		ClaseImpartir claseimpartir = claseImpartirRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Id invalido: " + id));
		model.addAttribute("ClasesImpartir", claseimpartir);
		return "EditarClaseImpartir";
	}

	@PostMapping("/EditarClaseImpartir/{id}")
	public String EditarClaseImpartir(@PathVariable("id") Integer id, @Valid ClaseImpartir claseimpartir,
			BindingResult result, Model model, RedirectAttributes attribute) {
		if (result.hasErrors()) {
			claseimpartir.setId(id);
			return "EditarClaseImpartir";
		}

		ClaseImpartir claseImpartirPorNombre = (ClaseImpartir) claseImpartirRepo
				.buscarClaseImpartirPorNombre(claseimpartir.getNombre());

		if (claseImpartirPorNombre != null) {
			new IllegalArgumentException(
					messageByLocaleService.getMessage("DomainClaseImpartirController.duplicateNameWarning"));
			model.addAttribute("warning",
					messageByLocaleService.getMessage("DomainClaseImpartirController.duplicateNameWarning"));
			return "AgregarClaseImpartir";
		}

		claseImpartirRepo.save(claseimpartir);
		model.addAttribute("claseimpartir", claseImpartirRepo.findAll());
		attribute.addFlashAttribute("info",
				messageByLocaleService.getMessage("DomainClaseImpartirController.modifyPartnerSucces"));
		return "redirect:/ListarClaseImpartir";
	}

	@GetMapping("/EliminarClaseImpartir/{id}")
	public String EliminarClaseImpartir(@PathVariable("id") Integer id, Model model, RedirectAttributes attribute) {
		ClaseImpartir claseimpartir = claseImpartirRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Id invalido: " + id));

		ClasePuedenImpartirInstructor claseImpartirPorId = (ClasePuedenImpartirInstructor) claseImpartirRepo
				.buscarClaseImpartirPorId(claseimpartir.getId());

		if (claseImpartirPorId != null) {
			new IllegalArgumentException(
					messageByLocaleService.getMessage("DomainClaseImpartirController.NotRemoveIdWarning"));
			attribute.addFlashAttribute("warning",
					messageByLocaleService.getMessage("DomainClaseImpartirController.NotRemoveIdWarning"));
			return "redirect:/ListarClaseImpartir";
		}
		claseImpartirRepo.delete(claseimpartir);
		model.addAttribute("claseimpartir", claseImpartirRepo.findAll());
		attribute.addFlashAttribute("warning",
				messageByLocaleService.getMessage("DomainClaseImpartirController.removePartnerSucces"));
		return "redirect:/ListarClaseImpartir";
	}

}
