package net.vergien.vaadinator.rwdpj.ui.std.view;

import java.util.Map;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import net.vergien.vaadinator.rwdpj.VaadinatorRwdpjUI;
import net.vergien.vaadinator.rwdpj.layout.LayoutMode;

public class MainViewImplEx extends MainViewImpl {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MainViewImplEx(Map<String, Object> context) {
		super(context);
	}

	@Override
	protected TabContent createTabContent() {
		if (context.get(VaadinatorRwdpjUI.LAYOUT_MODE) == LayoutMode.PHONE) {
			return new TabContentVerticalImpl();
		} else {
			return new TabContentImpl();
		}
	}

	@Override
	public void buildLayout() {
		for (String tabIdentifer : tabContents.keySet()) {
			TabContent oldTabContent = tabContents.get(tabIdentifer);
			TabContent newTabContent = createTabContent();
			newTabContent.replaceMasterComponent(oldTabContent.getMasterComponent());
			newTabContent.replaceDetailComponent(oldTabContent.getDetailComponent());
			tabContents.replace(tabIdentifer, newTabContent);
			tabs.replaceComponent(oldTabContent, newTabContent);
		}
	}

	public class TabContentVerticalImpl extends VerticalLayout implements TabContent {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private Component leftComponent = new Placeholder();
		private Component rightComponent = new Placeholder();

		public TabContentVerticalImpl() {
			super();
			setSizeFull();
			setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
			addComponent(leftComponent);
			addComponent(rightComponent);
		}

		@Override
		public void replaceMasterComponent(Component withComponent) {
			replaceComponent(leftComponent, withComponent);
			leftComponent = withComponent;

		}

		@Override
		public void replaceDetailComponent(Component withComponent) {
			replaceComponent(rightComponent, withComponent);
			rightComponent = withComponent;
			replaceComponent(rightComponent, withComponent);
			rightComponent = withComponent;

		}

		@Override
		public Component getMasterComponent() {
			return leftComponent;
		}

		@Override
		public Component getDetailComponent() {
			return rightComponent;
		}

	}

}
