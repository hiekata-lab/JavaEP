package jp.ac.u_tokyo.k.is.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class GlobalExceptionResolver implements HandlerExceptionResolver {
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);
	
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object object, Exception ex) {
		logger.error("Exception catched.", ex);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("message", "Unexpected error happened. Detail: " + ex);
		mav.setViewName("error");
		
		return mav;
	}
}
