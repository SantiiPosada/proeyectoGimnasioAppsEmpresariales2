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
import co.com.eam.appsEmpresariales.domain.Aparato;
import co.com.eam.appsEmpresariales.domain.Sala;
import co.com.eam.appsEmpresariales.repository.AparatoRepository;
import co.com.eam.appsEmpresariales.repository.ClaseRepository;
import co.com.eam.appsEmpresariales.service.MessageByLocaleService;

@Controller
public class AparatoController {

	@Autowired
	MessageByLocaleService messageByLocaleService;

	private final AparatoRepository aparatoRepo;

	@Autowired
	private CloudinaryConfig cloudc;

	@Autowired // inyeccion de depedencias. Meter un dao
	public AparatoController(AparatoRepository aparatoRepo) {
		this.aparatoRepo = aparatoRepo;
	}

	@GetMapping("/ListarAparato")
	public String ListarAparato(Model model) {
		model.addAttribute("Aparatos", aparatoRepo.findAll());
		return "ListaAparato";
	}

	@GetMapping("/IrAgregarAparato")
	public String IrAgregarAparato(Aparato aparato, Model model) {
		Iterable<Sala> ListaSala = aparatoRepo.ListarSala();

		model.addAttribute("salasOrdenadas", ListaSala);

		return "AgregarAparato";
	}

	@PostMapping("/AgregarAparato")
	public String AgregarAparato(@Valid Aparato aparato, BindingResult result, Model model,
			@RequestParam("file") MultipartFile file, RedirectAttributes attribute) {
		Iterable<Sala> ListaSala = aparatoRepo.ListarSala();
		if (result.hasErrors()) {
			model.addAttribute("salasOrdenadas", ListaSala);
			return "AgregarAparato";
		}
		try {
			Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
			System.out.println(uploadResult.get("url").toString());
			aparato.setUrlFoto(uploadResult.get("url").toString());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			new IllegalArgumentException(messageByLocaleService.getMessage("DomainAparatoController.pictureWarning"));
			model.addAttribute("warning", messageByLocaleService.getMessage("DomainAparatoController.pictureWarning"));
			model.addAttribute("salasOrdenadas", ListaSala);
			return "AgregarAparato";
		}
		aparatoRepo.save(aparato);
		model.addAttribute("Aparatos", aparatoRepo.findAll());
		attribute.addFlashAttribute("success",
				messageByLocaleService.getMessage("DomainAparatoController.savedPartnerSucces"));
		return "redirect:/ListarAparato";
	}

	@GetMapping("/IrEditarAparato/{id}")
	public String IrEditarAparato(@PathVariable("id") Integer id, Model model) {
		Aparato aparato = aparatoRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Id Invalido: " + id));
		Iterable<Sala> ListaSala = aparatoRepo.ListarSala();

		model.addAttribute("salasOrdenadas", ListaSala);
		model.addAttribute("aparato", aparato);
		return "EditarAparato";
	}

	@PostMapping("/EditarAparato/{id}")
	public String EditarAparato(@PathVariable("id") Integer id, @Valid Aparato aparato, BindingResult result,
			Model model, @RequestParam("file") MultipartFile file, @RequestParam("cambioUrl") boolean cambioUrl,
			RedirectAttributes attribute) {
		Iterable<Sala> ListaSala = aparatoRepo.ListarSala();
		if (result.hasErrors()) {
			aparato.setId(id);
			model.addAttribute("salasOrdenadas", ListaSala);
			return "EditarAparato";
		}

		if (cambioUrl) {
			try {
				Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
				System.out.println(uploadResult.get("url").toString());
				aparato.setUrlFoto(uploadResult.get("url").toString());

			} catch (Exception e) {
				System.out.println(e.getMessage());
				new IllegalArgumentException(
						messageByLocaleService.getMessage("DomainAparatoController.pictureWarning"));
				model.addAttribute("warning",
						messageByLocaleService.getMessage("DomainAparatoController.pictureWarning"));
				model.addAttribute("salasOrdenadas", ListaSala);
				aparato.setId(id);
				return "EditarAparato";
			}
		}
		aparatoRepo.save(aparato);
		model.addAttribute("Aparatos", aparatoRepo.findAll());
		attribute.addFlashAttribute("info",
				messageByLocaleService.getMessage("DomainAparatoController.modifyPartnerSucces"));
		return "redirect:/ListarAparato";
	}

	@GetMapping("/EliminarAparato/{id}")
	public String EliminarAparato(@PathVariable("id") Integer id, Model model, RedirectAttributes attribute) {
		Aparato aparato = aparatoRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Id Invalido:" + id));
		aparatoRepo.delete(aparato);
		model.addAttribute("Aparatos", aparatoRepo.findAll());
		attribute.addFlashAttribute("warning",
				messageByLocaleService.getMessage("DomainAparatoController.removePartnerSucces"));
		return "redirect:/ListarAparato";
	}

	@PostMapping("**/aparatoFiltradoSala")
	public String AparatoFiltrado(@RequestParam("salaFiltrar") String salaFiltrar, Model model) {
		model.addAttribute("Aparatos", aparatoRepo.BuscarAparatoPorTipoSala(salaFiltrar));

		return "ListaAparato";
	}
}
