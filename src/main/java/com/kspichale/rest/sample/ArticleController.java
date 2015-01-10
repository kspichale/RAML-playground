package com.kspichale.rest.sample;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleController {

	private final AtomicLong counter = new AtomicLong();

	@RequestMapping(value = "/articles", method = RequestMethod.GET)
	public List<Article> get(
			@RequestParam(value = "author", required = false) String author) {
		System.out.println(author);
		final List<Article> list = new ArrayList<Article>();
		list.add(new Article(1, "abc"));
		list.add(new Article(2, "def"));
		return list;
	}

	@RequestMapping(value = "/articles/{articleId}", method = RequestMethod.GET)
	public Article findById(@PathVariable Long articleId) {
		if (articleId == null) {
			return null;
		} else if (articleId == 1) {
			return new Article(1, "abc");
		}
		return new Article(2, "def");
	}

	@RequestMapping(value = "/articles/{articleId}", method = RequestMethod.POST)
	public ResponseEntity<Article> update(@PathVariable Long articleId,
			@RequestParam(value = "content") String content) {
		Article a = new Article(articleId, content);
		return new ResponseEntity<Article>(a, HttpStatus.CREATED);
	}
}
