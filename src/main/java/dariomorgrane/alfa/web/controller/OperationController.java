package dariomorgrane.alfa.web.controller;

import dariomorgrane.alfa.model.Operation;
import dariomorgrane.alfa.service.OperationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/")
public class OperationController {

    private final OperationService service;

    public OperationController(OperationService service) {
        this.service = service;
    }

    @GetMapping("/operate")
    public ModelAndView processOperation(@RequestParam("code") String currencyCode) {
        Operation operation = service.setupInitialData(currencyCode);
        service.setupRatesData(operation);
        service.setupGifData(operation);
        return new ModelAndView("redirect:" + operation.getGifUrl());
    }

}
