package com.gdeiassistant.gdeiassistant.Controller.Agreement;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AgreementController {

    @RequestMapping(value = "/agreement", method = RequestMethod.GET)
    public ModelAndView ResolveAgreementPage() {
        return new ModelAndView("Agreement/agreement");
    }
}