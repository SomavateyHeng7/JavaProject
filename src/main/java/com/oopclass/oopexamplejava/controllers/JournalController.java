package com.oopclass.oopexamplejava.controllers;

import com.oopclass.oopexamplejava.models.Journal;
import com.oopclass.oopexamplejava.repositories.JournalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class JournalController {

    @Autowired
    private JournalRepository journalRepository;

    @GetMapping("/")
    public String getLatestJournals(Model model) throws ParseException {
        Iterable<Journal> journals = journalRepository.findAll();
        ArrayList<Journal> journalArrayList = new ArrayList<>();
        for (Journal journal: journals)
        {
           journalArrayList.add(journal);
        }
        if (journalArrayList.size()>0) //Give html file a journal which is null,check if journal title or content is null, if so it does not display
        {
            journalArrayList.sort(Comparator.comparing(Journal::getJournal_date));
            Collections.reverse(journalArrayList);
            model.addAttribute("journal", journalArrayList.get(0));

        }
        else
        {
            model.addAttribute("journal", new Journal());
        }
        return "index";
    }
    @GetMapping("/journals")
    public String getAllJournals(Model model){ //Check Journal idGroup is empty if empty it is original journal
        Iterable<Journal> journals = journalRepository.findAll(); // Find idgroup for original journal
        ArrayList<Journal> currentJournals = new ArrayList<>(); // save them to list or history
        for (Journal journal: journals )
        {
            if(journal.getIdGroup()==null) //check entire list and compare journal date and the latest version of journal
            {
                Iterable<Journal> histories = journalRepository.findByIdGroup(journal.getId()); //then will add to current journal
                Journal latestJournal = journals.iterator().next(); //if history is 0, then add journal to currentJournals
                int count=0;
                for (Journal history: histories)
                {
                    if (history.getJournal_date().compareTo(latestJournal.getJournal_date())>0) //compare date for latest
                    {
                        latestJournal=history;
                        count++;
                    }
                }
                if(count>0)
                {
                    currentJournals.add(latestJournal);
                }
                else
                {
                    currentJournals.add(journal);
                }
            }
        }
        if (currentJournals.size()>0) //sort journal with journal date
        {
            currentJournals.sort(Comparator.comparing(Journal::getJournal_date));
            Collections.reverse(currentJournals);
        }
        model.addAttribute("journals", currentJournals);
        return "alljournals";
    }
    @GetMapping("/journal")
    public String getJournal(@RequestParam(name = "id",required = false) Long id ,Model model){ // find idGroup by journal ID
        Iterable<Journal> journals = journalRepository.findByIdGroup(id);
        Journal latestJournal = null; // if nth found then history 0
        int count=0;
        ArrayList<Journal> journalList = new ArrayList<Journal>();
        for (Journal journal: journals) //set original journal to latest journal, display it in html
        {
            journalList.add(journal);
            if (count==0) //latest journal
            {
                latestJournal=journal;
                count++;
            }
            else
            {
                if (journal.getJournal_date().compareTo(latestJournal.getJournal_date())>0) //compare date for the latest
                {
                    latestJournal=journal;
                }
            }
            
        }
        Journal originalJournal = journalRepository.findById(id).orElseThrow(); //if found history it will compare the date and set the latest one as that one
        if(count==0)                                                               // add the found journal to history list and give it to html
        {
            latestJournal=originalJournal;
        }

        if (journalList.size()>0) //Sorting Date for Journal
        {
            journalList.add(originalJournal);
            journalList.sort(Comparator.comparing(Journal::getJournal_date));
            Collections.reverse(journalList);
        }
        model.addAttribute("journal", latestJournal);
        model.addAttribute("history", journalList);
        return "journal";
    }

    @PostMapping("/journal/edit")
    public String editJournal( //
                                @RequestParam String journal_content,
                                @RequestParam String journal_title,
                                @RequestParam Long id,
                                Model model){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"); //Current date time
        LocalDateTime now = LocalDateTime.now();
        String journal_date = now.toString();
        Journal journal = new Journal(id,journal_date, journal_content,journal_title);
        journalRepository.save(journal);

        return "redirect:/journal?id="+id;
    }

    @PostMapping("/journal/add")
    public String addJournal( //adding journal
            @RequestParam String journal_content,
            @RequestParam String journal_title,
            Model model){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"); //Current date time
        LocalDateTime now = LocalDateTime.now();
        String journal_date = now.toString();
        Journal journal = new Journal(journal_date, journal_content,journal_title);
        journalRepository.save(journal);

        return "redirect:/";
    }
    @PostMapping("/journal/delete")
    public String deleteJournal(@RequestParam(name = "id",required = false) Long id ,Model model){
        if(!journalRepository.existsById(id)){
            return "redirect:/journals";
        }
        Iterable<Journal> journals = journalRepository.findByIdGroup(id);
        journalRepository.deleteAll(journals);
        Journal journal = journalRepository.findById(id).orElseThrow();
        journalRepository.delete(journal);
        return "redirect:/journals";
    }





//    @PostMapping("/journal/add")
//    public String addNewJournal(
//                                @RequestParam String journal_content,
//                                @RequestParam String journal_title,
//                                Model model){
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"); //Current date time
//        LocalDateTime now = LocalDateTime.now();
//        String journal_date = now.toString();
//        Journal journal = new Journal(journal_date, journal_content,journal_title);
//        journalRepository.save(journal);
//
//        return "redirect:/journals";
//    }
//    @PostMapping("/journal/edit")
//    public String editJournal1(
//            @RequestParam String journal_content,
//            @RequestParam String journal_title,
//            @RequestParam Long journal_id,
//            Model model){
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"); //Current Date time
//        LocalDateTime now = LocalDateTime.now();
//        String journal_date = now.toString();
//        Journal journal = new Journal(journal_id, journal_date, journal_content,journal_title);
//        journalRepository.save(journal);
//
//        return "redirect:/journals";
//    }
//
//
//    @GetMapping("/journaladd")
//    public String addNewJournal(Model model){
//        return "journaladd";
//    }
//
//    @GetMapping("/journal")
//    public String getJournal(@RequestParam(name = "id",
//            required = false) Long id ,Model model){
//        if(!journalRepository.existsById(id)){
//            return "redirect:/journals";
//        }
//        Journal journal = journalRepository.findById(id).orElseThrow();
//        model.addAttribute("journal", journal);
//        return "journal";
//    }
//
//    @GetMapping("/journal{id}/edit")
//    public String editJournal(@PathVariable(value = "id") Long id ,Model model){
//        if(!journalRepository.existsById(id)){
//            return "redirect:/journals";
//        }
//        Journal journal = journalRepository.findById(id).orElseThrow();
//        model.addAttribute("journal", journal);
//        return "journaledit";
//    }
//
//
//    @PostMapping("/journal{id}/delete")
//    public String deleteJournal(@PathVariable(value = "id") Long id,
//                              Model model){
//        if(!journalRepository.existsById(id)){
//            return "redirect:/journals";
//        }
//        Journal journal = journalRepository.findById(id).orElseThrow();
//        journalRepository.delete(journal);
//        return "redirect:/journals";
//    }

}
