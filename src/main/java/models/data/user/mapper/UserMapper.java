package models.data.user.mapper;

import domain.exceptions.UserAlreadyExists;
import domain.exceptions.UserNotFound;
import domain.repositories.Loghmeh;
import domain.entity.User;
import java.util.ArrayList;

public  class UserMapper{
    private static UserMapper ourInstance = new UserMapper();

    public static UserMapper getInstance() {
        return ourInstance;
    }

    public boolean validateUser(String username, String password){
        ArrayList<User> users= Loghmeh.getInstance().getUsers();
        for(int i=0;i<users.size();i++){
            if(users.get(i).getUsername().equals(username) && users.get(i).getUserPassword().equals(password)){
                return true;
            }
        }
        return false;
    }

    public void registerNewUser(User newUser) throws UserAlreadyExists {
        if(find(newUser.getUsername()) != null) {
            throw new UserAlreadyExists();
        }
        insert(newUser);
    }

    private User find(String username){
        ArrayList<User> users= Loghmeh.getInstance().getUsers();
        for(int i=0;i<users.size();i++){
            if(users.get(i).getUsername().equals(username)){
                return users.get(i);
            }
        }
        return null;
    }

    private void insert(User new_user){
        Loghmeh.getInstance().addUser(new_user);
    }



    public User getUserById(String id) throws UserNotFound {
        User user;
        user = find(id);
        if(user != null)
            return user;
        else {
            throw new UserNotFound();
        }
    }
}
