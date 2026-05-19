package tasks.manager.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tasks.manager.model.category.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{

    Optional<Category> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
    List<Category> findAllByOrderByNameAsc();
    Set<Category> findByIdIn(Set<Long> ids);
    
}
