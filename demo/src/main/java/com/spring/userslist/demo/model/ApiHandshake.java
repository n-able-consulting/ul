package com.spring.userslist.demo.model;

public class ApiHandshake {

    private String version;

    private String name;

    private String companymessage;

    public ApiHandshake() {
        super();
        this.version = "1.0";
        this.name = "UserList";
        this.companymessage = "N-Able Company Demo Application";
    }
    public String getVersion() {
        return this.version;
    }
    public String getName() {
        return this.name;
    }
    public String getCompanymessage() {
        return this.companymessage;
    }

    public String toJSON()
    {
        // Returning attributes of organisation
        String json = "{ \"Application\" : { \"application_name\" : \""
            + this.name
            + "\", \"version\" : \"" + this.version
            + "\", \"company_message\" : \"" + this.companymessage + "\"}}";
        return json;
    }

    @Override public String toString()
    {
        // Returning attributes of organisation
        return "Application [application_name="
            + this.name
            + ", version=" + this.version
            + ", company_message=" + this.companymessage + "]";
    }
}