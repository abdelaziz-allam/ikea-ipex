package com.ikea.product.catalog.api;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Hidden
public class Index {

  private final static String SWAGGER_URI = "redirect:/swagger-ui/index.html";

  @GetMapping("/")
  public String redirect(){
    return SWAGGER_URI;
  }
}
