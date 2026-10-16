package mx.edu.utez.Game.controller;

import jakarta.validation.Valid;
import mx.edu.utez.Game.controller.dto.RequestIniciarPartidaDTO;
import mx.edu.utez.Game.controller.dto.RequestRecargaDTO;
import mx.edu.utez.Game.model.jugador.Jugador;
import mx.edu.utez.Game.model.partida.Partida;
import mx.edu.utez.Game.model.tiro.Tiro;
import mx.edu.utez.Game.service.CasinoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/casino")
public class CasinoController {

    @Autowired
    private CasinoService casinoService;

    @PostMapping("/iniciar")
    public ResponseEntity<Partida> iniciarPartida(@Valid @RequestBody RequestIniciarPartidaDTO dto) {
        Partida partida = casinoService.iniciarPartida(dto.getIdJugador(), dto.getApuesta());
        return new ResponseEntity<>(partida, HttpStatus.CREATED);
    }

    @PostMapping("/partida/{idPartida}/tiro")
    public ResponseEntity<Tiro> realizarTiro(@PathVariable Long idPartida) {
        Tiro tiro = casinoService.realizarTiro(idPartida);
        return new ResponseEntity<>(tiro, HttpStatus.OK);
    }

    @PostMapping("/partida/{idPartida}/finalizar")
    public ResponseEntity<Partida> finalizarPartidaManual(@PathVariable Long idPartida) {
        Partida partida = casinoService.finalizarPartidaManual(idPartida);
        return new ResponseEntity<>(partida, HttpStatus.OK);
    }

    @PostMapping("/jugador/{idJugador}/recargar")
    public ResponseEntity<Jugador> recargarSaldo(
            @PathVariable Long idJugador,
            @Valid @RequestBody RequestRecargaDTO dto) {
        Jugador jugador = casinoService.recargarSaldo(idJugador, dto.getMonto());
        return new ResponseEntity<>(jugador, HttpStatus.OK);
    }

    @GetMapping("/jugador/{idJugador}/historial")
    public ResponseEntity<List<Partida>> obtenerHistorial(@PathVariable Long idJugador) {
        List<Partida> historial = casinoService.obtenerHistorial(idJugador);
        return new ResponseEntity<>(historial, HttpStatus.OK);
    }
}