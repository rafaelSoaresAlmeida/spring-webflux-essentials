package academy.devdojo.springwebfluxessentials.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
@With
@Table("ANIME")
public class Anime {

    @Id
    private Integer id;

    @NotNull
    @NotEmpty(message = "The name of this anime cannot be empty")
    private String name;

}
