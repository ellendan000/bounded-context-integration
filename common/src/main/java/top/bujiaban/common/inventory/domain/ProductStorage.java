package top.bujiaban.common.inventory.domain;

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
    private LocalDateTime createdTime;
    @LastModifiedDate
    private LocalDateTime lastModifiedTime;

    public ProductStorage minusStorage(Integer quantity) {
        this.quantity -= quantity;
        return this;
    }
}
