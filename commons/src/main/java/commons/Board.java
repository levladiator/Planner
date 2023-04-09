package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity(name = "Board")
@Table(name = "board", uniqueConstraints = @UniqueConstraint(columnNames = {"key"}))
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class,
                property = "@board_id")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    @Column(nullable = false)
    public String key;
    public String title;

    @OneToMany(mappedBy = "parentBoard",
            cascade = CascadeType.ALL)
    public List<CardList> cardLists = new ArrayList<>();

    @ManyToMany(mappedBy = "boards")
    public Set<User> users = ConcurrentHashMap.newKeySet();

    @OneToMany(mappedBy = "parentBoard",
            cascade = CascadeType.ALL)
    public List<Tag> tags = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    public ColorPreset colors;

    @OneToOne(cascade = CascadeType.ALL)
    public ColorPreset cardListColors;

    @OneToMany(cascade = CascadeType.ALL)
    public List<ColorPreset> cardPresets = new ArrayList<>();

    @OneToOne
    public ColorPreset defaultPreset;

    public boolean isPasswordProtected = false;

    @JsonIgnore
    public String password = null;

    @SuppressWarnings("unused")
    public Board() {
    }

    /**
     * @param key The key set when creating the board, used to join the board. (is UNIQUE)
     * @param title The board's title.
     */
    public Board(String key, String title) {
        this.key = key;
        this.title = title;
    }

    public void addCard(CardList newCardList) {
        cardLists.add(newCardList);
    }

    public void addUser(User newUser) {
        users.add(newUser);
    }

    public void addTag(Tag newTag) {
        tags.add(newTag);
    }

    public static String getDefaultForeground() { return "#1a4d1a"; }

    public static String getDefaultBackground() { return "#adaaaa"; }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }

}
