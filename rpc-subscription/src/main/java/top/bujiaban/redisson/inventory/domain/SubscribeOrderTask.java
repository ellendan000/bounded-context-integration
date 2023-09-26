package top.bujiaban.rpc.inventory.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@Entity
@Table(name = "i_subscribe_order_task")
@NoArgsConstructor
@AllArgsConstructor
public class SubscribeOrderTask {
    @Id
    private String productId;
    private Long timestamp;
    private Integer currentPage;
    private Integer pageSize;
}
