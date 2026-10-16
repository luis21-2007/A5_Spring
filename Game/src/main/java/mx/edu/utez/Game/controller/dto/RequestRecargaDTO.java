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
public class RequestRecargaDTO {

    @NotNull(message = "El monto a recargar es obligatorio")
    @Min(value = 1, message = "El monto a recargar debe ser mayor a 0")
    private Double monto;
}