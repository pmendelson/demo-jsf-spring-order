package app.web.bbean.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.FacesRequestAttributes;

/**
 * Helper for general layout, navigation and general Faces awareness.
 * 
 * @author Paul Mendelson
 * @since 1.0, 2010-Apr
 */
public class EBeanStoreBean {
	private static Logger log = Logger.getLogger(EBeanStoreBean.class.getName());
	private static final long serialVersionUID = 1L;

	// TODO Add any page properties or variables here

	/**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param parameters
	 *            Page parameters
	 */
	public List getAdditionalLinks() {
		final HttpServletRequest rq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		log.info("url=" + rq.getRequestURI());
		List r = new ArrayList();
		r.add(createAlso("cart", "Shopping Cart"));
		r.add(createAlso("cag", "Update Catalog"));
		r.add(createAlso("address", "Maintain Addresses"));
		// WebMarkupContainer also = new WebMarkupContainer("also");
		// add(also.setOutputMarkupId(true));
		// RepeatingView container = new RepeatingView("alsolist");
		// also.add(container.setOutputMarkupId(true));
		// if (!(this instanceof HomePage))
		// createAlso(container, HomePage.class, "Shopping Cart");
		// if (!(this instanceof CatalogEditPage))
		// createAlso(container, CatalogEditPage.class, "Update Catalog");
		// createAlso(container, OrderPage.class, "Maintain Addresses");
		return r;
	}

	//
	// private void createAlso(RepeatingView container, Class<? extends Page>
	// clazz, final String txt) {
	// createAlso(container, new BookmarkablePageLink("link", clazz), txt);
	// }
	//
	private Object createAlso(final String page, final String txt) {
		return new Link(page, txt);
	}

	public class Link {
		private String mLabel;
		private String mUrl;

		public Link(String page, String txt) {
			mLabel = txt;
			mUrl = page + ".jsf";
		}

		public String getLabel() {
			return mLabel;
		}

		public String getUrl() {
			return mUrl;
		}
	}
}
