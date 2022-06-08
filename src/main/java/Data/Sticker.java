package Data;

import java.util.ArrayList;
import java.util.List;

import Application.StartApplication;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Sticker {
    public static ArrayList<String> allStickers = new ArrayList<>(List.of("angry", 
                                                                    "cool",
                                                                    "coolsmile",
                                                                    "crazy",
                                                                    "creeper",
                                                                    "cry",
                                                                    "cute",
                                                                    "happy",
                                                                    "hithere",
                                                                    "ojej",
                                                                    "rock",
                                                                    "smile",
                                                                    "tcs",
                                                                    "thecoolest",
                                                                    "tnt",
                                                                    "uch",
                                                                    "verycool",
                                                                    "wtf"
                                                                    ));
    public static ImageView getSticker(String text) {
        ImageView striker = new ImageView(new Image(String.valueOf(StartApplication.class.getResource("Stickers/" + text.substring(1, text.length()) + ".png")), 100, 100, false, false));
        return striker;
    }
}
