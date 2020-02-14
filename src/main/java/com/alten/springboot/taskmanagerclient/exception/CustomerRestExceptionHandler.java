package com.alten.springboot.taskmanagerclient.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CustomerRestExceptionHandler {

	@ExceptionHandler
	public ModelAndView handleException(HttpServletRequest req, Exception exc){


		ModelAndView mav = new ModelAndView();
		mav.addObject("exception", exc);
		mav.addObject("url", req.getRequestURL());
		mav.setViewName("Error");
		return mav;

	}

}
