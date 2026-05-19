package tasks.manager.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CategoryDTO {
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío.")
    private String name;

    @NotBlank(message = "El color es obligatorio.")
    @Pattern(
        regexp = "^#[0-9a-fA-F]{6}$",
        message = "El color debe ser un hexadecimal válido como #1E3A8A"
    )
    private String color;
    
}
