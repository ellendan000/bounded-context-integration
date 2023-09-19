package top.bujiaban.inventory.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "rpc_product_storage")
@NoArgsConstructor
public class ProductStorage {
    @Id
    private String id;
    private String productId;
    private Integer quantity;
    private LocalDateTime createdTime;
    private LocalDateTime lastModifiedTime;
}
