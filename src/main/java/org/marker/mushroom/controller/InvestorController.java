package org.marker.mushroom.controller;

import org.marker.mushroom.beans.Investor;
import org.marker.mushroom.beans.Page;
import org.marker.mushroom.beans.ResultMessage;
import org.marker.mushroom.core.AppStatic;
import org.marker.mushroom.core.proxy.SingletonProxyFrontURLRewrite;
import org.marker.mushroom.service.impl.CategoryService;
import org.marker.mushroom.service.impl.DictionariesService;
import org.marker.mushroom.service.impl.InvestorService;
import org.marker.mushroom.support.SupportController;
import org.marker.mushroom.utils.HttpUtils;
import org.marker.urlrewrite.URLRewriteEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 文章管理
 *
 * @author marker
 */
@Controller
@RequestMapping("/admin/investor")
public class InvestorController extends SupportController {

	@Autowired
	InvestorService investorService;

	@Autowired
	CategoryService categoryService;

	@Autowired
	DictionariesService dictionariesService;

	public InvestorController() {
		this.viewPath = "/admin/investor/";
	}

	//发布文章
	@RequestMapping("/add")
	public ModelAndView add() {
		final ModelAndView view = new ModelAndView(this.viewPath + "add");
		view.addObject("categorys", categoryService.list("investor"));
		view.addObject("labels", dictionariesService.findDictionaries("label", "investor"));

		return view;
	}

	//编辑文章
	@RequestMapping("/edit")
	public ModelAndView edit(@RequestParam("id") final int id) {
		final ModelAndView view = new ModelAndView(this.viewPath + "edit");
		view.addObject("investor", commonDao.findById(Investor.class, id));
		view.addObject("categorys", categoryService.list("investor"));
		view.addObject("labels", dictionariesService.findDictionaries("label", "investor"));

		return view;
	}

	/**
	 * 持久化文章操作
	 *
	 * @param investor
	 * @param cid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(@ModelAttribute("investor") final Investor investor, @RequestParam("cid") final int cid) {
		investor.setCreateTime(new Date());
		investor.setCid(cid);// 这里是因为不能注入bean里

		if (commonDao.save(investor)) {
			return new ResultMessage(true,  "提交成功!");
		} else {
			return new ResultMessage(false,  "提交失败!");
		}
	}

	//保存
	@ResponseBody
	@RequestMapping("/update")
	public Object update(@ModelAttribute("investor") final Investor investor) {
		if(investor.getStatus()==1){
			investor.setTime(new Date());
		}

		if (commonDao.update(investor)) {
			return new ResultMessage(true, "更新成功!");
		} else {
			return new ResultMessage(false, "更新失败!");
		}
	}

	//删除文章
	@ResponseBody
	@RequestMapping("/delete")
	public Object delete(@RequestParam("rid") final String rid) {
		final boolean status = commonDao.deleteByIds(Investor.class, rid);
		if (status) {
			return new ResultMessage(true, "删除成功!");
		} else {
			return new ResultMessage(false, "删除失败!");
		}
	}

	//发布文章
	@RequestMapping("/list")
	public ModelAndView listview(final HttpServletRequest request) {
		final ModelAndView view = new ModelAndView(this.viewPath + "list");
		view.addObject("categorys", categoryService.list("investor"));

		return view;
	}

	/**
	 * 文章列表接口(REST)
	 *
	 * @param currentPageNo
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	public Object list(final HttpServletRequest request, final ModelMap model,
					   @RequestParam("currentPageNo") final int currentPageNo, @RequestParam("cid") String cid,
					   @RequestParam("status") final String status, @RequestParam("keyword") final String keyword,
					   @RequestParam("pageSize") final int pageSize

	) {
		final HttpSession session = request.getSession();
		final String usercategory = (String) session.getAttribute(AppStatic.WEB_APP_SESSION_USER_CATEGORY);
		final Integer userId = (Integer) session.getAttribute(AppStatic.WEB_APP_SESSION_USER_ID);
		final Integer groupId = (Integer) session.getAttribute(AppStatic.WEB_APP_SESSSION_USER_GROUP_ID);

		if (StringUtils.isEmpty(cid)) {
			cid = usercategory;
		}

		final Map<String, Object> params = new HashMap<>();
		params.put("cid", cid);
		params.put("status", status);
		params.put("keyword", keyword);
		params.put("userid", userId);
		params.put("groupid", groupId);
		final Page page = investorService.find(currentPageNo, pageSize, params);

		final URLRewriteEngine urlRewrite = SingletonProxyFrontURLRewrite.getInstance();

		final String url = HttpUtils.getRequestURL(request);
		// 遍历URL重写
		for (final Map<String, Object> data : page.getData()) {
			data.put("url", url + urlRewrite.encoder(data.get("url").toString()));
		}
		return page;
	}

	//置顶
	@ResponseBody
	@RequestMapping("/top")
	public Object top(@RequestParam("ids") final String ids, @RequestParam("level") final String level) {
		String tips = "";
		if (commonDao.top(Investor.class, ids, level)) {
			if (level.equals("1"))
				tips = "置顶成功！";
			else if (level.equals("0"))
				tips = "取消置顶成功！";

			return new ResultMessage(true, tips);
		} else {
			if (level.equals("1"))
				tips = "置顶失败！";
			else if (level.equals("0"))
				tips = "取消置顶失败！";

			return new ResultMessage(false, tips);
		}
	}
}
