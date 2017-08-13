package Game.View;

import Game.Controller.CreditsController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class CreditsPane extends Pane {
    private StackPane rootPane;
    private Pane pane = this;
    private CreditsController parent;
    private Text txtNames, txtCreditsTitle, txtCreditsPhoto;
    private Button btnBack;
    private ImageView ivShane, ivYentl, ivLucas, ivJel, ivProjectPhoto;
    Background background;

    public CreditsPane(CreditsController parent, StackPane rootPane){
        this.rootPane = rootPane;
        this.parent = parent;
        initComponents();
        background = new Background(rootPane, 350, 690);
        addComponents();
        addEventHandlers();
    }//Constructor: Creates a Pane and adds it to the StackPanel

    private void initComponents() {
        pane.setMaxSize(330, 640);
        pane.setPrefSize(330, 640);

        ivShane = new ImageView(new Image("file:../../assets/images/avatars/1.png"));
        ivYentl = new ImageView(new Image("file:../../assets/images/avatars/2.png"));
        ivLucas = new ImageView(new Image("file:../../assets/images/avatars/3.png"));
        ivJel = new ImageView(new Image("file:../../assets/images/avatars/4.png"));

        ImageView[] ivArray = {ivShane, ivJel, ivYentl, ivLucas};

        for(int i = 0; i < ivArray.length; i++){
            ivArray[i].setFitHeight(75);
            ivArray[i].setFitWidth(75);
            ivArray[i].setLayoutX(pane.getMaxWidth() / 2 - 145);
            ivArray[i].setLayoutY(pane.getMaxHeight() / 2 - 220 + (80 * i));
        }

        ivProjectPhoto = new ImageView(new Image("file:../../assets/projectfoto.jpg"));
        ivProjectPhoto.setFitHeight(75);
        ivProjectPhoto.setFitWidth(75);
        ivProjectPhoto.setLayoutX(pane.getMaxWidth() / 2 - 145);
        ivProjectPhoto.setLayoutY(pane.getMaxHeight() / 2 + 10);

        txtCreditsTitle = new Text("Made by these monsters:");
        txtCreditsTitle.setLayoutX(pane.getMaxWidth() / 2 - 125);
        txtCreditsTitle.setLayoutY(50);
        txtCreditsTitle.setId("creditsPaneTitle");

        txtNames = new Text("Shane \"Viking\" Deconinck\n" + "Yentl \"Hipster\" Jacobs\n" + "Lucas \"Croque\" Pirard\n" + "Jel \"Sleepy\" Sadones");
        txtNames.setLineSpacing(63);
        txtNames.setLayoutX(pane.getMaxWidth() / 2 - 40);
        txtNames.setLayoutY(pane.getMaxHeight() - 500);
        txtNames.setId("creditsPaneText");

        txtCreditsPhoto = new Text("Design resources gathered from Freepik.com");
        txtCreditsPhoto.setLayoutX(pane.getMaxWidth() / 2-163);
        txtCreditsPhoto.setLayoutY(pane.getMaxHeight() - 100);
        txtCreditsPhoto.setId("creditsPaneText");

        ImageView backIconView = new ImageView(new Image("file:../../assets/images/buttons/back.png"));

        btnBack = new Button();
        btnBack.setGraphic(backIconView);
        btnBack.setId("mainMenuButton");
        btnBack.setPrefSize(100, 50);
        btnBack.setLayoutX(pane.getMaxWidth() / 2 - 50);
        btnBack.setLayoutY(pane.getMaxHeight() / 2 + 250);
    }//Initializes the objects that are going to be put on the pane

    private void addComponents() {
        pane.getChildren().addAll(txtNames, txtCreditsTitle,btnBack, ivShane, ivYentl, ivLucas, ivJel, ivProjectPhoto, txtCreditsPhoto);
        rootPane.getChildren().add(pane);
    }//Adds this pane to the StackPanel

    private void addEventHandlers() {
        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                removeAllComponents();
                parent.invokeMenuController();
            }
        });
    }//Adds the event handlers for the button

    private void removeAllComponents() {
        rootPane.getChildren().removeAll(pane, background.getWood(), background.getBeige());
    }//Removes this panel from the StackPanel
}