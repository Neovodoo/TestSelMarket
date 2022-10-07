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

            //задаю ожидание, чтобы страница успевала прогружаться
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));

            //окрываю браузер на весь экран и перехожу по ссылке
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

            //Ещё одна задержка (на всякий случай)
            Thread.sleep(5000);


            //Создаю список из имеющихся элементов на странице
            List<WebElement> search_results = driver.findElements(By.xpath("//article[@class='_2vCnw cia-vs cia-cs']"));


            //Условие для проверки того, что на странице более 10 элементов
            if (search_results.size()>=10) {

                //Запоминаю первый элемент на странице
                Thread.sleep(1000);
                WebElement first_smartphone = driver.findElement(By.xpath("//span[@data-tid='2e5bde87']"));

                //Преобразовываю первый элемент в текстовое значение
                String  check = first_smartphone.getText();
                System.out.println("Выбранный смартфон: " + check);

                //Переход к другой системе ранжирования (по рейтингу)
               WebElement sorting = driver.findElement(By.xpath("/html/body/div[4]/div[2]/div[3]/div[5]/div/div/div/div/div/div/div[3]/div/div/div/div[1]/div/div/div/button[3]"));
               move.moveToElement(sorting).click().build().perform();
               Thread.sleep(5000);

                //Локатор на конец страницы (список страниц)
                WebElement ending_of_page = driver.findElement(By.xpath("/html/body/div[4]/div[2]/div[3]/div[5]/div/div/div/div/div/div/div[7]/div/div/div[2]"));

                //Локатор на кнопку для переключения страниц ("Показать ещё")
                WebElement new_page = driver.findElement(By.xpath("/html/body/div[4]/div[2]/div[3]/div[5]/div/div/div/div/div/div/div[7]/div/div/div[1]/div/button/span"));
                move.moveToElement(ending_of_page).build().perform();


                boolean present = false;

                //Цикл для перехода на следующую страницу списка (в случае если искомого объекта нет на первой)
                while (!present){

                    try {
                        //Поиск нужного элемента на странице по названию и переход на него
                        WebElement smartphone_needed = driver.findElement(By.linkText(check));
                        move.moveToElement(smartphone_needed).click().build().perform();

                        present = true;

                    } catch (NoSuchElementException e) {
                        //Переход на новую страницу (с прогрузкой всех элементов), если мы не находим нужный
                        Thread.sleep(1000);
                        move.moveToElement(new_page).click().build().perform();
                        Thread.sleep(1000);
                        move.moveToElement(ending_of_page).build().perform();
                        present = false;

                    }
                    if (present) {
                        break;
                    }
                }
            }

            //Переключение на открытую вкладку смартфона
            List<String> change_tabs = new ArrayList<>(driver.getWindowHandles());
            driver.switchTo().window(change_tabs.get(1));

            //Получение текстового значения рейтинга и его отображение
            WebElement raiting = driver.findElement(By.xpath("//span[@class='_2v4E8']"));
            String answer = raiting.getAttribute("innerText");

            //Вывод ответа и доп информации
            System.out.println("Значение рейтинга: " + answer);
            driver.quit();



        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }
    }

