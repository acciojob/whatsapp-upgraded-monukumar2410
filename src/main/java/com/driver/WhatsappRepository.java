package com.driver;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {
    
    private HashMap<String,User> userMap = new HashMap<>();
    private HashMap<Integer,Message> messageMap  = new HashMap<>();
   // private HashMap<User,List<Message>> userMessageMap = new HashMap<>();
    private HashMap<Group,List<User>> groupMap = new HashMap<>();
    private HashMap<Group,List<Message>> groupMessageMap = new HashMap<>();

    public int id=1;
    public int idx=1;

    public String createUser(String name, String mobile) throws Exception {
        if(userMap.containsKey(mobile)){
            throw new Exception("User already exists");
        }
         User user = new User(name,mobile);
         userMap.put(mobile,user);
         return "SUCCESS";
         

    }

    public Group createGroup(List<User> users){
        if(users.size()==2){
          Group group = new Group(users.get(1).getName(),2);
          groupMap.put(group,users);
          groupMessageMap.put(group,new ArrayList<>());
          return group;
          
        }
        else{

            Group group = new Group("Group "+idx,users.size());
            idx++;
            groupMap.put(group,users);
            groupMessageMap.put(group,new ArrayList<>());
            return group;      
        }
        
        
    }

    public int createMessage(String content){
           
           Message message = new Message(id, content);
           message.setTimestamp(new Date());
           messageMap.put(id,message);
           id++;
           return message.getId();

    }

    public int sendMessage(Message message, User sender, Group group) throws Exception{
       if(!groupMap.containsKey(group)){
        throw new Exception("Group does not exist");
       }

       List<User> users = groupMap.get(group);

       if(!users.contains(sender)){
        throw new Exception("You are not allowed to send message");
       }

       messageMap.put(message.getId(),message);
       List<Message> msgs = groupMessageMap.get(group);
       msgs.add(message);

       return msgs.size();
       

        

    }
   
    public String changeAdmin(User approver, User user, Group group) throws Exception{

        if(!groupMap.containsKey(group)){
            throw new Exception("Group does not exist");
        }

        if(groupMap.get(group).get(1).getMobile().equals(approver.getMobile())){
           throw new Exception("Approver does not have rights");
        }

        List<User> users = groupMap.get(group);
        if(!users.contains(user)){
            throw new Exception("User is not a participant");
        }

        users.add(0, user);

        return "SUCCESS";
    }

    public int removeUser(User user) throws Exception{
        boolean flag = false;
        Group group1 = null;
        for(Group group: groupMap.keySet()){
           if( groupMap.get(group).contains(user)){
            flag = true;
            group1 = group;
            break;
           }
        }
        if(!flag){
            throw new Exception("User not found");
        }
        if(groupMap.get(group1).get(0).equals(user)){
            throw new Exception("Cannot remove admin");
        }
        groupMap.get(group1).remove(user);
        return 100;


    }
    public String findMessage(Date start, Date end, int K) throws Exception{

        return "SUCCESS";
        
    }
}
