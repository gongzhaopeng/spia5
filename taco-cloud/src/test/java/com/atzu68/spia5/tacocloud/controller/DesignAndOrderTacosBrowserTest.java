package com.atzu68.spia5.tacocloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class DesignAndOrderTacosBrowserTest {

    private static HtmlUnitDriver browser;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate rest;

    @BeforeClass
    public static void setup() {

        browser = new HtmlUnitDriver();
        browser.manage().timeouts()
                .implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterClass
    public static void closeBrowser() {
        browser.quit();
    }

    @Test
    public void testDesignATacoPage_HappyPath() {

        browser.get(homePageUrl());
        clickDesignATaco();
        assertLandedOnLoginPage();
        doRegistration("testuser", "testpassword");
        assertLandedOnLoginPage();
        doLogin("testuser", "testpassword");
        assertDesignPageElements();
        buildAndSubmitATaco("Basic Taco",
                "FLTO", "GRBF", "CHED", "TMTO", "SLSA");
        clickBuildAnotherTaco();
        buildAndSubmitATaco("Another Taco",
                "COTO", "CARN", "JACK", "LETC", "SRCR");
        fillInAndSubmitOrderForm();
        assertEquals(homePageUrl(), browser.getCurrentUrl());
        doLogout();
    }

    @Test
    public void testDesignATacoPage_EmptyOrderInfo() {

        browser.get(homePageUrl());
        clickDesignATaco();
        assertLandedOnLoginPage();
        doRegistration("testuser2", "testpassword");
        doLogin("testuser2", "testpassword");
        assertDesignPageElements();
        buildAndSubmitATaco("Basic Taco",
                "FLTO", "GRBF", "CHED", "TMTO", "SLSA");
        submitEmptyOrderForm();
        fillInAndSubmitOrderForm();
        assertEquals(homePageUrl(), browser.getCurrentUrl());
        doLogout();
    }

    @Test
    public void testDesignATacoPage_InvalidOrderInfo() {

        browser.get(homePageUrl());
        clickDesignATaco();
        assertLandedOnLoginPage();
        doRegistration("testuser3", "testpassword");
        doLogin("testuser3", "testpassword");
        assertDesignPageElements();
        buildAndSubmitATaco("Basic Taco",
                "FLTO", "GRBF", "CHED", "TMTO", "SLSA");
        submitInvalidOrderForm();
        fillInAndSubmitOrderForm();
        assertEquals(homePageUrl(), browser.getCurrentUrl());
        doLogout();
    }

    private void buildAndSubmitATaco(String name, String... ingredients) {

        assertDesignPageElements();

        Arrays.stream(ingredients).forEach((ingredient) ->
                browser.findElementByCssSelector(
                        MessageFormat.format("input[value=''{0}'']", ingredient))
                        .click());
        browser.findElementByCssSelector("input#name").sendKeys(name);
        browser.findElementByCssSelector("form#tacoForm").submit();
    }

    private void assertLandedOnLoginPage() {
        assertEquals(loginPageUrl(), browser.getCurrentUrl());
    }

    private void doRegistration(String username, String password) {

        browser.findElementByLinkText("here").click();
        assertEquals(registrationPageUrl(), browser.getCurrentUrl());
        browser.findElementByName("username").sendKeys(username);
        browser.findElementByName("password").sendKeys(password);
        browser.findElementByName("confirm").sendKeys(password);
        browser.findElementByName("fullname").sendKeys("Test McTest");
        browser.findElementByName("street").sendKeys("1234 Test Street");
        browser.findElementByName("city").sendKeys("Testville");
        browser.findElementByName("state").sendKeys("TX");
        browser.findElementByName("zip").sendKeys("12345");
        browser.findElementByName("phone").sendKeys("123-123-1234");
        browser.findElementByCssSelector("form#registerForm").submit();
    }

    private void doLogin(String username, String password) {

        browser.findElementByCssSelector("input#username").sendKeys(username);
        browser.findElementByCssSelector("input#password").sendKeys(password);
        browser.findElementByCssSelector("form#loginForm").submit();
    }

    private void doLogout() {

        Optional.ofNullable(
                browser.findElementByCssSelector("form#logoutForm"))
                .ifPresent(WebElement::submit);
    }

    private void assertDesignPageElements() {

        assertEquals(designPageUrl(), browser.getCurrentUrl());
        var ingredientGroups =
                browser.findElementsByClassName("ingredient-group");
        assertEquals(5, ingredientGroups.size());

        var wrapGroup =
                browser.findElementByCssSelector("div.ingredient-group#wraps");
        var wraps = wrapGroup.findElements(By.tagName("div"));
        assertEquals(2, wraps.size());
        assertIngredient(wrapGroup, 0, "FLTO", "Flour Tortilla");
        assertIngredient(wrapGroup, 1, "COTO", "Corn Tortilla");

        var proteinGroup =
                browser.findElementByCssSelector("div.ingredient-group#proteins");
        var proteins = proteinGroup.findElements(By.tagName("div"));
        assertEquals(2, proteins.size());
        assertIngredient(proteinGroup, 0, "GRBF", "Ground Beef");
        assertIngredient(proteinGroup, 1, "CARN", "Carnitas");

        var cheeseGroup =
                browser.findElementByCssSelector("div.ingredient-group#cheeses");
        var cheeses = proteinGroup.findElements(By.tagName("div"));
        assertEquals(2, cheeses.size());
        assertIngredient(cheeseGroup, 0, "CHED", "Cheddar");
        assertIngredient(cheeseGroup, 1, "JACK", "Monterrey Jack");

        var veggieGroup =
                browser.findElementByCssSelector("div.ingredient-group#veggies");
        var veggies = proteinGroup.findElements(By.tagName("div"));
        assertEquals(2, veggies.size());
        assertIngredient(veggieGroup, 0, "TMTO", "Diced Tomatoes");
        assertIngredient(veggieGroup, 1, "LETC", "Lettuce");

        var sauceGroup =
                browser.findElementByCssSelector("div.ingredient-group#sauces");
        var sauces = proteinGroup.findElements(By.tagName("div"));
        assertEquals(2, sauces.size());
        assertIngredient(sauceGroup, 0, "SLSA", "Salsa");
        assertIngredient(sauceGroup, 1, "SRCR", "Sour Cream");
    }

    private void fillInAndSubmitOrderForm() {

        assertTrue(browser.getCurrentUrl().startsWith(orderDetailsPageUrl()));
        fillField("input#deliveryName", "Ima Hungry");
        fillField("input#deliveryStreet", "1234 Culinary Blvd.");
        fillField("input#deliveryCity", "Foodsville");
        fillField("input#deliveryState", "CO");
        fillField("input#deliveryZip", "81019");
        fillField("input#ccNumber", "4111111111111111");
        fillField("input#ccExpiration", "10/19");
        fillField("input#ccCVV", "123");
        browser.findElementByCssSelector("form#orderForm").submit();
    }

    private void submitEmptyOrderForm() {

        assertEquals(currentOrderDetailsPageUrl(), browser.getCurrentUrl());
        // clear fields automatically populated from user profile
        fillField("input#deliveryName", "");
        fillField("input#deliveryStreet", "");
        fillField("input#deliveryCity", "");
        fillField("input#deliveryState", "");
        fillField("input#deliveryZip", "");
        browser.findElementByCssSelector("form#orderForm").submit();

        assertEquals(orderDetailsPageUrl(), browser.getCurrentUrl());

        var validationErrors = getValidationErrorTexts();
        assertEquals(9, validationErrors.size());
        assertTrue(validationErrors.contains("Please correct the problems below and resubmit."));
        assertTrue(validationErrors.contains("Delivery name is required"));
        assertTrue(validationErrors.contains("Street is required"));
        assertTrue(validationErrors.contains("City is required"));
        assertTrue(validationErrors.contains("State is required"));
        assertTrue(validationErrors.contains("Zip code is required"));
        assertTrue(validationErrors.contains("Not a valid credit card number"));
        assertTrue(validationErrors.contains("Must be formatted MM/YY"));
        assertTrue(validationErrors.contains("Invalid CVV"));
    }

    private List<String> getValidationErrorTexts() {

        var validationErrorElements = browser.findElementsByClassName("validationError");
        return validationErrorElements.stream()
                .map(WebElement::getText)
                .collect(toList());
    }

    private void submitInvalidOrderForm() {

        assertTrue(browser.getCurrentUrl().startsWith(orderDetailsPageUrl()));
        fillField("input#deliveryName", "I");
        fillField("input#deliveryStreet", "1");
        fillField("input#deliveryCity", "F");
        fillField("input#deliveryState", "C");
        fillField("input#deliveryZip", "8");
        fillField("input#ccNumber", "1234432112344322");
        fillField("input#ccExpiration", "14/91");
        fillField("input#ccCVV", "1234");
        browser.findElementByCssSelector("form#orderForm").submit();

        assertEquals(orderDetailsPageUrl(), browser.getCurrentUrl());

        var validationErrors = getValidationErrorTexts();
        assertEquals(4, validationErrors.size());
        assertTrue(validationErrors.contains("Please correct the problems below and resubmit."));
        assertTrue(validationErrors.contains("Not a valid credit card number"));
        assertTrue(validationErrors.contains("Must be formatted MM/YY"));
        assertTrue(validationErrors.contains("Invalid CVV"));
    }

    private void fillField(String fieldName, String value) {

        var field = browser.findElementByCssSelector(fieldName);
        field.clear();
        field.sendKeys(value);
    }

    private void assertIngredient(WebElement ingredientGroup,
                                  int ingredientIdx, String id, String name) {

        var ingredients = ingredientGroup.findElements(By.tagName("div"));
        var ingredient = ingredients.get(ingredientIdx);
        assertEquals(id,
                ingredient.findElement(By.tagName("input")).getAttribute("value"));
        assertEquals(name,
                ingredient.findElement(By.tagName("span")).getText());
    }

    private void clickDesignATaco() {

        assertEquals(homePageUrl(), browser.getCurrentUrl());
        browser.findElementByCssSelector("a[id='design']").click();
    }

    private void clickBuildAnotherTaco() {

        assertTrue(browser.getCurrentUrl().startsWith(orderDetailsPageUrl()));
        browser.findElementByCssSelector("a[id='another']").click();
    }

    private String loginPageUrl() {

        return MessageFormat.format(
                "{0}login",
                homePageUrl());
    }

    private String registrationPageUrl() {

        return MessageFormat.format(
                "{0}register",
                homePageUrl());
    }

    private String designPageUrl() {

        return MessageFormat.format(
                "{0}design",
                homePageUrl());
    }

    private String homePageUrl() {

        return MessageFormat.format(
                "http://localhost:{0}/",
                String.valueOf(port));
    }

    private String orderDetailsPageUrl() {

        return MessageFormat.format(
                "{0}orders",
                homePageUrl());
    }

    private String currentOrderDetailsPageUrl() {

        return MessageFormat.format(
                "{0}orders/current",
                homePageUrl());
    }

}
