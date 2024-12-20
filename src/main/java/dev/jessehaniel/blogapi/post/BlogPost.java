package dev.jessehaniel.blogapi.post;

import dev.jessehaniel.blogapi.system.AuditedEntity;
import dev.jessehaniel.blogapi.users.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@Entity
@Table(name = "TB_POSTS")
public class BlogPost extends AuditedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false, updatable = false)
    private User author;

    public BlogPost() {
        this.uuid = UUID.randomUUID();
    }

    @ToString.Include
    public String getAuthorName() {
        return this.author.getUsername();
    }

    public String diff(BlogPost other) {
        StringBuilder differences = new StringBuilder();
        if (!this.title.equals(other.title)) {
            differences.append("Title differs: '").append(this.title).append("' vs '").append(other.title).append("'\n");
        }
        if (!this.content.equals(other.content)) {
            differences.append("Content differs\n");
        }
        return differences.isEmpty() ? "No differences found." : differences.toString();
    }
}
