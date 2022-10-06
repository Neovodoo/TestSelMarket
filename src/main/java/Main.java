import net.bytebuddy.asm.Advice;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "/Applications/IntelliJ IDEA CE.app/Contents/Resources/Chromedrivers/chromedriver");
//hecnfv kj[
        /*
        ChromeDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.get("https://dzen.ru/?yredirect=true");
        WebElement search = driver.findElement(By.xpath("//form[@aria-label='Поиск в интернете']"));
        Actions move = new Actions(driver);
        move.moveToElement(search).click().build().perform();
        WebElement market = driver.findElement(By.xpath("//html/body/form/div[1]/div/div/ul/li[2]/a/span[1]"));
        market.click();
        С Дзеном не получается взаимодеймствовать с помощью силениума и веб драйвера
        */

        //Выполнил с прямой ссылкой на Яндекс маркет
       try {
            ChromeDriver driver = new ChromeDriver();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
            driver.get("https://market.yandex.ru/");
            driver.manage().window().maximize();


            //переход в каталог
            WebElement catalog = driver.findElement(By.id("catalogPopupButton"));
            catalog.click();

            //переход в раздел электроники
            WebElement electronics = driver.findElement(By.linkText("Электроника"));
            Actions move = new Actions(driver);
            move.moveToElement(electronics).build().perform();

            //выбираем категорию смартфоны
            driver.findElement(By.linkText("Смартфоны")).click();

            //Максимально допустимая цена
            WebElement max_price = driver.findElement(By.xpath("//span[@data-auto='filter-range-max']//input[@class='_3qxDp'] "));
            max_price.sendKeys("20000");

            //Прожимаю выбор производителей
            driver.findElement(By.xpath("//*[@id='search-filters']/div/div[4]/div/div/div/div/div[4]/div/fieldset/div/div/div/div/div/div/div[1]/label/span/span[2]")).click();
            driver.findElement(By.xpath("//*[@id='search-filters']/div/div[4]/div/div/div/div/div[4]/div/fieldset/div/div/div/div/div/div/div[2]/label/span/span[2]")).click();
            driver.findElement(By.xpath("//*[@id='search-filters']/div/div[4]/div/div/div/div/div[4]/div/fieldset/div/div/div/div/div/div/div[3]/label/span/span[2]")).click();
            driver.findElement(By.xpath("//*[@id='search-filters']/div/div[4]/div/div/div/div/div[4]/div/fieldset/div/div/div/div/div/div/div[4]/label/span/span[2]")).click();
            driver.findElement(By.xpath("//*[@id='search-filters']/div/div[4]/div/div/div/div/div[4]/div/fieldset/div/div/div/div/div/div/div[5]/label/span/span[2]")).click();

            //Выбор диаганоли экрана (от)
            WebElement diagonal = driver.findElement(By.xpath("/html/body/div[4]/div[2]/div[3]/div[5]/div/div/div/div/aside/div/div[4]/div/div/div/div/div[13]/div/fieldset/div/div/div/span[1]/div/div/input"));
            diagonal.sendKeys("3");

            Thread.sleep(5000);



            List<WebElement> search_results = driver.findElements(By.xpath("//article[@class='_2vCnw cia-vs cia-cs']"));

            if (search_results.size()>=10) {
                WebElement first_smartphone = driver.findElement(By.xpath("//span[@data-tid='2e5bde87']"));
                Thread.sleep(5000);
                String  check = first_smartphone.getText();
                System.out.println(check);
               WebElement sorting = driver.findElement(By.xpath("/html/body/div[4]/div[2]/div[3]/div[5]/div/div/div/div/div/div/div[3]/div/div/div/div[1]/div/div/div/button[3]"));
               move.moveToElement(sorting).click().build().perform();
                Thread.sleep(5000);


                WebElement ending_of_page = driver.findElement(By.xpath("/html/body/div[4]/div[2]/div[3]/div[5]/div/div/div/div/div/div/div[7]/div/div/div[2]"));
                WebElement new_page = driver.findElement(By.xpath("/html/body/div[4]/div[2]/div[3]/div[5]/div/div/div/div/div/div/div[7]/div/div/div[1]/div/button/span"));
                move.moveToElement(ending_of_page).build().perform();
                int count = 1;

                boolean present = false;

                while (!present){

                    try {
                        WebElement smartphone_needed = driver.findElement(By.linkText(check));
                        move.moveToElement(smartphone_needed).click().build().perform();

                        present = true;

                    } catch (NoSuchElementException e) {
                        Thread.sleep(1000);
                        move.moveToElement(new_page).click().build().perform();
                        Thread.sleep(1000);
                        move.moveToElement(ending_of_page).build().perform();
                        present = false;
                        count++;
                    }
                    if (present) {
                        break;
                    }



                }





            }

            List<String> change_tabs = new ArrayList<>(driver.getWindowHandles());
            driver.switchTo().window(change_tabs.get(1));
            WebElement raiting = driver.findElement(By.xpath("//span[@class='_2v4E8']"));
            String answer = raiting.getAttribute("innerText");
            System.out.println(answer);
            System.out.println("Печать");
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }
    }

