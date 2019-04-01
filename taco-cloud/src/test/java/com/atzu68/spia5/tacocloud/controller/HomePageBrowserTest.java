package com.atzu68.spia5.tacocloud.controller;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HomePageBrowserTest {

    @LocalServerPort
    private int port;

    private static HtmlUnitDriver browser;

    @BeforeClass
    public static void setup() {

        browser = new HtmlUnitDriver();

        browser.manage().timeouts()
                .implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterClass
    public static void teardown() {

        browser.quit();
    }

    @Test
    public void testHomePage() {

        var homePage = String.format(
                "http://localhost:%s", port);
        browser.get(homePage);

        var titleText = browser.getTitle();
        assertEquals("Taco Cloud", titleText);

        var h1Text = browser.findElementByTagName("h1")
                .getText();
        assertEquals("Welcome to...", h1Text);

        var imgSrc = browser.findElementByTagName("img")
                .getAttribute("src");
        assertEquals(
                String.format(
                        "%s/images/TacoCloud.png", homePage),
                imgSrc);
    }
}
