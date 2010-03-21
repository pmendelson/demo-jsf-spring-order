package app.web.bbean;

import java.util.List;

import javax.faces.component.html.HtmlDataTable;

import app.data.Product;


/**
 * JSF Contract for a bean to navigate product catalog
 * @author Paul Mendelson
 * @since 1.0, 2010-Apr
 */
public interface ProductCatalogBean {
	/** Set of items in the catalog */
	List<Product> getProductList();

	/** # of items in the catalog */
	int getSize();

	/** Select item for next UI step.
	 * Item is communicated via {@link #setItem(Product)}
	 * or {link {@link #setDataTable(HtmlDataTable)}}.
	 * @return naviation rule for next page. */
	String selectItem();

	/** Select this item for next UI step. */
	void setItem(Product v);

	/** Bind underlying table to this bean */
	void setDataTable(HtmlDataTable dataTable);
}
