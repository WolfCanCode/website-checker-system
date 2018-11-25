package com.fpt.capstone.wcs.model.entity.website;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fpt.capstone.wcs.model.entity.user.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Getter
@Setter
@Table(name = "WarningWord")

public class WarningWord {
    @Id
    @GeneratedValue
    private long id;
    @NotNull
    private String word;

    @CreationTimestamp
    private LocalDateTime createdTime;
    @UpdateTimestamp
    private LocalDateTime modifiedTime;
    @JsonIgnore
    private boolean delFlag;
    @OneToOne()
    @JoinColumn(name = "type")
    private Topic topic;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "warning_word_user",
            joinColumns = { @JoinColumn(name = "warning_word_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") })
    @JsonIgnore
    private List<User> user = new ArrayList<>();
}
