package br.senac.tads.dsw.exemplosspring.advanced;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/advanced-test")
@MvcCrudConfig(templatePath = "advanced")
public class AdvancedController {

	private String getListView() {
		MvcCrudConfig crudConfig = getClass().getAnnotation(MvcCrudConfig.class);
		return crudConfig.templatePath() + "/" + crudConfig.listFile();
	}

	private String getFormView() {
		MvcCrudConfig crudConfig = getClass().getAnnotation(MvcCrudConfig.class);
		return crudConfig.templatePath() + "/" + crudConfig.formFile();
	}

	@GetMapping
	public ModelAndView list() {
		return new ModelAndView(getListView());
	}

	@GetMapping("/form")
	public ModelAndView form() {
		return new ModelAndView(getFormView());
	}

}
