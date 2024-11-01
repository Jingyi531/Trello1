import time
from asyncio import wait_for

from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.chrome.service import Service

driver = webdriver.Chrome()
driver.implicitly_wait(10)

try:
    # Navigate to the login page
    driver.get("http://localhost:3000/")

    # Find the email input field and enter the email
    email_field = driver.find_element(By.ID, "login_email")
    email_field.send_keys("bee@bee.com")

    # Find the password input field and enter the password
    password_field = driver.find_element(By.ID, "login_password")
    password_field.send_keys("Password1!")

    # Find the login button and click it
    login_button = driver.find_element(By.CSS_SELECTOR, "button.btn.btn-primary.form-button")
    login_button.click()

    # Add assertions here to verify successful login

    time.sleep(2)
    assert "http://localhost:3000/home" in driver.current_url
    print("Login test passed!")

except Exception as e:
    print(f"An error occurred: {e}")

finally:
    driver.quit()
