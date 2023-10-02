package top.bujiaban.seataat.inventory.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "i_product_storage")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ProductStorage {
    @Id
    private String id;
    @Version
    private Integer version;
    private String productId;
    private Integer quantity;
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdTime;
    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime lastModifiedTime;

    public ProductStorage minusStorage(Integer quantity) {
        this.quantity -= quantity;
        return this;
    }
}
