package zhar.achraf.voting_system_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zhar.achraf.voting_system_app.entity.Option;
import zhar.achraf.voting_system_app.repo.OptionRepo;

@Service
public class OptionService {

    @Autowired
    private OptionRepo optionRepo;

    public void createVote(int optionId) {

        Option option = optionRepo.findById(optionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Option ID!"));
        option.setVotes(option.getVotes() + 1);
        optionRepo.save(option);
    }
}