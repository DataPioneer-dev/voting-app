package zhar.achraf.voting_system_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zhar.achraf.voting_system_app.entity.Option;
import zhar.achraf.voting_system_app.entity.Poll;
import zhar.achraf.voting_system_app.repo.PollRepo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PollService {

    @Autowired
    private PollRepo pollRepo;

    @Autowired
    private PredictionService predictionService;

    public Map<Poll, String> getPollPredictions() {
        List<Poll> polls = getAllPolls();
        Map<Poll, String> predictions = new HashMap<>();

        for (Poll poll : polls) {
            try {

                List<String> optionDescriptions = poll.getOptionList().stream()
                        .map(Option::getDescription)
                        .collect(Collectors.toList());


                String predictedOption = predictionService.predictWinningOption(
                        poll.getQuestion(),
                        optionDescriptions
                );
                predictions.put(poll, predictedOption);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return predictions;
    }

    public List<Poll> getAllPolls() {
        return pollRepo.findAll();
    }

    public Poll getPollById(int id) {
        return pollRepo.findById(id).orElse(null);
    }

    public void createPoll(Poll poll) {
        pollRepo.save(poll);
    }
}