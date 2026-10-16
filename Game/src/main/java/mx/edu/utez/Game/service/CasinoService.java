package mx.edu.utez.Game.service;

import mx.edu.utez.Game.exception.customExceptions.BadRequestException;
import mx.edu.utez.Game.model.jugador.Jugador;
import mx.edu.utez.Game.model.partida.EstadoPartida;
import mx.edu.utez.Game.model.partida.Partida;
import mx.edu.utez.Game.model.tiro.Tiro;
import mx.edu.utez.Game.repository.jugador.JugadorRepository;
import mx.edu.utez.Game.repository.partida.PartidaRepository;
import mx.edu.utez.Game.repository.tiro.TiroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class CasinoService {

    @Autowired
    private JugadorRepository jugadorRepository;

    @Autowired
    private PartidaRepository partidaRepository;

    @Autowired
    private TiroRepository tiroRepository;

    private final Random random = new Random();

    @Transactional
    public Partida iniciarPartida(Long idJugador, Double apuesta) {
        Jugador jugador = jugadorRepository.findById(idJugador)
                .orElseThrow(() -> new BadRequestException("Jugador no encontrado en la base de datos"));

        if (!jugador.getActivo()) {
            throw new BadRequestException("El jugador no esta activo, no puede iniciar partida");
        }

        if (jugador.getSaldo() < apuesta) {
            throw new BadRequestException("Saldo insuficiente para realizar una apuesta");
        }

        jugador.setSaldo(jugador.getSaldo() - apuesta);
        jugadorRepository.save(jugador);

        Partida partida = new Partida();
        partida.setJugador(jugador);
        partida.setApuesta(apuesta);
        partida.setEstado(EstadoPartida.EN_JUEGO);
        partida.setFecha(LocalDateTime.now());

        return partidaRepository.save(partida);
    }

    @Transactional
    public Tiro realizarTiro(Long idPartida) {
        Partida partida = partidaRepository.findById(idPartida)
                .orElseThrow(() -> new BadRequestException("Partida no encontrada"));

        if (partida.getEstado() != EstadoPartida.EN_JUEGO) {
            throw new BadRequestException("No se puede tirar, la partida ya ha finalizado");
        }

        int dado1 = random.nextInt(6) + 1;
        int dado2 = random.nextInt(6) + 1;
        int suma = dado1 + dado2;

        Tiro tiro = new Tiro();
        tiro.setPartida(partida);
        tiro.setValorDado1(dado1);
        tiro.setValorDado2(dado2);
        tiro.setSuma(suma);

        if (suma == 7 || suma == 11) {
            tiro.setEsGanador(true);
            partida.setEstado(EstadoPartida.FINALIZADA);

            Jugador jugador = partida.getJugador();
            jugador.setSaldo(jugador.getSaldo() + (partida.getApuesta() * 2));
            jugadorRepository.save(jugador);

        } else if (suma == 2 || suma == 3 || suma == 12) {
            tiro.setEsGanador(false);
            partida.setEstado(EstadoPartida.FINALIZADA);
        } else {
            tiro.setEsGanador(false);
        }

        partidaRepository.save(partida);
        return tiroRepository.save(tiro);
    }

    @Transactional
    public Partida finalizarPartidaManual(Long idPartida) {
        Partida partida = partidaRepository.findById(idPartida)
                .orElseThrow(() -> new BadRequestException("Partida no encontrada"));

        if (partida.getEstado() == EstadoPartida.FINALIZADA) {
            throw new BadRequestException("La partida ya se encuentra finalizada");
        }

        partida.setEstado(EstadoPartida.FINALIZADA);
        return partidaRepository.save(partida);
    }

    @Transactional
    public Jugador recargarSaldo(Long idJugador, Double monto) {
        Jugador jugador = jugadorRepository.findById(idJugador)
                .orElseThrow(() -> new BadRequestException("Jugador no encontrado para recargar"));

        jugador.setSaldo(jugador.getSaldo() + monto);
        return jugadorRepository.save(jugador);
    }

    public List<Partida> obtenerHistorial(Long idJugador) {
        if (!jugadorRepository.existsById(idJugador)) {
            throw new BadRequestException("Jugador no encontrado");
        }
        return partidaRepository.findByJugadorId(idJugador);
    }
}