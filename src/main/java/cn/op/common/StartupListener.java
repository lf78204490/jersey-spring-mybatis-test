package cn.op.common;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 由于filter、servlet不属于spring容器管理,所以filter内不能注入bean;
 * 为解决此问题我们写了这个listener,在web启动时将spring context丢进去. 这样就可以从filter里拿到我们想要的bean了.
 * 
 * 替代web.xml中Spring的listener
 */
public class StartupListener extends ContextLoaderListener implements
		ServletContextListener {

	private static ApplicationContext appContext;
	private static ServletContext servletContext;
	// protected Log log = LogFactory.getLog(getClass());
	protected Logger log = Logger.getLogger(StartupListener.class);

	public void contextInitialized(ServletContextEvent event) {

		if (log.isDebugEnabled()) {
			log.debug("initializing context...");
		}

		// call Spring's context ContextLoaderListener to initialize
		// all the context files specified in web.xml
		super.contextInitialized(event);

		ServletContext context = event.getServletContext();
		setServletContext(context);

		ApplicationContext ctx = WebApplicationContextUtils
				.getRequiredWebApplicationContext(context);
		setAppContext(ctx);

		setupContext(context);
	}

	public static void setupContext(ServletContext context) {

	}

	/**
	 * 根据beanName获取spring容器管理的bean
	 * 
	 * @param beanName
	 * @return
	 */
	public static Object getBean(String beanName) {
		return appContext.getBean(beanName);
	}

	public static String getServletWebInfRealPath() {
		return servletContext.getRealPath("WEB-INF");
	}

	public static ApplicationContext getAppContext() {
		return appContext;
	}

	public static void setAppContext(ApplicationContext ctx) {
		appContext = ctx;
	}

	public static ServletContext getServletContext() {
		return servletContext;
	}

	public static void setServletContext(ServletContext servletContext) {
		StartupListener.servletContext = servletContext;
	}
}