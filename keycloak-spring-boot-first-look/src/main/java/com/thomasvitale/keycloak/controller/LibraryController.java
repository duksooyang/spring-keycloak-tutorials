package com.thomasvitale.keycloak.controller;

import com.thomasvitale.keycloak.repository.BookRepository;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class LibraryController {

	private final HttpServletRequest request;
	private final BookRepository bookRepository;

	public LibraryController(HttpServletRequest request, BookRepository bookRepository) {
		this.request = request;
		this.bookRepository = bookRepository;
	}

	@GetMapping(value = "/")
	public String getHome() {
		return "index";
	}

	@GetMapping(value = "/books")
	public String getBooks(Model model) {
		configCommonAttributes(model);
		model.addAttribute("books", bookRepository.readAll());
		return "books";
	}

	@GetMapping(value = "/manager")
	public String getManager(Model model) {
		configCommonAttributes(model);
		model.addAttribute("books", bookRepository.readAll());
		return "manager";
	}

	@GetMapping(value = "/userattr")
	public String getUserAttribute(Model model) {
		configCommonAttributes(model);
		model.addAttribute("books", bookRepository.readAll());
		return "userattr";
	}



	@GetMapping(value = "/logout")
	public String logout() throws ServletException {
		request.logout();
		return "redirect:/";
	}

	private void configCommonAttributes(Model model) {
		model.addAttribute("name", getKeycloakSecurityContext().getIdToken().getGivenName());

		AccessToken token = ((KeycloakPrincipal) request.getUserPrincipal()).getKeycloakSecurityContext().getToken();
		Map<String, Object> otherClaims = token.getOtherClaims();
		System.out.println(otherClaims.size());

		//int cnt = 0;
		//for(String key : otherClaims.keySet()){
		//	cnt ++;
		//	String value = String.valueOf(otherClaims.get(key));
		//	model.addAttribute("attribute" + cnt, value);
		//}


		if(otherClaims.containsKey("fullname")){

			model.addAttribute("attribute1", String.valueOf(otherClaims.get("fullname")));
		}

		if(otherClaims.containsKey("place")){
			model.addAttribute("attribute2", String.valueOf(otherClaims.get("place")));
		}

		if(otherClaims.containsKey("sex")){
			model.addAttribute("attribute3", String.valueOf(otherClaims.get("sex")));
		}


	}

	private KeycloakSecurityContext getKeycloakSecurityContext() {
		return (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
	}


}
