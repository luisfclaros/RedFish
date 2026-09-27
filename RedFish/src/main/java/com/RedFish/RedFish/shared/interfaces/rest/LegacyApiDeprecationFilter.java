package com.RedFish.RedFish.shared.interfaces.rest;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LegacyApiDeprecationFilter extends OncePerRequestFilter {

	private static final String SUNSET_DATE = "Wed, 30 Jun 2027 23:59:59 GMT";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String requestPath = request.getRequestURI();
		String successorPath = requestPath.startsWith("/api/products")
				? requestPath.replaceFirst("/api/products", "/api/v1/products")
				: requestPath.replaceFirst("/api/customers", "/api/v1/customers");
		response.setHeader("Deprecation", "true");
		response.setHeader("Sunset", SUNSET_DATE);
		response.setHeader("Link", "<" + successorPath + ">; rel=\"successor-version\"");
		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();
		return !path.equals("/api/products") && !path.startsWith("/api/products/")
				&& !path.equals("/api/customers") && !path.startsWith("/api/customers/");
	}
}
