package ru.netology.carddeliveryorderTest.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class cardDeliveryOrderTest {

    public String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @BeforeEach
    void openURL() {
        open("http://localhost:9999");
        Configuration.holdBrowserOpen = true;
    }

    @Test
    void correctDataWithSuccessNotification() {
        $x(".//span[@data-test-id='city']//input").setValue("Санкт-Петербург");
        $x(".//span[@data-test-id='date']//input").doubleClick().sendKeys(Keys.DELETE);
        $x(".//span[@data-test-id='date']//input").setValue(generateDate(3));
        $x(".//span[@data-test-id='name']//input").setValue("Проверяло Проверяев");
        $x(".//span[@data-test-id='phone']//input").setValue("+79112223344");
        $("[data-test-id=\"agreement\"]").click();
        $(".button").click();
        $x("//*[contains(text(), \"Успешно!\")]").should(visible, Duration.ofSeconds(15));
        $(".notification__content").should(visible, Duration.ofSeconds(15)).should(exactText("Встреча успешно забронирована на " + generateDate(3)));
    }

    @Test
    void shouldWarnIfNameEmpty() {
        $x(".//span[@data-test-id='city']//input").setValue("Санкт-Петербург");
        $x(".//span[@data-test-id='date']//input").doubleClick().sendKeys(Keys.DELETE);
        $x(".//span[@data-test-id='date']//input").setValue(generateDate(3));
        $x(".//span[@data-test-id='phone']//input").setValue("+79112223344");
        $("[data-test-id=\"agreement\"]").click();
        $(".button").click();
        $x(".//span[@data-test-id='name']//span[@class='input__sub']").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldWarnIfNameinLatin() {
        $x(".//span[@data-test-id='city']//input").setValue("Санкт-Петербург");
        $x(".//span[@data-test-id='date']//input").doubleClick().sendKeys(Keys.DELETE);
        $x(".//span[@data-test-id='date']//input").setValue(generateDate(3));
        $x(".//span[@data-test-id='name']//input").setValue("Checky Well-Testing");
        $x(".//span[@data-test-id='phone']//input").setValue("+79112223344");
        $("[data-test-id=\"agreement\"]").click();
        $(".button").click();
        $x(".//span[@data-test-id='name']//span[@class='input__sub']").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldWarnIfCityInLatin() {
        $x(".//span[@data-test-id='city']//input").setValue("Saint-Petersburg");
        $x(".//span[@data-test-id='date']//input").doubleClick().sendKeys(Keys.DELETE);
        $x(".//span[@data-test-id='date']//input").setValue(generateDate(3));
        $x(".//span[@data-test-id='name']//input").setValue("Проверяло Проверяев");
        $x(".//span[@data-test-id='phone']//input").setValue("+79112223344");
        $("[data-test-id=\"agreement\"]").click();
        $(".button").click();
        $x(".//span[@data-test-id='city']//span[@class='input__sub']").should(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldNotConfirmCityOutOfTheList() {
        $x(".//span[@data-test-id='city']//input").setValue("Старая Русса");
        $x(".//span[@data-test-id='date']//input").doubleClick().sendKeys(Keys.DELETE);
        $x(".//span[@data-test-id='date']//input").setValue(generateDate(3));
        $x(".//span[@data-test-id='name']//input").setValue("Проверяло Проверяев");
        $x(".//span[@data-test-id='phone']//input").setValue("+79112223344");
        $("[data-test-id=\"agreement\"]").click();
        $(".button").click();
        $x(".//span[@data-test-id='city']//span[@class='input__sub']").should(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldWarnIfDateEmpty() {
        $x(".//span[@data-test-id='city']//input").setValue("Санкт-Петербург");
        $x(".//span[@data-test-id='date']//input").doubleClick().sendKeys(Keys.DELETE);
        $x(".//span[@data-test-id='name']//input").setValue("Проверяло Проверяев");
        $x(".//span[@data-test-id='phone']//input").setValue("+79112223344");
        $("[data-test-id=\"agreement\"]").click();
        $(".button").click();
        $x(".//span[@data-test-id='date']//span[@class='input__sub']").should(exactText("Неверно введена дата"));
    }

    @Test
    void shouldConfirmDateAfterThreeDays() {
        $x(".//span[@data-test-id='city']//input").setValue("Санкт-Петербург");
        $x(".//span[@data-test-id='date']//input").doubleClick().sendKeys(Keys.DELETE);
        $x(".//span[@data-test-id='date']//input").setValue(generateDate(10));
        $x(".//span[@data-test-id='name']//input").setValue("Проверяло Проверяев");
        $x(".//span[@data-test-id='phone']//input").setValue("+79112223344");
        $("[data-test-id=\"agreement\"]").click();
        $(".button").click();
        $x("//*[contains(text(), \"Успешно!\")]").should(visible, Duration.ofSeconds(15));
        $(".notification__content").should(visible, Duration.ofSeconds(15)).should(exactText("Встреча успешно забронирована на " + generateDate(10)));
    }

    @Test
    void shouldNotConfirmDateBeforeThreeDays() {
        $x(".//span[@data-test-id='city']//input").setValue("Санкт-Петербург");
        $x(".//span[@data-test-id='date']//input").doubleClick().sendKeys(Keys.DELETE);
        $x(".//span[@data-test-id='date']//input").setValue(generateDate(1));
        $x(".//span[@data-test-id='name']//input").setValue("Проверяло Проверяев");
        $x(".//span[@data-test-id='phone']//input").setValue("+79112223344");
        $("[data-test-id=\"agreement\"]").click();
        $(".button").click();
        $x(".//span[@data-test-id='date']//span[@class='input__sub']").should(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldWarnIfPhoneEmpty() {
        $x(".//span[@data-test-id='city']//input").setValue("Санкт-Петербург");
        $x(".//span[@data-test-id='date']//input").doubleClick().sendKeys(Keys.DELETE);
        $x(".//span[@data-test-id='date']//input").setValue(generateDate(3));
        $x(".//span[@data-test-id='name']//input").setValue("Проверяло Проверяев");
        $x(".//span[@data-test-id='phone']//input").hover();
        $("[data-test-id=\"agreement\"]").click();
        $(".button").click();
        $x(".//span[@data-test-id='phone']//span[@class='input__sub']").should(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldWarnIfPhoneFormatWrong() {
        $x(".//span[@data-test-id='city']//input").setValue("Санкт-Петербург");
        $x(".//span[@data-test-id='date']//input").doubleClick().sendKeys(Keys.DELETE);
        $x(".//span[@data-test-id='date']//input").setValue(generateDate(3));
        $x(".//span[@data-test-id='name']//input").setValue("Проверяло Проверяев");
        $x(".//span[@data-test-id='phone']//input").setValue("+(8)81244454");
        $("[data-test-id=\"agreement\"]").click();
        $(".button").click();
        $x(".//span[@data-test-id='phone']//span[@class='input__sub']").should(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }
    @Test
    void shouldWarnIfAgreementBoxEmpty() {
        $x(".//span[@data-test-id='city']//input").setValue("Санкт-Петербург");
        $x(".//span[@data-test-id='date']//input").doubleClick().sendKeys(Keys.DELETE);
        $x(".//span[@data-test-id='date']//input").setValue(generateDate(3));
        $x(".//span[@data-test-id='name']//input").setValue("Проверяло Проверяев");
        $x(".//span[@data-test-id='phone']//input").setValue("+79112223344");
        $("[data-test-id=\"agreement\"]").hover();
        $(".button").click();
        $("[data-test-id=agreement].input_invalid").should(exist);

        //$x("//*[text()='Я соглашаюсь с условиями обработки и использования моих персональных данных']").shouldHave(cssValue("color", "rgba(255, 92, 92, 1)"));
    }
}
