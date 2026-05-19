package tasks.manager.service.categories;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tasks.manager.dto.category.CategoryDTO;
import tasks.manager.exception.CategoryNotFoundException;
import tasks.manager.model.category.Category;
import tasks.manager.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository repository;

    @Override
    @Cacheable(value = "categories", key = "'all'")
    public List<CategoryDTO> getAllCategories() {
        simulateSlowQuery();
        return repository.findAllByOrderByNameAsc()
                    .stream()
                    .map(this::toDTO)
                    .toList();
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        return toDTO(findOrThrow(id));
    }

    @Override
    @CacheEvict(value = "categories", allEntries = true)
    public CategoryDTO createCategory(CategoryDTO dto) {
        if(repository.existsByNameIgnoreCase(dto.getName())){
            throw new IllegalArgumentException( "Ya existe una categoría con el nombre: " + dto.getName());
        }
        Category saved = repository.save(toEntity(dto));
        return toDTO(saved);
    }

    @Override
    @CacheEvict(value = "categories", allEntries = true)
    public CategoryDTO updateCategory(Long id, CategoryDTO dto) {
        Category category = findOrThrow(id);
        category.setName(dto.getName());
        category.setColor(dto.getColor());
        return toDTO(category);
    }

    @Override
    @CacheEvict(value = "categories", allEntries = true)
    public void deleteCategory(Long id) {
        Category cat = findOrThrow(id);
        repository.delete(cat);
    }

    private void simulateSlowQuery() {
        try {
            System.out.println("[CACHE] Simulando consulta lenta...");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private Category findOrThrow(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    private CategoryDTO toDTO(Category c) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(c.getId());
        dto.setName(c.getName());
        dto.setColor(c.getColor());
        return dto;
    }

    private Category toEntity(CategoryDTO c) {
        Category dto = new Category(c.getName(), c.getColor());
        return dto;
    }
    
}
