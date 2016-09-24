package net.vergien.vaadinator.rwdpj;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

import net.vergien.vaadinator.rwdpj.dao.AddressDaoPlain;
import net.vergien.vaadinator.rwdpj.layout.LayoutMode;
import net.vergien.vaadinator.rwdpj.service.AddressService;
import net.vergien.vaadinator.rwdpj.service.AddressServicePlain;
import net.vergien.vaadinator.rwdpj.ui.std.presenter.MainPresenter;
import net.vergien.vaadinator.rwdpj.ui.std.presenter.PresenterFactoryEx;
import net.vergien.vaadinator.rwdpj.ui.std.view.MainView;
import net.vergien.vaadinator.rwdpj.ui.std.view.VaadinViewFactoryEx;

/**
 * Main UI class
 */
@Title("VaadinatorRwdpj")
@Theme("valo")
@Push
public class VaadinatorRwdpjUI extends UI {
	public final static String LAYOUT_MODE = "LAYOUT_MODE";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<String, Object> context = new HashMap<>();

	@Override
	protected void init(VaadinRequest request) {
		Page.getCurrent().addBrowserWindowResizeListener(event -> {
			reLayout();
		});
		context.put(LAYOUT_MODE, getLayoutMode());
		mpres = obtainPresenterFactory(request.getContextPath()).createMainPresenter();
		MainView mview = mpres.getView();
		setContent((Component) mview.getComponent());
		// and go
		mpres.startPresenting();
	}

	private void reLayout() {
		LayoutMode layoutMode = getLayoutMode();
		if (layoutMode != context.get(LAYOUT_MODE)) {
			context.put(LAYOUT_MODE, layoutMode);
			mpres.onContextChange();
		}
	}

	PresenterFactoryEx presenterFactory = null;
	private MainPresenter mpres;

	protected PresenterFactoryEx obtainPresenterFactory(String contextPath) {
		if (presenterFactory == null) {
			// simple, overwrite method for e.g. Spring / CDI / ...
			// Entity-Manager NUR Thread-Safe, wenn er injected wird wie hier
			AddressService addressService;
			EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("VaadinatorRwdpj");
			AddressDaoPlain addressDaoPlain = new AddressDaoPlain(entityManagerFactory);
			addressService = new AddressServicePlain(entityManagerFactory, addressDaoPlain);
			presenterFactory = new PresenterFactoryEx(context, new VaadinViewFactoryEx(context), addressService);
		}
		return presenterFactory;
	}

	private LayoutMode getLayoutMode() {
		int width = Page.getCurrent().getBrowserWindowWidth();
		System.out.println(width);
		if (width < 480) {
			return LayoutMode.PHONE;
		} else if (width < 1024) {
			return LayoutMode.TABLET;
		} else {
			return LayoutMode.DESKTOP;
		}
	}

}