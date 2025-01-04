package zhar.achraf.voting_system_app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import zhar.achraf.voting_system_app.entity.Option;
import zhar.achraf.voting_system_app.entity.Poll;
import zhar.achraf.voting_system_app.entity.PollData;
import zhar.achraf.voting_system_app.entity.User;
import zhar.achraf.voting_system_app.service.OptionService;
import zhar.achraf.voting_system_app.service.PollService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import zhar.achraf.voting_system_app.service.UserService;

@Controller
public class PollController {

    @Autowired
    private PollService pollService;

    @Autowired
    private OptionService optionService;

    @Autowired
    private UserService userService;

    @GetMapping("/createPoll")
    public String showCreatePollPage(Model model) {
        PollData pollData = new PollData();
        pollData.setOptions(List.of("", ""));
        if (!model.containsAttribute("successMessage")) {
            model.addAttribute("successMessage", null);
        }

        model.addAttribute("pollData", pollData);
        return "createPoll";
    }

    @GetMapping("/polls")
    public String showPollsPage(@RequestParam(name = "selectedPollId", required = false) Integer selectedPollId, Model model) {
        List<Poll> polls = pollService.getAllPolls();
        model.addAttribute("polls", polls);

        Poll selectedPoll = null;
        if (selectedPollId != null) {
            selectedPoll = pollService.getPollById(selectedPollId);
        }
        model.addAttribute("selectedPoll", selectedPoll);

        return "polls";
    }


    @GetMapping("/index")
    public String homePage(Model model) throws JsonProcessingException {
        List<Poll> polls = pollService.getAllPolls();

        ObjectMapper objectMapper = new ObjectMapper();
        String pollsJson = objectMapper.writeValueAsString(polls);

        model.addAttribute("polls", polls);
        model.addAttribute("pollsJson", pollsJson);
        model.addAttribute("totalPolls", polls.size());


        long totalVotes = polls.stream()
                .flatMap(poll -> poll.getOptionList().stream())
                .mapToInt(Option::getVotes).sum();
        model.addAttribute("totalVotes", totalVotes);

        String pollEngagement = polls.isEmpty() ? "0%" : (totalVotes / polls.size()) + "%";
        model.addAttribute("pollEngagement", pollEngagement);


        Map<Poll, String> pollPredictions = pollService.getPollPredictions();
        model.addAttribute("pollPredictions", pollPredictions);

        return "index";
    }

    @GetMapping("/poll/{Id}")
    public String viewPoll(@PathVariable  int Id, Model model) {

        model.addAttribute("poll", pollService.getPollById(Id));
        return "poll";

    }

    @PostMapping("/poll")
    public String createPoll(@ModelAttribute PollData pollData, Model model) {
        Poll poll = new Poll();
        poll.setQuestion(pollData.getQuestion());
        List<Option> optionList = new ArrayList<>();

        for (String optionStr : pollData.getOptions()) {
            Option option = new Option();
            option.setDescription(optionStr);
            option.setPoll(poll);

            optionList.add(option);
        }

        poll.setOptionList(optionList);
        pollService.createPoll(poll);

        return "redirect:/createPoll";
    }

    @PostMapping("/vote")
    public String createVote(@RequestParam(required = false) List<Integer> optionIds,
                             @RequestParam(name = "selectedPollId", required = true) Integer selectedPollId,
                             Model model) {

        List<Poll> polls = pollService.getAllPolls();
        model.addAttribute("polls", polls);

        Poll selectedPoll = pollService.getPollById(selectedPollId);
        model.addAttribute("selectedPoll", selectedPoll);


        if (optionIds == null || optionIds.isEmpty()) {
            model.addAttribute("errorMessage", "You must select at least one option.");
            return "polls";
        }

        optionIds.forEach(optionService::createVote);
        model.addAttribute("successMessage", "Your vote has been successfully submitted!");

        return "polls";
    }

}
