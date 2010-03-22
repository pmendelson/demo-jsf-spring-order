package app.web.bbean;

import java.util.List;
import java.util.logging.Logger;

import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import app.data.Product;
import app.workflow.ShoppingCart;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.PagingList;
import com.avaje.ebean.Query;

/**
 * Homepage
 */
public class ProductCatalogBeanImpl implements ProductCatalogBean {
	private static Logger log = Logger.getLogger(ProductCatalogBeanImpl.class.getName());
	private static final long serialVersionUID = 1L;
	/** The ebean server. */
	@Autowired
	private EbeanServer mServer;
	private static final String ID4BUTTON_BAR = "buttonbar";
	private static final String ID4PRODUCT_LIST = "plist";
	private static final String SORTBY_NAME = "name";
	private static final String SORTBY_SKU = "sku";
	private transient Query<Product> query;
	private transient PagingList<Product> pager = null;
	private boolean orderup;
	private String sortProp;
	// Init
	// --------------------------------------------------------------------------------------

	private HtmlDataTable dataTable;
	private Product dataItem = new Product();
	private HtmlInputHidden dataItemId = new HtmlInputHidden();
//	@Autowired
//	private ShoppingCart mCart;

	// Actions
	// -----------------------------------------------------------------------------------

	public String selectItem() {
		// Store the ID of the data item in hidden input element.
		dataItemId.setValue(dataItem.getId());
		return "buy"; // Navigation case.
	}

	public void addToCart(ValueChangeEvent event) {
		ShoppingCart cart = getShoppingCart();
		cart.addItem(getCurrentItem(), Integer.parseInt((String) event.getNewValue()));
//		log.info("add To Cart "+event.getOldValue()+" to "+event.getNewValue());
	}

	private ShoppingCart getShoppingCart() {
		ShoppingCart cart=(ShoppingCart) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cart");
		log.info("Cart from session map="+cart);
		if(cart==null)
			cart=new ShoppingCart(null);
		return cart;
	}
	// Getters
	// -----------------------------------------------------------------------------------

	public Product getCurrentItem() {
		return dataItem;
	}

	public HtmlInputHidden getDataItemId() {
		return dataItemId;
	}
	public HtmlDataTable getDataTable() {
		return dataTable;
	}

	// Setters
	// -----------------------------------------------------------------------------------

	public void setDataTable(HtmlDataTable dataTable) {
		this.dataTable = dataTable;
	}

	public void setItem(Product dataItem) {
		log.info("SET..." + dataItem);
		this.dataItem = dataItem;
	}

	public void setDataItemId(HtmlInputHidden dataItemId) {
		this.dataItemId = dataItemId;
	}

	public String getItemActionLabel() {
		Product prod=(Product) dataTable.getRowData();
		log.info("prod="+prod);
		ShoppingCart cart = getShoppingCart();
		return cart!=null && cart.hasItem(prod)?"Remove from Cart":"Add to Cart";
	}

	public List<Product> getProductList() {
		int first = 1;
		int count = 100;
		if (pager == null) {
			setPager(count);
		}
		return pager.getPage(first / pager.getPageSize()).getList();
	}

	public int getSize() {
		if (pager == null) {
			return getQuery().findRowCount();
		} else
			return pager.getTotalRowCount();
	}

	private Query<Product> getQuery() {
		if (query == null)
			query = mServer.find(Product.class);
		return query;
	}

	private void setPager(int count) {
		if (query == null)
			setFilter(null);
		// SortParam sort = getSort();
		// orderup = sort == null || sort.isAscending();
		// if (sort != null && sort.getProperty().equalsIgnoreCase(SORTBY_SKU))
		// {
		// query.orderBy("sku" + (orderup ? "" : " desc") + ",name,id");
		// sortProp = SORTBY_SKU;
		// } else {
		query.orderBy("name" + (orderup ? "" : " desc") + ",sku,id");
		// sortProp = SORTBY_NAME;
		// }
		pager = query.findPagingList(count);
	}

	public void setFilter(String search) {
		query = mServer.find(Product.class);
		if (search != null)
			query.where("name" + " like " + "'%" + search + "%'");
		pager = null;
	}

	/**
	 * Sets the ebean server.
	 * 
	 * @param ebeanServer
	 *            the ebeanServer to set
	 */
	public void setEbeanServer(EbeanServer ebeanServer) {
		this.mServer = ebeanServer;
	}

	/**
	 * Sets the ebean server.
	 * 
	 * @param ebeanServer
	 *            the ebeanServer to set
	 */
//	public void setShoppingCart(ShoppingCart v) {
//		this.mCart = v;
//	}
}
