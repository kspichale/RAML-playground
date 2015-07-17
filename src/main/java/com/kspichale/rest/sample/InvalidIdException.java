package com.kspichale.rest.sample;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Provided Id is not valid")
public class InvalidIdException extends RuntimeException {
}