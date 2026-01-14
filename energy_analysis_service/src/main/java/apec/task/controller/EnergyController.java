package apec.task.controller;

import apec.task.dto.ChargingWindowResponseDto;
import apec.task.dto.EnergyMixResponseDto;
import apec.task.service.EnergyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/energy")
public class EnergyController {

    private final EnergyService service;

    public EnergyController(EnergyService service) {
        this.service = service;
    }

    @GetMapping("/energy-mix")
    public ResponseEntity<EnergyMixResponseDto> averageResources() {
        EnergyMixResponseDto mix = service.getEnergyMix();
        return ResponseEntity.ok(mix);
    }

    @GetMapping("/optimal-charging-window")
    public ResponseEntity<ChargingWindowResponseDto> chargingWindow(@RequestParam Integer windowHours) {
        ChargingWindowResponseDto chargingWindow = service.getChargingWindow(windowHours);
        return ResponseEntity.ok(chargingWindow);
    }

    @GetMapping("/")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok("Hello World");
    }

}
