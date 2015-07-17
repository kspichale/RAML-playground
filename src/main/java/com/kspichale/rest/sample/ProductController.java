package com.kspichale.rest.sample;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Api(value = "/products", position = 1, description = "Provides functions to retrieve, search, and update products.")
@RestController
@RequestMapping("/products")
public class ProductController {

	@ApiOperation(value = "Get list of all products")
	@RequestMapping(method = RequestMethod.GET)
	public List<Product> get(
			@RequestParam(value = "name", required = false, defaultValue = "") String name,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "size", required = false, defaultValue = "10") int size) {

		final List<Product> list = new ArrayList<Product>();
		for (int i = (page - 1) * size + 1; i < page * size + 1; i++) {
			list.add(new Product(i, name + i, "10.0"));
		}
		return list;
	}

	@ApiOperation(value = "Find single product by ID", response = Product.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully found product", response = Product.class),
			@ApiResponse(code = 400, message = "Invalid Id supplied", response = InvalidIdException.class ),
			@ApiResponse(code = 404, message = "Product not found", response = ProductNotFoundException.class) })
	@RequestMapping(value = "/{productId}", method = RequestMethod.GET)
	public ResponseEntity<Product> findById(
			HttpServletRequest req,
			@ApiParam(name = "productId", allowableValues = "1, 2, 3", required = true, value = "This is a unique id.") @PathVariable String productId) {
		
		long parsedProductId = -1;
		try {
			parsedProductId = Long.parseLong(productId);
		} catch (NumberFormatException pae) {
			throw new InvalidIdException();
		}
		
		if (1 == parsedProductId) {
			final Product a = new Product(1, "1", "10.0");
			return new ResponseEntity<Product>(a, HttpStatus.FOUND);
		}
		// no article was found
		throw new ProductNotFoundException();
	}

	@ApiOperation(value = "Change specific product")
	@RequestMapping(value = "/{productId}", method = RequestMethod.POST)
	public ResponseEntity<Product> update(@PathVariable Long productId,
			@RequestParam(value = "name") String name,
			@RequestParam(value = "price") String number) {
		final Product a = new Product(productId, name, number);
		return new ResponseEntity<Product>(a, HttpStatus.CREATED);
	}
}
