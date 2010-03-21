package app.web.bbean;

import java.util.List;
import java.util.logging.Logger;

import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.event.ValueChangeEvent;

import org.springframework.beans.factory.annotation.Autowired;

import app.data.Product;

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

	// Actions
	// -----------------------------------------------------------------------------------

	public String selectItem() {
		// Store the ID of the data item in hidden input element.
		dataItemId.setValue(dataItem.getId());
		return "buy"; // Navigation case.
	}

	public void addToCart(ValueChangeEvent event) {
		log.info("add To Cart "+event.getOldValue()+" to "+event.getNewValue());
	}
	// Getters
	// -----------------------------------------------------------------------------------

	public Product getCurrentItem() {
		return dataItem;
	}

	public HtmlInputHidden getDataItemId() {
		return dataItemId;
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
}
