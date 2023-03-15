package server.api;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;
import server.database.CardListRepository;
import server.database.CardRepository;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardRepository cardRepository;
    private SimpMessagingTemplate messageTemplate;

    public CardController(CardRepository cardRepository,
                          CardListRepository cardListRepository,
                          BoardRepository boardRepository,
                          SimpMessagingTemplate messageTemplate) {
        this.cardRepository = cardRepository;
        this.messageTemplate = messageTemplate;
    }
}
