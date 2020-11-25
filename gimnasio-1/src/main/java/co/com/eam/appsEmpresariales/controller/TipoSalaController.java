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

import co.com.eam.appsEmpresariales.domain.TipoSala;

import co.com.eam.appsEmpresariales.repository.TipoSalaRepository;
import co.com.eam.appsEmpresariales.service.MessageByLocaleService;

@Controller
public class TipoSalaController {
	
	@Autowired
    MessageByLocaleService messageByLocaleService;
	
	
	@Autowired// inyeccion de depedencias. Meter un dao
	private TipoSalaRepository tipoSalaRepo;

	@Autowired // inyeccion de depedencias. Meter un dao
	public TipoSalaController(TipoSalaRepository tipoSalaRepo) {
		this.tipoSalaRepo = tipoSalaRepo;
	}

	@GetMapping("/ListarTipoSala")
	public String ListarTipoSala(Model model) {
		model.addAttribute("tipoSalas", tipoSalaRepo.findAll());
		return "ListaTipoSala";
	}

	@GetMapping("/IrAgregarTipoSala")
	public String IrAgregarTipoSala(TipoSala tipoSala) {
		return "AgregarTipoSala";
	}

	@PostMapping("/AgregarTipoSala")
	public String AgregarTipoSala(@Valid TipoSala tipoSala, BindingResult result, Model model,
			RedirectAttributes attribute) {
		if (result.hasErrors()) {

			return "AgregarTipoSala";
		}
		TipoSala TiposalaPorNombre = (TipoSala) tipoSalaRepo.findByNombre(tipoSala.getNombre());
		if (TiposalaPorNombre != null) {
			new IllegalArgumentException( messageByLocaleService.getMessage("TipoSalaController.duplicateNameWarning"));
			model.addAttribute("warning", messageByLocaleService.getMessage("TipoSalaController.duplicateNameWarning"));
			return "AgregarTipoSala";
		}

		tipoSalaRepo.save(tipoSala);
		model.addAttribute("tipoSalas", tipoSalaRepo.findAll());
		attribute.addFlashAttribute("success",  messageByLocaleService.getMessage("TipoSalaController.savedRoomTypeSucces"));
		return "redirect:/ListarTipoSala";
	}

	@GetMapping("/IrEditarTipoSala/{id}")
	public String IrEditarTipoSala(@PathVariable("id") Integer id, Model model) {
		TipoSala tipoSala = tipoSalaRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Id Invalido: " + id));
		model.addAttribute("tipoSala", tipoSala);
		return "EditarTipoSala";
	}

	@PostMapping("/EditarTipoSala/{id}")
	public String EditarTipoSala(@PathVariable("id") Integer id, @Valid TipoSala tipoSala, BindingResult result,
			Model model, RedirectAttributes attribute) {
		if (result.hasErrors()) {
			tipoSala.setId(id);
			return "EditarTipoSala";
		}
		TipoSala tipoSalaAnterior = (TipoSala) tipoSalaRepo.buscarTipoSalaId(id);

		if (tipoSalaAnterior.getNombre().equals(tipoSala.getNombre())) {

		} else {
			TipoSala TiposalaPorNombre = (TipoSala) tipoSalaRepo.findByNombre(tipoSala.getNombre());
			if (TiposalaPorNombre != null) {
				new IllegalArgumentException(messageByLocaleService.getMessage("TipoSalaController.duplicateNameWarning"));
				model.addAttribute("warning",messageByLocaleService.getMessage("TipoSalaController.duplicateNameWarning"));
				tipoSala.setId(id);
				
				return "EditarTipoSala";
			}
		}

		tipoSalaRepo.save(tipoSala);
		model.addAttribute("tipoSalas", tipoSalaRepo.findAll());
		attribute.addFlashAttribute("info", messageByLocaleService.getMessage("TipoSalaController.modifyRoomTypeSucces"));
		return "redirect:/ListarTipoSala";
	}

	@GetMapping("/EliminarTipoSala/{id}")
	public String EliminarTipoSala(@PathVariable("id") Integer id, Model model, RedirectAttributes attribute) {
		TipoSala tipoSala = tipoSalaRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Id Invalido:" + id));
		tipoSalaRepo.delete(tipoSala);
		model.addAttribute("tipoSalas", tipoSalaRepo.findAll());
		attribute.addFlashAttribute("warning", messageByLocaleService.getMessage("TipoSalaController.RemoveRoomTypeSucces"));
		return "redirect:/ListarTipoSala";
	}

	@GetMapping("/BuscarSalaNombreTiposada")
	public String BuscarSalaNombreTiposada(Model model, TipoSala tipoSala) {
		model.addAttribute("sala", tipoSalaRepo.BuscarSalaNombreTiposada(tipoSala.getNombre()));
		return "AgregarTipoSala";
	}

}
