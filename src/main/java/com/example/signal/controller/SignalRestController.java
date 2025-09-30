package com.example.signal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.signal.dto.SignalStatus;
import com.example.signal.rmi.LoadBalancerInterface;
import com.example.signal.rmi.SignalControlInterface;
import com.example.signal.utils.DBHelper;
@RestController
@RequestMapping("/signals")
public class SignalRestController {


    @Autowired
    private DBHelper dbHelper;

    private int currentGreen = 1;
    //private final SignalControlInterface controller;
    private final LoadBalancerInterface controller;
    public SignalRestController(LoadBalancerInterface controller) {
    this.controller = controller;
}
    
    /*public SignalRestController(SignalControlInterface controller) {
        this.controller = controller;
    }*/

   @GetMapping("/status")
public SignalStatus getStatus() throws Exception {
    String status = controller.getGlobalStatus(); // ✅ from LoadBalancer
    // Parse one of the controller statuses (first one is enough)
    if (status.contains("GREEN") && status.contains("RED")) {
        if (status.contains("1 & 2: GREEN")) return new SignalStatus("green", "red");
        if (status.contains("3 & 4: GREEN")) return new SignalStatus("red", "green");
    }
    if (status.contains("YELLOW")) {
        if (status.contains("1 & 2: YELLOW")) return new SignalStatus("yellow", "red");
        if (status.contains("3 & 4: YELLOW")) return new SignalStatus("red", "yellow");
    }
    return new SignalStatus("red", "red");
}



    @PostMapping("/request/{group}")
    public String requestGreen(@PathVariable int group) throws Exception {
        var currentGroup = group == 1 ? "1 & 2" : "3 & 4";
         if(group == currentGreen) return "Already green!";
        dbHelper.saveManipulatorLog(currentGroup, "Manual Request", "Operator");
        dbHelper.saveControllerLog(currentGroup, "MANUAL REQUEST");
        dbHelper.savePedestrianLog(true, currentGroup);
        currentGreen = group;
       
        return controller.requestGreen(group);
           //     dbHelper.saveControllerLog(group, "MANUAL REQUEST");
        //     dbHelper.saveManipulatorLog(nextGroup, (vip ? "VIP OVERRIDE" : "AUTO SWITCH"), requestedBy);
        //     dbHelper.savePedestrianLog(true, nextGroup);
    }

    @PostMapping("/vip/{group}")
    public String vipOverride(@PathVariable int group) throws Exception {
       currentGreen = group;
        var currentGroup = group == 1 ? "1 & 2" : "3 & 4"; 
       dbHelper.saveManipulatorLog(currentGroup, "VIP override", "System");
      dbHelper.saveControllerLog(currentGroup, "VIP OVERRIDE");
      dbHelper.savePedestrianLog(true, currentGroup);
       return controller.manualOverride(group);

    }
}

/*
 *
    public SignalRestController(SignalControlInterface controller) {
        this.controller = controller;
    }

    @GetMapping("/status")
    public String getStatus() throws Exception {
      return currentGreen == 1 ? "Signal 1 & 2: GREEN | Signal 3 & 4: RED" : "Signal 1 & 2: RED | Signal 3 & 4: GREEN";
    }

    @PostMapping("/request/{group}")
    public String requestGreen(@PathVariable int group) throws Exception {
        if(group == currentGreen) return "Already green!";
        dbHelper.saveManipulatorLog("Group " + group, "Request green", "User");
        currentGreen = group;
        return "Green signal granted to group " + group;
    }

    @PostMapping("/vip/{group}")
    public String vipOverride(@PathVariable int group) throws Exception {
         currentGreen = group;
        dbHelper.saveManipulatorLog("Group " + group, "VIP override", "VIP");
        return "VIP Override applied to group " + group;
    } 
 else if(status.contains("1 & 2: YELLOW") && status.contains("3 & 4: RED")){
 return new SignalStatus("yellow", "red");
 
    }else if(status.contains("1 & 2: RED") && status.contains("3 & 4: YELLOW")){
 return new SignalStatus("red", "yellow");
    } 
    */