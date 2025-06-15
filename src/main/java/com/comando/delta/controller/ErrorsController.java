package com.comando.delta.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorsController implements ErrorController{

    @RequestMapping(value = "error", method = { RequestMethod.GET, RequestMethod.POST})
    public ModelAndView renderErrorPage(HttpServletRequest httpServletRequest){

        ModelAndView errorPage = new ModelAndView("error");

        String msg = "";

        int httpErrorCode = (Integer) httpServletRequest.getAttribute("javax.servlet.error.status_code");

        switch (httpErrorCode) {
            case 400:{
                msg = "El recurso solicitado no existe";
                break;
            }

            case 401:{
                msg = "No tiene permisos para acceder a esta página";
                break;
            }

            case 403:{
                msg = "No tiene permisos para acceder al recurso";
                break;
            }

            case 404:{
                msg = "No se ha encontrado la página solicitada";
                break;
            }

            case 405:{
                msg = "Método de petición no permitido en esta pagina";
                break;
            }

            case 500:{
                msg = "Ocurrio un error en el servidor";
                break;
            }
        }

        errorPage.addObject("msg", msg);
        errorPage.addObject("errorCode", httpErrorCode);
        
        return errorPage;
    }  


    public String getErrorPath(){

        return "error.html";
    }

    
}
