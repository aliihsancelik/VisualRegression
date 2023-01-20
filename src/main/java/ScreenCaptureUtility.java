import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ScreenCaptureUtility {


    public static void prepareBaseline(WebDriver driver, String imageName) {
        Screenshot screen = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(driver);
        BufferedImage bi = screen.getImage();

        File file = new File(System.getProperty("user.dir") + "/src/images/baseline/" + imageName + ".png");
        try {
            ImageIO.write(bi, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void takePageScreenShot(WebDriver driver, String imageName) {
        Screenshot screen = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(driver);
        BufferedImage bi = screen.getImage();

        File file = new File(System.getProperty("user.dir") + "/src/images/screenshots/" + imageName + ".png");
        try {
            ImageIO.write(bi, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void takeElementScreenShot(WebDriver driver, String imageName, WebElement element) {
        Screenshot screen = new AShot().coordsProvider(new WebDriverCoordsProvider()).takeScreenshot(driver, element);
        BufferedImage bi = screen.getImage();

        File file = new File(System.getProperty("user.dir") + "/src/images/screenshots/" + imageName + ".png");
        try {
            ImageIO.write(bi, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static boolean areImagesEqual(String baseline, String screenshot) {
        BufferedImage imgBaseline = null;
        BufferedImage imgScreenshot = null;
        try {
            imgBaseline = ImageIO.read(new File(System.getProperty("user.dir") + "/src/images/baseline/" + baseline + ".png"));
            imgScreenshot = ImageIO.read(new File(System.getProperty("user.dir") + "/src/images/screenshots/" + screenshot + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageDiff imageDiff = new ImageDiffer().makeDiff(imgBaseline, imgScreenshot);
        boolean isDifferent = imageDiff.hasDiff();
        if (isDifferent) {
            BufferedImage diffImage = imageDiff.getMarkedImage();
            try {
                ImageIO.write(diffImage, "png", new File(System.getProperty("user.dir") + "/src/images/diffimages/" + screenshot + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return !isDifferent;
    }

}
