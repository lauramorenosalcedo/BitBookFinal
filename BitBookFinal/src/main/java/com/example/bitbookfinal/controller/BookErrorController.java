package com.example.bitbookfinal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class BookErrorController  implements ErrorController { //The error controller is used to customize the base spring error displays.
    @RequestMapping("/error") // This function is used to redirect a faulty url request to the custom showError html. It also captures the error code to display in the html.
    public String handleError(Model model, HttpServletRequest request){
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if(status != null){
            Integer statusCode = Integer.valueOf(status.toString());
            model.addAttribute("statusCode", statusCode);
            return "showError";
        }
        return "showError";
    }
}
