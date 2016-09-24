package net.vergien.vaadinator.rwdpj.ui.std.presenter;

import java.util.Map;

import net.vergien.vaadinator.rwdpj.service.AddressService;
import net.vergien.vaadinator.rwdpj.ui.presenter.SubviewCapablePresenter;
import net.vergien.vaadinator.rwdpj.ui.std.view.AddressListView;

public class AddressListPresenterImplEx extends AddressListPresenterImpl{

	public AddressListPresenterImplEx(Map<String, Object> context, AddressListView view,
			PresenterFactory presenterFactory, AddressService service,
			SubviewCapablePresenter subviewCapablePresenter) {
		super(context, view, presenterFactory, service, subviewCapablePresenter);		
	}
	
	@Override
	public void onContextChange() {
		super.onContextChange();
		System.out.println("list.onContext()");
	}

}
