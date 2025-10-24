package com.turkcell.product_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
@RequestMapping("/api/v1/products")
public class ProductsController {
	@GetMapping
	public String getProducts() {
		return "Products";
	}

	@PostMapping
	public String createProduct() {
		return "Product created";
	}

	@PutMapping
	public String updateProduct() {
		return "Product updated";
	}

	@DeleteMapping
	public String deleteProduct() {
		return "Product deleted";
	}
}
