
package net.vergien.vaadinator.rwdpj;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.jsoup.nodes.Element;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;

@WebServlet(value = "/*", asyncSupported = true)
@VaadinServletConfiguration(productionMode = false, ui = VaadinatorRwdpjUI.class)
public class VaadinatorRwdpjServlet extends VaadinServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void servletInitialized() throws ServletException {
		super.servletInitialized();
		VaadinService.getCurrent().addSessionInitListener((com.vaadin.server.SessionInitEvent e) -> {
			e.getSession().addBootstrapListener(new BootstrapListener() {
				@Override
				public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
				}

				@Override
				public void modifyBootstrapPage(BootstrapPageResponse response) {
					Element head = response.getDocument().getElementsByTag("head").get(0);
					Element meta = head.appendElement("meta");
					meta.attr("name", "viewport");
					meta.attr("content", "width=device-width, initial-scale=1");
				}
			});
		});
	}
}
