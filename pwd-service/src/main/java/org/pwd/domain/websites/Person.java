package org.pwd.domain.websites;

import javax.persistence.Embeddable;

/**
 * @author Kris
 * Created by Kris on 2015-09-09.
 */
@Embeddable
public class Person {
    private String forename;
    private String surename;
    private String jobTitle;

    //only for Hibernate
    Person() {
    }

    public Person(String forename, String surename, String jobTitle) {
        this.forename = forename;
        this.surename = surename;
        this.jobTitle = jobTitle;
    }

    public String getForename() {
        return forename;
    }

    public String getSurename() {
        return surename;
    }

    public String getJobTitle() {
        return jobTitle;
    }
}
