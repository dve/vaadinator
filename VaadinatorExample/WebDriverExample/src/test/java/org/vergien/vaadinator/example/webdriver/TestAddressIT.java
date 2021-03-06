/*
 * Copyright 2016 Daniel Nordhoff-Vergien
 *  
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.vergien.vaadinator.example.webdriver;

import static com.github.webdriverextensions.Bot.assertIsNotDisplayed;
import static com.github.webdriverextensions.Bot.assertIsOpen;
import static com.github.webdriverextensions.Bot.assertTextEquals;
import static com.github.webdriverextensions.Bot.assertThat;
import static com.github.webdriverextensions.Bot.assertValueEquals;
import static com.github.webdriverextensions.Bot.clearAndType;
import static com.github.webdriverextensions.Bot.open;
import static com.github.webdriverextensions.Bot.type;
import static com.github.webdriverextensions.vaadin.VaadinBot.clickAndWait;
import static com.github.webdriverextensions.vaadin.VaadinBot.waitForVaadin;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.Dimension;
import org.vaadin.addonhelpers.automated.AbstractWebDriverCase;
import org.vergien.vaadinator.example.webdriver.ui.std.view.webdriver.component.AddressSelectViewComponent.AddressListTableRowComponent;
import org.vergien.vaadinator.example.webdriver.ui.std.view.webdriver.page.AddressPage;

import com.github.webdriverextensions.Bot;
import com.github.webdriverextensions.junitrunner.WebDriverRunner;
import com.github.webdriverextensions.junitrunner.annotations.PhantomJS;
import com.github.webdriverextensions.junitrunner.annotations.TakeScreenshotOnFailure;

@RunWith(WebDriverRunner.class)
@PhantomJS
@TakeScreenshotOnFailure
public class TestAddressIT extends AbstractWebDriverCase {
	private AddressPage addressPage;

	@Before
	public void openPage() {
		open(BASEURL + "org.vergien.vaadinator.example.webdriver.WebDriverExampleDemo");
		waitForVaadin();
		assertIsOpen(addressPage);
		Bot.driver().manage().window().setSize(new Dimension(1024, 786));
	}

	@Test
	public void addPerson() {
		clickAndWait(addressPage.getAddressListViewComponent().getAddAddressWebElement());

		type("Daniel", addressPage.getAddressAddViewComponent().getVornameWebElement());
		String nachname = UUID.randomUUID().toString();
		type(nachname, addressPage.getAddressAddViewComponent().getNachnameWebElement());

		addressPage.getAddressAddViewComponent().getAnredeVaadinComboBox().selectItemFromFilter(0);
		clickAndWait(addressPage.getAddressAddViewComponent().getSaveWebElement());

		boolean foundInTable = false;
		for (AddressListTableRowComponent row : addressPage.getAddressListViewComponent()
				.getAddressSelectViewComponent().getAddressListTableRows()) {
			if (row.getNameCellWebElement().getText().equals("Daniel " + nachname)) {
				foundInTable = true;
				clickAndWait(row);
			}
		}
		assertThat("Generated address not found in table", foundInTable, is(true));

		assertValueEquals("Daniel", addressPage.getAddressChangeViewComponent().getVornameWebElement());
		assertValueEquals(nachname, addressPage.getAddressChangeViewComponent().getNachnameWebElement());
		assertThat(addressPage.getAddressChangeViewComponent().getAnredeVaadinComboBox().getValue(), is("Herr"));
	}

	@Test
	public void cancelAddingPerson() {
		clickAndWait(addressPage.getAddressListViewComponent().getAddAddressWebElement());

		clickAndWait(addressPage.getAddressAddViewComponent().getCancelWebElement());

		assertIsNotDisplayed(addressPage.getAddressAddViewComponent());
	}

	@Test
	public void searchPerson() {
		clickAndWait(addressPage.getAddressListViewComponent().getAddAddressWebElement());
		String nachName = UUID.randomUUID().toString();
		type("Daniel", addressPage.getAddressAddViewComponent().getVornameWebElement());
		type(nachName, addressPage.getAddressAddViewComponent().getNachnameWebElement());
		clickAndWait(addressPage.getAddressAddViewComponent().getSaveWebElement());

		type("Hans",
				addressPage.getAddressListViewComponent().getAddressSelectViewComponent().getSearchFieldWebElement());
		waitForVaadin();

		List<AddressListTableRowComponent> rows = addressPage.getAddressListViewComponent()
				.getAddressSelectViewComponent().getAddressListTableRows();
		for (AddressListTableRowComponent row : rows) {
			assertThat(row.getNameCellWebElement().getText(), is(not("Daniel " + nachName)));
		}

		clearAndType(nachName,
				addressPage.getAddressListViewComponent().getAddressSelectViewComponent().getSearchFieldWebElement());
		assertThat(addressPage.getAddressListViewComponent().getAddressSelectViewComponent().getAddressListTableRows()
				.size(), is(1));

		assertTextEquals("Daniel " + nachName, (addressPage.getAddressListViewComponent()
				.getAddressSelectViewComponent().getAddressListTableRows().get(0).getNameCellWebElement()));
	}
}
