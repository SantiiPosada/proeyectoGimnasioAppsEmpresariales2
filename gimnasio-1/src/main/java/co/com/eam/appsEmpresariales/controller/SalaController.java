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
import co.com.eam.appsEmpresariales.domain.Sala;
import co.com.eam.appsEmpresariales.domain.Socio;
import co.com.eam.appsEmpresariales.domain.TipoSala;
import co.com.eam.appsEmpresariales.repository.SalaRepository;
import co.com.eam.appsEmpresariales.service.MessageByLocaleService;

@Controller
public class SalaController {
	
	@Autowired
    MessageByLocaleService messageByLocaleService;
	
	
	@Autowired // inyeccion de depedencias. Meter un dao
	private SalaRepository salaRepo;

	@Autowired
	private CloudinaryConfig cloudc;

	@Autowired
	public SalaController(SalaRepository salaRepo) {
		this.salaRepo = salaRepo;
	}

	@GetMapping("/ListarSala")
	public String listarTodasSala(Model model) {
		model.addAttribute("Salas", salaRepo.findAll());
		return "ListaSalas";
	}

	@GetMapping("/IrAgregarSala")
	public String IrAgregarSala(Sala sala, Model model) {

		Iterable<TipoSala> ListaTipoSala = salaRepo.ListarTipoSalas();
		model.addAttribute("tipoSalasOrdenadas", ListaTipoSala);
		return "AgregarSala";
	}

	@PostMapping("/AgregarSala")
	public String AgregarSala(@Valid Sala sala, BindingResult result, Model model,
			@RequestParam("file") MultipartFile file, RedirectAttributes attribute) {
		Iterable<TipoSala> ListaTipoSala = salaRepo.ListarTipoSalas();

		if (result.hasErrors()) {

			model.addAttribute("tipoSalasOrdenadas", ListaTipoSala);

			return "AgregarSala";

		}

		Sala salaPorUbicacion = (Sala) salaRepo.BuscarSalaPorUbicacion(sala.getUbicacion());
		
	
		if (salaPorUbicacion != null) {
			new IllegalArgumentException(messageByLocaleService.getMessage("SalaController.duplicateLocationWarning")	);
			model.addAttribute("tipoSalasOrdenadas", ListaTipoSala);
			model.addAttribute("warning",messageByLocaleService.getMessage("SalaController.duplicateLocationWarning"));
			return "AgregarSala";
		}
		TipoSala tipoSalaPorId=salaRepo.BuscarTipoSalaPorId(sala.getTiposala().getId());
		if (tipoSalaPorId == null) {
			new IllegalArgumentException( messageByLocaleService.getMessage("SalaController.SelectRoomTypeWarning"));
			model.addAttribute("tipoSalasOrdenadas", ListaTipoSala);
			model.addAttribute("warning",  messageByLocaleService.getMessage("SalaController.SelectRoomTypeWarning"));
			return "AgregarSala";
		}
		
		try {
			Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
			System.out.println(uploadResult.get("url").toString());
			sala.setUrlFoto(uploadResult.get("url").toString());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			new IllegalArgumentException( messageByLocaleService.getMessage("SalaController.pictureWarning") );
			model.addAttribute("warning",messageByLocaleService.getMessage("SalaController.pictureWarning"));
			model.addAttribute("tipoSalasOrdenadas", ListaTipoSala);
			return "AgregarSala";
		}
		salaRepo.save(sala);
		model.addAttribute("Salas", salaRepo.findAll());
		attribute.addFlashAttribute("success", messageByLocaleService.getMessage("SalaController.savedRoomSucces"));

		return "redirect:/ListarSala";

	}

	@GetMapping("/IrEditarSala/{id}")
	public String irEditarSala(@PathVariable("id") Integer id, Model model) {
		Sala sala = salaRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Id invalido: " + id));
		
		model.addAttribute("sala", sala);
		return "EditarSala";
	}

	@PostMapping("/EditarSala/{id}")
	public String EditarSala(@PathVariable("id") Integer id, @Valid Sala sala, BindingResult result, Model model,@RequestParam("file") MultipartFile file, @RequestParam("cambioUrl") boolean cambioUrl,
			RedirectAttributes attribute) {
		if (result.hasErrors()) {
			sala.setId(id);
			
			return "EditarSala";
		}
		
		Sala salaPorId = salaRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Id invalido: " + id));
		
		
		if (salaPorId.getUbicacion().equals(sala.getUbicacion())) {

		} else {
			Sala salaPorUbicacion = (Sala) salaRepo.BuscarSalaPorUbicacion(sala.getUbicacion());
			if (salaPorUbicacion != null) {
				new IllegalArgumentException( messageByLocaleService.getMessage("SalaController.duplicateLocationWarning") );
				model.addAttribute("warning", messageByLocaleService.getMessage("SalaController.duplicateLocationWarning"));
				sala.setId(id);
				return "EditarSala";
			}

		}
		
		
		
		if (cambioUrl) {
			try {
	            Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
	            System.out.println(uploadResult.get("url").toString());
	            sala.setUrlFoto(uploadResult.get("url").toString());	
	            
	        } catch (Exception e) {
	        	System.out.println(e.getMessage());
	        	new IllegalArgumentException( messageByLocaleService.getMessage("SalaController.pictureWarning"));
				model.addAttribute("warning", messageByLocaleService.getMessage("SalaController.pictureWarning"));
				sala.setId(id);
				return "EditarSala";
	        }
		}
		salaRepo.save(sala);
		model.addAttribute("Salas", salaRepo.findAll());
		attribute.addFlashAttribute("info", messageByLocaleService.getMessage("SalaController.modifyRoomSucces") );
		return "redirect:/ListarSala";
	}

	@GetMapping("/EliminarSala/{id}")
	public String EliminarSala(@PathVariable("id") Integer id, Model model, RedirectAttributes attribute) {
		Sala sala = salaRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Id invalido: " + id));
		salaRepo.delete(sala);
		model.addAttribute("Salas", salaRepo.findAll());
		attribute.addFlashAttribute("warning", messageByLocaleService.getMessage("SalaController.removeRoomSucces") );
		return "redirect:/ListarSala";
	}

}
