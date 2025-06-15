package com.comando.delta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.comando.delta.service.AutomobileService;
import com.comando.delta.service.ChargeService;

@Controller
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private AutomobileService automobileService;


    @GetMapping("/charge/{id}")
    public ResponseEntity<byte[]> detailsChargeImage(@PathVariable Long id){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);        
        
        return new ResponseEntity<byte[]>(
            (byte[]) chargeService.getOneChargeById(id).getImage().getContent(),
            headers,
            HttpStatus.OK
        );

    }

    @GetMapping("/automobile/{id}")
    public ResponseEntity<byte[]> detailsAutomobileImage(@PathVariable Long id){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);        
        
        return new ResponseEntity<byte[]>(
            (byte[]) automobileService.getOneAutomobile(id).getImage().getContent(),
            headers,
            HttpStatus.OK
        );

    }
    
}
