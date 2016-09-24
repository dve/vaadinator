package net.vergien.vaadinator.rwdpj.ui.std.presenter;

import java.util.Map;

import net.vergien.vaadinator.rwdpj.ui.std.view.MainView;
import net.vergien.vaadinator.rwdpj.ui.view.VaadinView;

public class MainPresenterImplEx extends MainPresenterImpl implements MainPresenter {

	public MainPresenterImplEx(Map<String, Object> context, MainView view, PresenterFactory presenterFactory) {
		super(context, view, presenterFactory);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onContextChange() {
		super.onContextChange();
		((VaadinView) getView()).buildLayout();
	}
}
