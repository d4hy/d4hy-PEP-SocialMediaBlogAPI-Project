package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.*;
public class MessageDAO {

  public Message insertMessage(Message message){
      Connection connection = ConnectionUtil.getConnection();
      String sql = "INSERT INTO message( posted_by,message_text, time_posted_epoch) VALUES(?,?,?);";
      try{
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, message.getPosted_by());
        ps.setString(2, message.getMessage_text());
        ps.setLong(3,message.getTime_posted_epoch());
      
        ps.executeUpdate();

        ResultSet pkeyResultSet = ps.getGeneratedKeys();
        if(pkeyResultSet.next()){
          return new Message(pkeyResultSet.getInt(1), message.getPosted_by(), message.getMessage_text(),message.getTime_posted_epoch());
        }

      }catch(SQLException e){
        throw new RuntimeException("Could not insert message", e);
      }
      return null;
  }
}
