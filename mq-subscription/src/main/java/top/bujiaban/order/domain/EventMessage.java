package top.bujiaban.order.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class EventMessage<T> implements DomainEvent<String> {
    @Id
    @GeneratedValue
    private Long id;
    private String aggregationId;
    @CreatedDate
    private LocalDateTime createdTime;

    @Column(columnDefinition = "Json")
    private T content;

    private Status status;
    @LastModifiedDate
    private LocalDateTime lastModifiedTime;

    public static enum Status {
        CREATED, PUBLISHED, PUBLISH_FAILED
    }
}
