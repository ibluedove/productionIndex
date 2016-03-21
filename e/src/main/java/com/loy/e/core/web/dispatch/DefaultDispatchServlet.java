package com.loy.e.core.web.dispatch;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.loy.e.core.web.SimpleUser;
/**
 * 
 * 
 * @author Loy Fu qq群 540553957
 * @since 1.7
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public class DefaultDispatchServlet extends DispatcherServlet{
	
	@Value("${com.loy.e.conf.supportLocales}")
	private String supportLocales;
	
	@Value("${com.loy.e.conf.defaultLocale}")
	private String defaultLocale;
	
	@Override
	protected LocaleContext buildLocaleContext(final HttpServletRequest request) {
		return new LocaleContext(){
			@Override
			public Locale getLocale() {
				Subject currentUser = org.apache.shiro.SecurityUtils.getSubject();
				SimpleUser simpleUser = (SimpleUser) currentUser.getPrincipal();
				Locale locale = null;
				if(simpleUser != null){
					locale = simpleUser.getLocale();
				}
				if(locale == null){
					locale = request.getLocale();
				}
				if(locale != null){
					String c = locale.getCountry();
					String l = locale.getLanguage();
					c = l+"_"+c;
					boolean support = false;
					String[] sls= supportLocales.split(",");
					for(String lang : sls){
						if(lang.equals(c)){
							support = true;
							break;
						}
					}
					if(support){
						return locale;
					}
				}
				
				String[] temp = defaultLocale.split("_");
				locale = new Locale(temp[0], temp[1]);
				return  locale;
			}};
	}
}
