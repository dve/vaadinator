/*
 * Copyright 2014 akquinet engineering GmbH
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
package de.akquinet.engineering.vaadinator.example.contractapplication.ui.std.presenter;

import java.util.Map;

import de.akquinet.engineering.vaadinator.example.contractapplication.service.ContractApplicationService;
import de.akquinet.engineering.vaadinator.example.contractapplication.ui.presenter.Presenter;
import de.akquinet.engineering.vaadinator.example.contractapplication.ui.std.view.ContractApplicationAddView;
import de.akquinet.engineering.vaadinator.example.contractapplication.ui.std.view.ProductChoiceAwareObserver;

public class ContractApplicationAddPresenterImplEx extends ContractApplicationAddPresenterImpl implements ProductChoiceAwareObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ContractApplicationAddPresenterImplEx(Map<String, Object> context, ContractApplicationAddView view, Presenter returnPresenter, ContractApplicationService service) {
		super(context, view, returnPresenter, service);
		this.view = view;
	}

	private ContractApplicationAddView view;

	@Override
	public void onProductChoiceModified() {
		saveToModel();
		view.setMonthlyFeeStr(getContractApplication().getMonthlyFeeStr());
	}

}
