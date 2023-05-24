package top.popko.agentdemo.handler.hookpoint.models.policy;
 
 public class PolicyException extends Exception {
   public PolicyException(String message) {
     super(message);
   }
   
   public PolicyException(String message, Throwable cause) {
     super(message, cause);
   }
 }


