package com.kspichale.rest.sample;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import guru.nidi.ramltester.RamlDefinition;
import guru.nidi.ramltester.RamlLoaders;
import guru.nidi.ramltester.SimpleReportAggregator;

import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = SpringRestSampleApplication.class)
public class ApiIntegrationTest {

	private static RamlDefinition api = RamlLoaders
			.fromClasspath(SpringRestSampleApplication.class).load("api.raml")
			.assumingBaseUri("http://localhost:8080/v1");

	private static SimpleReportAggregator aggregator = new SimpleReportAggregator();

	private MediaType contentType = new MediaType(
			MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Autowired
	WebApplicationContext context;

	protected MockMvc mvc;

	@Before
	public void setUp() {
		this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void getAllProducts() throws Exception {
		mvc.perform(
				get("/products").accept(
						MediaType.parseMediaType("application/json")))
				.andExpect(api.matches().aggregating(aggregator))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.[0].id", is(1)))
				.andExpect(jsonPath("$.[0].name", is("1")))
				.andExpect(jsonPath("$.[0].price", is("10.0")));
	}

	@Test
	public void findProductsByName() throws Exception {
		mvc.perform(
				get("/products?name=smartphone").accept(
						MediaType.parseMediaType("application/json")))
				.andExpect(api.matches().aggregating(aggregator))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.[0].id", is(1)))
				.andExpect(jsonPath("$.[0].name", is("smartphone1")))
				.andExpect(jsonPath("$.[0].price", is("10.0")));
	}

	@Test
	public void getPagedArticles() throws Exception {
		mvc.perform(
				get("/products?page=1&size=2").accept(
						MediaType.parseMediaType("application/json")))
				.andExpect(api.matches().aggregating(aggregator))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.[0].id", is(1)))
				.andExpect(jsonPath("$.[0].name", is("1")))
				.andExpect(jsonPath("$.[1].id", is(2)))
				.andExpect(jsonPath("$.[1].name", is("2")))
				.andExpect(jsonPath("$", hasSize(2)))
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void getArticlesById() throws Exception {
		mvc.perform(
				get("/products/1").accept(
						MediaType.parseMediaType("application/json")))
				.andExpect(api.matches().aggregating(aggregator))
				.andExpect(status().isFound())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("1")));
	}

	@Test
	public void receive404ForUnknownArticle() throws Exception {
		mvc.perform(
				get("/products/2").accept(
						MediaType.parseMediaType("application/json")))
				.andExpect(api.matches().aggregating(aggregator))
				.andExpect(status().isNotFound());
	}

	@Test
	public void updateArticle() throws Exception {
		mvc.perform(
				post("/products/1").param("name", "new name").param("price", "9.99").accept(
						MediaType.parseMediaType("application/json")))
				.andExpect(api.matches().aggregating(aggregator))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("new name")))
				.andExpect(jsonPath("$.price", is("9.99")));
	}
}
