package com.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class Greetings {

	// if i want to download the sessionId
		@GetMapping("/")
		public String greeting(HttpServletRequest request) {
			return "Welcome to Spring Security Course !!.." + request.getSession().getId();
		}


}
