package tasks.manager.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Data;

@Data
public class PageResponseDTO<T> {
    
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
    private boolean hasNext;
    private boolean hasPrevious;

    public static <T> PageResponseDTO<T> of(Page<T> page) {
        PageResponseDTO<T> dto = new PageResponseDTO<>();
        dto.setContent(page.getContent());
        dto.setPage(page.getNumber());
        dto.setSize(page.getSize());
        dto.setTotalElements(page.getTotalElements());
        dto.setTotalPages(page.getTotalPages());
        dto.setFirst(page.isFirst());
        dto.setLast(page.isLast());
        dto.setHasNext(page.hasNext());
        dto.setHasPrevious(page.hasPrevious());
        return dto;
    }

}
