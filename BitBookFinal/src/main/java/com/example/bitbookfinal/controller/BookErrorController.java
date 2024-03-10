package com.example.bitbookfinal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class BookErrorController  implements ErrorController {
    @RequestMapping("/error")
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
