package nl.inholland.librarysystemjavafx.data;

import javafx.scene.control.Label;
import nl.inholland.librarysystemjavafx.model.*;

import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private List<User> users = new ArrayList<>();
    private List<Item> collection = new ArrayList<>();
    private List<Member> members = new ArrayList<>();

    public List<User> getUsers() { return users; }
    public List<Item> getCollection() { return collection; }
    public List<Member> getMembers() { return members; }

    public Database() {
        addUsers();
        addCollection();
        addMembers();
    }

    public void writeDataToFile(){
        writeCollectionToFile();
        writeMembersToFile();
    }

    public void addUsers() {
        users.add(new User("user", "1234", "Xander", "Goi"));
        users.add(new User("admin", "1234", "Aizaz", "Ahsan"));
    }

    public void addCollection() {
        if (!loadCollection()){
            collection.add(new Item(1, true, "Java for Dummies, 13th Edition", "Vries, E. de"));
            collection.add(new Item(2, true, "UI Design, 2nd Edition", "Clara Hampton"));
            collection.add(new Item(3, true, "Java for Amateurs, 10th Edition", "Vries, E. de"));
            collection.add(new Item(4, true, "ITIL, 3rd Edition", "Vries, E. de"));
            collection.add(new Item(5, true, "Java for Dummies, 13th Edition", "Vries, E. de"));
        }
    }

    public void addMembers() {
        if (!loadMembers()){
            members.add(new Member(1, "Piet", "de Vries", LocalDate.of(2000,6, 21)));
            members.add(new Member(2, "Mark", "Zack", LocalDate.of(1992,1, 12)));
            members.add(new Member(3, "Thijs","Otter", LocalDate.of(1996,7, 06)));
            members.add(new Member(4, "Gerwin","van Dijk", LocalDate.of(1989,8, 19)));
            members.add(new Member(5, "Niels", "van der Zwet", LocalDate.of(1994,3, 29)));
        }
    }

    public boolean isItemCodeVerified(List<Item> collection, int itemCode){
        for(Item item : collection){
            if(item.getItemCode() == itemCode){
                return true;
            }
        }
        return false;
    }
    public boolean isItemLent(int itemCode){
        for(Item item : collection){
            if(item.getItemCode() == itemCode && item.getIsAvailable()){
                item.setIsAvailable(false);
                item.setLendingDate(LocalDate.now());
                return true;
            }
        }
        return false;
    }
    public boolean itemReceived(List<Item> collection, int itemCode, Label lblReceiveItem){
        for(Item item : collection){
            if(item.getItemCode() == itemCode && !item.getIsAvailable()){

                long timeOfLending = ChronoUnit.DAYS.between(item.getLendingDate(), LocalDate.now());

                if(timeOfLending > 21){
                    int extraDay = Math.toIntExact(timeOfLending) - 21;
                    lblReceiveItem.setText("Retrieved successfully " + extraDay + " days later!");
                }
                else{
                    lblReceiveItem.setText("Retrieved successfully!");
                }
                item.setIsAvailable(true);
                item.setLendingDate(null);

                return true;
            }
        }
        return false;
    }
    public boolean isMemberVerified(List<Member> members, int memberIdentifier){
        for(Member Member : members){
            if(Member.getMemberId() == memberIdentifier){
                return true;
            }
        }
        return false;
    }

    public void writeCollectionToFile() {
        try {
            ObjectOutputStream oOSCollection = new ObjectOutputStream(new FileOutputStream
                    ("collection.dat"));
                for (Item item : collection) oOSCollection.writeObject(item);
            oOSCollection.close();

        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void readCollectionFromFile(ObjectInputStream ois) {
        try {
            Item item = (Item) ois.readObject();
            do {
                try {
                    if (item != null) {
                        collection.add(item);
                    }
                    item = (Item) ois.readObject();
                } catch (Exception e){
                    break;
                }
            } while (item != null);
            ois.close();
        }
        catch (IOException | ClassNotFoundException ioe) {
            ioe.printStackTrace();
        }

    }
    public boolean loadCollection() {
        try (
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream
                        ("collection.dat"))) {
            readCollectionFromFile(ois);
        } catch (FileNotFoundException fnfe) {
            return false;
        } catch (IOException ioe) {
            ioe.printStackTrace();

        }
        return true;
    }

    public void writeMembersToFile() {
        try {
            ObjectOutputStream oOS = new ObjectOutputStream(new FileOutputStream
                        ("members.dat"));
            for (Member member : members) oOS.writeObject(member);
            oOS.close();

        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void readMembersFromFile(ObjectInputStream ois) {
        try {
            Member member = (Member) ois.readObject();
            do {
                try {
                    if (member != null) {
                        members.add(member);
                    }
                    member = (Member) ois.readObject();

                } catch (Exception e){
                    break;
                }
            } while (member != null);
            ois.close();
        }
        catch (IOException | ClassNotFoundException ioe) {
            ioe.printStackTrace();
        }


    }
    public boolean loadMembers() {
        try (
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream
                        ("members.dat"))) {
            readMembersFromFile(ois);
        } catch (FileNotFoundException fnfe) {
            return false;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return true;
    }

}
