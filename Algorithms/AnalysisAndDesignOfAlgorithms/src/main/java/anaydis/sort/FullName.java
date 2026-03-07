package anaydis.sort;

import java.util.*;



public class FullName {
    private String firstname;
    private String lastname;

    public FullName(String firstname, String lastname){
        this.firstname = firstname;
        this.lastname = lastname;
    }
    public String getFirstname(){
        return firstname;
    }
    public String getLastname(){
        return lastname;
    }
    public List<String> LastName(List<String> lastname){
        AbstractSorter Sorter = new AbstractSorter.SelectionSorter();
        Comparator cmp = Comparator.naturalOrder();
        Sorter.sort(cmp, lastname);
        return lastname;
    }
    public List<String> FirstName(List<String> firstname){
        AbstractSorter Sorter = new AbstractSorter.SelectionSorter();
        Comparator cmp = Comparator.naturalOrder();
        Sorter.sort(cmp, firstname);
        return firstname;
    }
}
