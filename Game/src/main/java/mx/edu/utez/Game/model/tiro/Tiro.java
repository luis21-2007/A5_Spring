package mx.edu.utez.Game.model.tiro;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utez.Game.model.partida.Partida;

@Entity
@Table(name = "tiros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "valor_dado_1", nullable = false)
    private Integer valorDado1;

    @Column(name = "valor_dado_2", nullable = false)
    private Integer valorDado2;

    @Column(name = "suma", nullable = false)
    private Integer suma;

    @Column(name = "es_ganador", nullable = false)
    private Boolean esGanador;

    @ManyToOne
    @JoinColumn(name = "partida_id", nullable = false)
    private Partida partida;
}