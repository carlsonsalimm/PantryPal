package project;

import java.io.IOException;

import javafx.event.ActionEvent;

public interface LoginControllerInterface{
    public boolean handleSignInButton(ActionEvent event) throws IOException;
    public boolean handleCreateAccountButton(ActionEvent event) throws IOException;
}