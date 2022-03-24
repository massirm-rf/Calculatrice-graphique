cd ../;
javac --module-path $PWD/../JavaFx/JavaFxLinux/javafx-sdk-17/lib --add-modules javafx.controls,javafx.fxml application/*.java;
java --module-path $PWD/../JavaFx/JavaFxLinux/javafx-sdk-17/lib --add-modules javafx.controls,javafx.fxml application.Main;
rm -r application/*.class


