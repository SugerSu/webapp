package com.haoxuan.demo.Entity;

public class UserTable {
    private String id;

    private String first_name;
    private String last_name;

    private String username;
    private String password;

    private String account_created;
    private String account_updated;

    private String salt;

    public UserTable() {
    }

    public UserTable(String first_name, String last_name, String username, String password, String account_created, String account_updated, String salt) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.password = password;
        this.account_created = account_created;
        this.account_updated = account_updated;
        this.salt = salt;
    }

    public UserTable(String id, String first_name, String last_name, String username, String password, String account_created, String account_updated, String salt) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.password = password;
        this.account_created = account_created;
        this.account_updated = account_updated;
        this.salt = salt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccount_created() {
        return account_created;
    }

    public void setAccount_created(String account_created) {
        this.account_created = account_created;
    }

    public String getAccount_updated() {
        return account_updated;
    }

    public void setAccount_updated(String account_updated) {
        this.account_updated = account_updated;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
