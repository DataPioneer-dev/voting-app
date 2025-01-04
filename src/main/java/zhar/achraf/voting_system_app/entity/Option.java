package zhar.achraf.voting_system_app.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    private String description;
    private int votes;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "poll_id")
    private Poll poll;


}
