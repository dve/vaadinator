package net.vergien.vaadinator.rwdpj.ui.std.presenter;

import java.util.Map;

import net.vergien.vaadinator.rwdpj.service.AddressService;
import net.vergien.vaadinator.rwdpj.ui.presenter.Presenter;
import net.vergien.vaadinator.rwdpj.ui.presenter.SubviewCapablePresenter;
import net.vergien.vaadinator.rwdpj.ui.std.view.AddressChangeView;

public class AddressChangePresenterImplEx extends AddressChangePresenterImpl {

	public AddressChangePresenterImplEx(Map<String, Object> context, AddressChangeView view, Presenter returnPresenter,
			AddressService service) {
		super(context, view, returnPresenter, service);
		// TODO Auto-generated constructor stub
	}

	public AddressChangePresenterImplEx(Map<String, Object> context, AddressChangeView view, Presenter returnPresenter,
			SubviewCapablePresenter capablePresenter, AddressService service) {
		super(context, view, returnPresenter, capablePresenter, service);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onContextChange() {

		super.onContextChange();
		System.out.println("change.onContext...()");
	}
}
