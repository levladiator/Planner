package server.database;

import commons.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CardRepository extends JpaRepository<Card, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE Card c SET c.index = c.index - 1 WHERE c.index > :index AND c.parentCardList.id = :listId")
    void shiftCardsUp(int index, long listId);

    @Transactional
    @Modifying
    @Query("UPDATE Card c SET c.index = c.index + 1 WHERE c.index >= :index AND c.parentCardList.id = :listId")
    void shiftCardsDown(int index, long listId);
}
