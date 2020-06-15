package com.webbank.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MyErrorController implements ErrorController  {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, ModelMap model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        model.addAttribute("Error","Unexpected error happened");
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("Error","Page not found");
            } else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("Error","Internal server error happened");
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("Error","Access not granted");
            }
        }
        return "errorPage";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}