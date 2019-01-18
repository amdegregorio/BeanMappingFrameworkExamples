package com.amydegregorio.mappers.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MenuController {
   private final Logger LOG = LoggerFactory.getLogger(this.getClass());
   
   @RequestMapping("/")
   public String listAll(Model model) {
      
      return "menu";
   }
}
