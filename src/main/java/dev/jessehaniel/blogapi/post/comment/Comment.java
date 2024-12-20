package dev.jessehaniel.blogapi.post.comment;

import dev.jessehaniel.blogapi.post.BlogPost;
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
@Table(name = "TB_COMMENTS")
public class Comment extends AuditedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @Lob
    @Column(nullable = false)
    private String content;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "blog_post_id", nullable = false, updatable = false)
    private BlogPost blogPost;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false, updatable = false)
    private User author;

    public Comment() {
        this.uuid = UUID.randomUUID();
    }

    @ToString.Include
    public String getAuthorName() {
        return this.author.getUsername();
    }

    @ToString.Include
    public String getBlogPostUuid() {
        return this.blogPost.getUuid().toString();
    }

    public String diff(Comment other) {
        StringBuilder differences = new StringBuilder("Differences found:\n");
        if (!this.content.equals(other.content)) {
            differences.append("Contents differ: ").append(this.content).append(" vs ").append(other.content).append("\n");
        }
        return differences.length() > 18 ? differences.toString() : "No differences found.";
    }

}
