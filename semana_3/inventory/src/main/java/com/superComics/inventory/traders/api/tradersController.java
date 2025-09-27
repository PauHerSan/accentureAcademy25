package com.superComics.inventory.traders.api;

import com.superComics.inventory.traders.service.tradersService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/traders")
public class tradersController {

    private tradersService tradersService;


}
