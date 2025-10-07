module aclisn2025minesrabat.lo3ba {
    requires javafx.controls;
    requires javafx.fxml;

    exports aclisn2025minesrabat.lo3ba;
    exports aclisn2025minesrabat.lo3ba.controller;

    opens aclisn2025minesrabat.lo3ba.controller to javafx.fxml;
}
