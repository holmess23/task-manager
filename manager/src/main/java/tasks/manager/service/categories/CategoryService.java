package tasks.manager.service.categories;

import java.util.List;

import org.springframework.stereotype.Service;

import tasks.manager.dto.category.CategoryDTO;

@Service
public interface CategoryService {
    
    List<CategoryDTO> getAllCategories();
    CategoryDTO getCategoryById(Long id);
    CategoryDTO createCategory(CategoryDTO dto);
    CategoryDTO updateCategory(Long id, CategoryDTO dto);
    void deleteCategory(Long id);
}
