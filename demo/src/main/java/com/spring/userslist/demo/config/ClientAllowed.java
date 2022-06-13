package com.spring.userslist.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "corsclient")
public class ClientAllowed {
    private String hostname;
    private int port;
    private String protocol;
    private String methods;
    private String origin;
    private Boolean enabled;

    public String getProtocol() {
        return this.protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Boolean isEnabled() {
        return this.enabled;
    }

    public Boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getprotocol() {
        return this.protocol;
    }

    public void setprotocol(String protocol) {
        this.protocol = protocol;
    }

    public String getMethods() {
        return this.methods;
    }

    public void setMethods(String methods) {
        this.methods = methods;
    }

    public String getOrigin() {
        String rOrigin = null;
        if (this.origin == null || this.origin.isEmpty()) {
            if (this.hostname.equals("*")) {
                rOrigin = "*";
            } else {
                rOrigin = this.protocol + "://" + this.hostname + ":" + this.port;
            }
        }
        else {
            rOrigin = this.origin;
        }
        //System.out.println("within ClientAllowed, origin is: " + rOrigin);
        return rOrigin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
    
}