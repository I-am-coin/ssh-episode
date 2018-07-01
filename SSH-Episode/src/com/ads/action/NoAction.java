package com.ads.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("struts-default")
@Namespace("/")
public class NoAction extends ActionSupport {
	private static final long serialVersionUID = 1L;

	/**
	 * 过滤随意请求
	 * @return
	 */
	@Action(value="*",
			results={
					@Result(name=SUCCESS, type="redirect", location="/episode/index.html")
			})
	public String noActionFilter() {
		return SUCCESS;
	}

}
