package top.bujiaban.mqsub.order.domain;

import io.hypersistence.utils.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@Table(name="o_event_message")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class EventMessage<T> implements DomainEvent<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String aggregationId;
    @CreatedDate
    private LocalDateTime createdTime;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private T content;

    @Enumerated(EnumType.STRING)
    private Status status;
    @LastModifiedDate
    private LocalDateTime lastModifiedTime;

    public static enum Status {
        CREATED, PUBLISHED, PUBLISH_FAILED
    }
}
