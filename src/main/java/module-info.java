module ca.qc.bdeb.sim.tp2camelotvelo {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.compiler;


    opens ca.qc.bdeb.sim.tp2camelotvelo to javafx.fxml;
    exports ca.qc.bdeb.sim.tp2camelotvelo;
}