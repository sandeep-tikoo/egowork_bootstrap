import org.marker.ext.ModuleActivatorimport org.marker.ext.ModuleUtilsimport org.marker.ext.module.ModuleContextimport org.marker.mushroom.ext.menu.MenuUtilsimport org.marker.mushroom.ext.model.ContentModelContextimport org.marker.mushroom.ext.model.impl.FinancingModelImpl/** * 自定义模块 *  * @author marker *  */public class ModuleActivatorImpl extends ModuleUtils implements ModuleActivator {		 	public void start(ModuleContext context) throws Exception {		ContentModelContext.getInstance().put(new FinancingModelImpl());		MenuUtils menuUtils = MenuUtils.getInstance();		// 人力资源管理菜单		def menu = [				name: "融资企业管理",				url : "financing/list.do",				desc: "系统融资企业内容管理",				icon: "fa-th",				type: "371a0ad5721b4a7789d924d4e577bb43" // 唯一编码		];		this.id = menuUtils.build(MenuUtils.L1_CONTENT, menu);			} 				public void stop(ModuleContext context) throws Exception {		ContentModelContext.getInstance().remove("financing");		MenuUtils menuUtils = MenuUtils.getInstance();		menuUtils.remove(this.id);			}}