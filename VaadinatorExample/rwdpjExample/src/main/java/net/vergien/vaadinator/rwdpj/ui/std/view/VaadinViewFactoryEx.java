package net.vergien.vaadinator.rwdpj.ui.std.view;

import java.util.Map;

public class VaadinViewFactoryEx extends VaadinViewFactory {

	public VaadinViewFactoryEx(Map<String, Object> context) {
		super(context);
	}

	public MainView createMainView() {
		return new MainViewImplEx(context);
	};
}
