package com.oopclass.oopexamplejava.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Journal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; //main id
    private Long idGroup; //id for edited journal
    private String journal_date;
    private String journal_content;

    private String journal_title;

    public Journal() {
    }
    //Journal
    public Journal(String journal_date, String journal_content,String journal_title) {
        this.journal_date = journal_date;
        this.journal_content = journal_content;
        this.journal_title = journal_title;
    }
    //Edit Journal
    public Journal(Long id,String journal_date, String journal_content,String journal_title) {
        this.journal_date = journal_date;
        this.journal_content = journal_content;
        this.journal_title = journal_title;
        this.idGroup = id;
    }

    public Long getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(Long idGroup) {
        this.idGroup = idGroup;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJournal_date() {
        return journal_date;
    }

    public void setJournal_date(String journal_date) {
        this.journal_date = journal_date;
    }

    public String getJournal_content() {
        return journal_content;
    }

    public void setJournal_content(String journal_content) {
        this.journal_content = journal_content;
    }

    public String getJournal_title() {
        return journal_title;
    }

    public void setJournal_title(String journal_title) {
        this.journal_title = journal_title;
    }
}
