package net.vergien.vaadinator.rwdpj.ui.std.presenter;

import java.util.Map;

import net.vergien.vaadinator.rwdpj.service.AddressService;
import net.vergien.vaadinator.rwdpj.ui.std.view.ViewFactory;

public class PresenterFactoryEx extends PresenterFactory {
	public PresenterFactoryEx(Map<String, Object> context, ViewFactory viewFactory, AddressService addressService) {
		super(context, viewFactory, addressService);
	}

	@Override
	public MainPresenter createMainPresenter() {
		return new MainPresenterImplEx(context, viewFactory.createMainView(), this);
	}
}
