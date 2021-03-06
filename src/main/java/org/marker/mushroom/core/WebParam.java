package org.marker.mushroom.core;

import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.core.config.impl.SystemConfig;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求参数解析器 调用get方法可以直接获取到请求参数数据
 *
 * @author marker
 */
public final class WebParam {

	public static final String ATTR_WEB_PARAM = ".mrcms.Webparam";

	public static final String FIELD_P = "p";
	public static final String FIELD_T = "type";
	public static final String FIELD_ID = "id";
	public static final String FIELD_PAGE = "page";

	/** 当前请求的静态URL名称 */
	public String pageName = "";
	/** 当前请求的模板对象名称 */
	public String template = "";
	/** 内容模型类型 */
	public String modelType = "";
	/** 内容ID */
	public String contentId = "0";
	/** 页码（默认为1） */
	public String page = "1";//页码
	public int currentPageNo = 1;// 页码
	/** 重定向URL地址 */
	public String redirect;
	/** 页面大小 */
	public int pageSize;

	/** 扩展条件查询 */
	public String extendSql;

	/** 排序条件 **/
	public String orderSql;

	/** 系统配置信息 */
	private static final SystemConfig config = SystemConfig.getInstance();

	/**
	 * 只有通过传递请求对象，才能获取解析数据对象
	 */
	public static WebParam get() {
		final HttpServletRequest req = ActionContext.getReq();
		final WebParam param = (WebParam) req.getAttribute(ATTR_WEB_PARAM);
		if (param != null)
			return param; // 如果获取到数据，则返回
		return new WebParam(req);
	}

	/**
	 * 私有构造禁止开发者创建此对象
	 */
	private WebParam(final HttpServletRequest req) {
		this.pageName = req.getParameter(FIELD_P);// 页面名称
		this.modelType = req.getParameter(FIELD_T);// 页面类型
		if (this.modelType == null) {
			this.modelType = "channel";
		}
		if (!(this.pageName != null && !"".equals(this.pageName))) {
			this.pageName = config.get("index_page");// 获取默认主页地址
		}
		this.contentId = req.getParameter(FIELD_ID);// 内容ID

		this.page = req.getParameter(FIELD_PAGE);// 页码
		try {
			this.currentPageNo = Integer.parseInt(this.page);
		} catch (final Exception ignored) {
		}

		// 初始化模版页面（指向错误页面）
		this.template = config.get("error_page");
		req.setAttribute("p", this.pageName);

		// 绑定请求数据到请求对象，避免二次解析
		req.setAttribute(ATTR_WEB_PARAM, this);

	}

	@Override
	public String toString() {

		return "R:\n" + "pageName=" + pageName + "\n" +
			"modelType=" + modelType + "\n" +
			"contentId=" + contentId + "\n" +
			"page=" + page + "\n";
	}

}
