package com.cts.microservices.test;

import java.io.IOException;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;

public class TestScript {
	
	int iExitValue;
    String sCommandString;

    public void runScript(String command){
    	System.out.println("command .....: "+command);
        sCommandString = command;
        CommandLine oCmdLine = CommandLine.parse(sCommandString);
        System.out.println("oCmdLine .....: "+oCmdLine);
        
        DefaultExecutor oDefaultExecutor = new DefaultExecutor();
        oDefaultExecutor.setExitValue(0);
        try {
            iExitValue = oDefaultExecutor.execute(oCmdLine);
        } catch (ExecuteException e) {
            System.err.println("Execution failed.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("permission denied.");
            e.printStackTrace();
        }
        System.out.println("iExitValue .....: "+iExitValue);
    }

    public static void main(String args[]){
        TestScript testScript = new TestScript();
        testScript.runScript("sh /Users/Mrudul/Documents/workspace-gitlab/Janus-Reactive-Microservices/screening-service/docker/build.sh");
    }

}
