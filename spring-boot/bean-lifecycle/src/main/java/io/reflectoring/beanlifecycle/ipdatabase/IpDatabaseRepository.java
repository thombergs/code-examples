package io.reflectoring.beanlifecycle.ipdatabase;

public class IpDatabaseRepository {

    private String file;

    public IpDatabaseRepository(String file) {
        this.file = file;
        // some logic to download and update the new database file
    }

    public String lookup(String ipAddress){
        // hardcoded for examplary purposes
        return "DE";
    }
}
