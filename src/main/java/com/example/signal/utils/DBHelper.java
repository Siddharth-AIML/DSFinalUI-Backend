package com.example.signal.utils;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DBHelper {

    private final JdbcTemplate controllerJdbc;
    private final JdbcTemplate manipulatorJdbc;
    private final JdbcTemplate pedestrianJdbc;

    public DBHelper(@Qualifier("controllerJdbcTemplate") JdbcTemplate controllerJdbc,
                    @Qualifier("manipulatorJdbcTemplate") JdbcTemplate manipulatorJdbc,
                    @Qualifier("pedestrianJdbcTemplate") JdbcTemplate pedestrianJdbc) {
        this.controllerJdbc = controllerJdbc;
        this.manipulatorJdbc = manipulatorJdbc;
        this.pedestrianJdbc = pedestrianJdbc;
    }

    // --- Save to SignalControllerDB ---
    public void saveControllerLog(String group, String action) {
        String sql = "INSERT INTO SignalLogs (signal_group, action) VALUES (?, ?)";
        controllerJdbc.update(sql, group, action);
        System.out.println("✅ Saved in SignalControllerDB.");
    }

    // --- Save to SignalManipulatorDB ---
    public void saveManipulatorLog(String signalGroup, String action, String requestedBy) {
        String sql = "INSERT INTO ManipulatorLogs (signal_group, action, requested_by) VALUES (?, ?, ?)";
        manipulatorJdbc.update(sql, signalGroup, action, requestedBy);
        System.out.println("✅ Saved in SignalManipulatorDB.");
    }

    // --- Save to PedestrianSignalDB ---
    public void savePedestrianLog(boolean request, String signalGroup) {
        String sql = "INSERT INTO pedestrianlogs (pedestrian_request, signal_group) VALUES (?, ?)";
        pedestrianJdbc.update(sql, request, signalGroup);
        System.out.println("✅ Saved in PedestrianSignalDB.");
    }
}
