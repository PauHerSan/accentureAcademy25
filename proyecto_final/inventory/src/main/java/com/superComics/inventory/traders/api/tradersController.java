package com.superComics.inventory.traders.api;

import com.superComics.inventory.shared.traderCreationDTO;
import com.superComics.inventory.traders.model.traders;
import com.superComics.inventory.traders.service.traderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/traders")
public class tradersController {

    private final traderService traderService;

    public tradersController(traderService traderService) {
        this.traderService = traderService;
    }

    @PostMapping
    public ResponseEntity<traders> createTrader(@RequestBody traderCreationDTO request) {
        traders newTrader = traderService.createNewTrader(
                request.getName(),
                request.getEmail(),
                request.getSpeciality(),
                request.getUsualPrice(),
                request.getAddress()
        );
        return new ResponseEntity<>(newTrader, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<traders> getTrader(@PathVariable Long id) {
        traders trader = traderService.getTraderById(id);
        return ResponseEntity.ok(trader);
    }

    @GetMapping("/active")
    public ResponseEntity<List<traders>> getActiveTraders() {
        List<traders> traders = traderService.getAllActiveTraders();
        return ResponseEntity.ok(traders);
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateTrader(@PathVariable Long id) {
        traderService.deactivateTrader(id);
        return ResponseEntity.ok().build();
    }


}
