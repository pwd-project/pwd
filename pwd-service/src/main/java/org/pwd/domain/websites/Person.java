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

    /** stanowisko */
    private String position;

    //only for Hibernate
    Person() {
    }

    public Person(String forename, String surename, String position) {
        this.forename = forename;
        this.surename = surename;
        this.position = position;
    }

    public String getForename() {
        return forename;
    }

    public String getSurename() {
        return surename;
    }

    public String getPosition() {
        return position;
    }
}
