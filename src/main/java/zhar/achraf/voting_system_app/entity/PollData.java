package zhar.achraf.voting_system_app.entity;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PollData {

    private String question;

    private List<String> options;
}
