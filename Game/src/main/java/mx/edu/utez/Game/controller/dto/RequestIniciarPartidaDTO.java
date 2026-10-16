package mx.edu.utez.Game.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestIniciarPartidaDTO {

    @NotNull(message = "El ID del jugador es obligatorio")
    private Long idJugador;

    @NotNull(message = "La apuesta es obligatoria")
    @Min(value = 1, message = "La apuesta mínima es de 1")
    private Double apuesta;
}