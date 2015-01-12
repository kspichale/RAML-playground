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

@RestController
public class ArticleController {

	@RequestMapping(value = "/articles", method = RequestMethod.GET)
	public List<Article> get(
			@RequestParam(value = "author", required = false) String author,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "size", required = false, defaultValue = "10") int size) {

		final List<Article> list = new ArrayList<Article>();
		for (int i = (page - 1) * size + 1; i < page * size + 1; i++) {
			list.add(new Article(i, "" + i));
		}
		return list;
	}

	@RequestMapping(value = "/articles/{articleId}", method = RequestMethod.GET)
	public ResponseEntity<Article> findById(HttpServletRequest req,
			@PathVariable Long articleId) {
		if (articleId != null && 1 == articleId) {
			final Article a = new Article(1, "1");
			return new ResponseEntity<Article>(a, HttpStatus.FOUND);
		}
		// no article was found
		throw new ArticleNotFoundException();
	}

	@RequestMapping(value = "/articles/{articleId}", method = RequestMethod.POST)
	public ResponseEntity<Article> update(@PathVariable Long articleId,
			@RequestParam(value = "content") String content) {
		final Article a = new Article(articleId, content);
		return new ResponseEntity<Article>(a, HttpStatus.CREATED);
	}
}
