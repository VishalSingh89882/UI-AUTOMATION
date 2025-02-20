package com.magentoautomation.tests9;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.List; // Import the List class
import java.util.Random;

public class Wishlistfinaledittest {
    WebDriver driver;

    @BeforeClass
    public void setup() {
        // Setup ChromeDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @Test
    public void testUpdateWishlistItem() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Random random = new Random();

        // Navigate to Magento website
        driver.get("https://magento.softwaretestingboard.com/");

        // Sign in
        WebElement signInLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Sign In")));
        signInLink.click();
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        emailField.sendKeys("your_email@yopmail.com");
        WebElement passwordField = driver.findElement(By.id("pass"));
        passwordField.sendKeys("Test@1234");
        WebElement signInButton = driver.findElement(By.id("send2"));
        signInButton.click();
        Thread.sleep(5000);

        // Navigate to product page and add to wishlist
        WebElement product = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".product-item-link")));
        product.click();
        WebElement heartButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".product-addto-links > .action.towishlist > span")));
        heartButton.click();

        // Navigate to wishlist
        WebElement accountDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".header.panel > .header.links span[role='button'] > .action.switch")));
        accountDropdown.click();
        WebElement wishlistLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".header.panel > .header.links .customer-menu > .header.links > .link.wishlist > a")));
        wishlistLink.click();

        // Hover over the wishlist item
        WebElement wishlistItem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("strong > a[title='Radiant Tee']")));
        Actions actions = new Actions(driver);
        actions.moveToElement(wishlistItem).perform();

        // Click on Edit Info
        WebElement editInfoLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".product-item-inner .edit")));
        editInfoLink.click();
        Thread.sleep(3000); // Wait for the edit form to appear

        // Generate a random quantity between 1 and 10
        int randomQuantity = random.nextInt(10) + 1;

        // Update the quantity
        WebElement quantityField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='qty']")));
        quantityField.clear();
        quantityField.sendKeys(String.valueOf(randomQuantity));

        // Click on Update Wishlist
        WebElement updateWishlistButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".action.towishlist.updated > span")));
        updateWishlistButton.click();

        // Wait for the update and refresh the page
        Thread.sleep(5000); // Adjust wait time as needed
        driver.navigate().refresh();

        // Hover over the item again and click on Quantity
        wishlistItem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li:nth-of-type(1) > div > a[title='Radiant Tee'] img[alt='Radiant Tee']")));
        actions.moveToElement(wishlistItem).perform();
        WebElement quantityLabel = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".field.qty > .label > span")));
        quantityLabel.click();

        // Get the updated quantity and validate it
        WebElement updatedQuantityElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".field.qty input")));
        int updatedQuantity = Integer.parseInt(updatedQuantityElement.getAttribute("value"));
        Assert.assertEquals(updatedQuantity, randomQuantity, "The quantity in the wishlist did not match the updated value.");

        // Hover over the wishlist item to reveal the "Remove Item" link
        WebElement wishlistItem1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li:nth-of-type(1) > div > a[title='Radiant Tee'] img[alt='Radiant Tee']"))); // Replace with actual selector for the wishlist item
        Actions actions1 = new Actions(driver);
        actions1.moveToElement(wishlistItem1).perform();
        Thread.sleep(5000); // Pause for 5 seconds to ensure hover action

        // Copy the CSS path of the item
        String itemCssPath = wishlistItem.getAttribute("outerHTML"); // Use a unique identifier or attribute if needed
        System.out.println("Item CSS Path: " + itemCssPath); // Print the CSS path for debugging

        // Click the "Remove Item" link
        WebElement removeItemLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[title='Remove Item']")));
        removeItemLink.click();
        Thread.sleep(5000); // Pause for 5 seconds to ensure the item is removed

        // Validate that the item is no longer in the wishlist
        List<WebElement> wishlistItems = driver.findElements(By.cssSelector("li:nth-of-type(1) > div > a[title='Radiant Tee'] img[alt='Radiant Tee']")); // Replace with actual selector for wishlist items

        boolean itemRemoved = true;
        for (WebElement item : wishlistItems) {
            if (item.getAttribute("outerHTML").equals(itemCssPath)) {
                itemRemoved = false;
                break;
            }
        }

        // Assert that the item was removed
        Assert.assertTrue(itemRemoved, "Item was not removed from the wishlist.");
        System.out.println("Editing and validating the wishlist is succesful ");
    }

    @AfterClass
    public void tearDown() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }
}

