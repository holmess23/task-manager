package tasks.manager.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long id) {
        super("No se encontró la categoría con id: " + id);
    }
    
}
