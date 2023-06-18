package com.example.azure.finalprojectazure.controllers;

import com.example.azure.finalprojectazure.repositories.AttractionRepository;
import com.example.azure.finalprojectazure.repositories.PostcardRepository;
import com.example.azure.finalprojectazure.services.IAppService;
import com.example.azure.finalprojectazure.services.ITourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;



@Controller
public class FrontendController {

    @Autowired
    private IAppService iAppService;
    @Autowired
    private ITourService iTourService;
    @Autowired
    private AttractionRepository attractionRepository;

    @Autowired
    private PostcardRepository postcardRepository;


    @GetMapping()
    public String home() {
        return "landing";
    }

    @GetMapping("/map")
    public String landingPage() {
        return "index";
    }


    @GetMapping("/translate")
    public String showTranslatePage() {
        return "logistic";
    }
        @PostMapping("/translator")
        public String translate(@RequestParam("text") String text, Model model) throws IOException, InterruptedException {
            String resultService = iAppService.translateTest(text);
            model.addAttribute("result", resultService);
            return "logistic";
        }

    @GetMapping("/audio")
    public String showAudioPage() {
        return "logistic";
    }

    @PostMapping("/getAudio")
    public String getAudio(@RequestParam("text") String text,Model model) {
        String resultService = iAppService.generateAudio(text);

        model.addAttribute("audioResult", resultService);

        return "logistic";
    }

    @RequestMapping ("/checkIn")
    public String showCheckInForm() {
        return "checkinForm";
    }

    @GetMapping ("/file")
    public String showFile() {
        return "file";
    }

    @GetMapping ("/memory")
    public String saveMemory() {
        return "postcard";
    }

    @GetMapping ("/search")
    public String search() {
        return "itinerary1";
    }

    @GetMapping ("/starting")
    public String first() {
        return "starting";
    }

    @GetMapping ("/showImage")
    public String image() {
        return "image";
    }

    @GetMapping ("/planner")
    public String showPlanner() {
        return "planner";
    }

    @GetMapping("/atractii")
    public String getAtractii(Model model) {
        model.addAttribute("atractii", attractionRepository.findAll());
        return"show-attraction"; // numele paginii Thymeleaf în care vei afișa atractiile
    }

    @GetMapping("/postcards")
    public String getPostcards(Model model) {
        model.addAttribute("postcards", postcardRepository.findAll());
        return"show-postcards"; // numele paginii Thymeleaf în care vei afișa atractiile
    }


}
