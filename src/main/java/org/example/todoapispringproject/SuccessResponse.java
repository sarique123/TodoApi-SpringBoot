package org.example.todoapispringproject;

public class SuccessResponse {
    private String message;
    private int statusCode;

    public SuccessResponse(String string,int statusCode){
        this.message = string;
        this.statusCode = statusCode;
    }

    public String getMessage(){
        return message;
    }

    public int getStatusCode(){
        return statusCode;
    }

    public void setString(String message){
        this.message = message;
    }

    public void setStatusCode(int statusCode){
        this.statusCode = statusCode;
    }
}
