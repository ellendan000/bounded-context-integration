package top.bujiaban.mqsub.order.interfaces;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.bujiaban.mqsub.order.service.EventPublisher;
import top.bujiaban.mqsub.order.domain.EventMessage;
import top.bujiaban.mqsub.order.domain.EventMessageRepository;
import top.bujiaban.mqsub.order.domain.Status;

import java.util.List;

import static com.google.common.collect.Sets.newHashSet;

@Service
@RestController
@RequestMapping("/compensation-message")
public class CompensationSendMessageController {
    private final EventMessageRepository eventMessageRepository;
    private final EventPublisher eventPublisher;

    public CompensationSendMessageController(EventMessageRepository eventMessageRepository,
                                             EventPublisher eventPublisher) {
        this.eventMessageRepository = eventMessageRepository;
        this.eventPublisher = eventPublisher;
    }

    @GetMapping
    public void triggerResendMessage() {
        List<EventMessage> eventMessageList = eventMessageRepository.findByStatusAndOrderByCreatedTimeAsc(
                newHashSet(Status.CREATED, Status.PUBLISH_FAILED));
        eventMessageList.forEach(eventPublisher::republish);
    }
}
