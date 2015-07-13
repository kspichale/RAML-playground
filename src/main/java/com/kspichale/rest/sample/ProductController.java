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


@Api(value="/products", description="The Products resource provides ...")
@RestController
public class ProductController {

	@ApiOperation(value = "Get list of all products")
	@RequestMapping(value = "/products", method = RequestMethod.GET)
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

	@ApiOperation(value = "Get single product")
	@ApiResponse(code=200, message="die message", response=Product.class)
	@RequestMapping(value = "/products/{productId}", method = RequestMethod.GET)
	public ResponseEntity<Product> findById(HttpServletRequest req,
			@ApiParam(name="productId", allowableValues= "1, 2, 3", required=true, value="This is a unique id.")
	        @PathVariable Long productId) {
		if (productId != null && 1 == productId) {
			final Product a = new Product(1, "1", "10.0");
			return new ResponseEntity<Product>(a, HttpStatus.FOUND);
		}
		// no article was found
		throw new ProductNotFoundException();
	}

	@ApiOperation(value = "Change specific product")
	@RequestMapping(value = "/products/{productId}", method = RequestMethod.POST)
	public ResponseEntity<Product> update(@PathVariable Long productId,
			@RequestParam(value = "name") String name,
			@RequestParam(value = "price") String number) {
		final Product a = new Product(productId, name, number);
		return new ResponseEntity<Product>(a, HttpStatus.CREATED);
	}
}
