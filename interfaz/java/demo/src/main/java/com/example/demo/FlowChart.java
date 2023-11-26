//import com.github.LeeReindeer.codeflow.core.CodeFlow;
//import com.github.LeeReindeer.codeflow.core.Format;
//
//
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.stage.Stage;
//
//public class FlowChart extends Application {
//
//    @Override
//    public void start(Stage primaryStage) {
//        // Configuración del CodeFlow
//        CodeFlow codeFlow = CodeFlow.builder()
//                .useNative(true)
//                .supportClass(false)
//                .failFast(true)
//                .workDir("examples")
//                .outDir("tests")
//                .format(Format.PNG)
//                .build();
//
//        // Uso del CodeFlow para parsear y generar un archivo de imagen
//        Image image = new Image(codeFlow.parse("if(ok){doSome();}else{doSomeElse();}").toImage());
//        ImageView imageView = new ImageView(image);
//
//        // Configuración de la escena
//        primaryStage.setScene(new Scene(imageView, 400, 400));
//        primaryStage.setTitle("JavaFX CodeFlow Example");
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
