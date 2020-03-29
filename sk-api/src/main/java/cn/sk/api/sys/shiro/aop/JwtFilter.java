package cn.sk.api.sys.shiro.aop;

import cn.sk.api.sys.common.CustomException;
import cn.sk.api.sys.common.ResponseCode;
import cn.sk.api.sys.common.SysConst;
import cn.sk.api.sys.shiro.JwtToken;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *@Deseription 鉴权登录拦截器
 *@Author zhoucp
 *@Date 2020/1/6 15:53
 **/
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

	/**
	 * 执行登录认证
	 *
	 * @param request
	 * @param response
	 * @param mappedValue
	 * @return
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		String uri = ((ShiroHttpServletRequest) request).getRequestURI();
		if(uri.equals("/sk/sysUser/export")
				||uri.equals("/sk/sysUser/import")||uri.equals("/sk/sysUser/downTemplate")) {
			return true;
		}
		try {
			executeLogin(request, response);
			return true;
		} catch (Exception e) {
			throw new CustomException(ResponseCode.TOKEN_LOSE_EFFICACY);
		}
	}

	/**
	 *
	 */
	@Override
	protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String token = httpServletRequest.getHeader(SysConst.X_ACCESS_TOKEN);

		JwtToken jwtToken = new JwtToken(token);
		// 提交给realm进行登入，如果错误他会抛出异常并被捕获
		getSubject(request, response).login(jwtToken);
		// 如果没有抛出异常则代表登入成功，返回true

		return true;
	}

	/**
	 * 对跨域提供支持
	 */
	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response){
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
		httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
		httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
		// 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
		if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
			httpServletResponse.setStatus(HttpStatus.OK.value());
			return false;
		}
		try {
			return super.preHandle(request, response);
		} catch (Exception e) {
			httpServletResponse.setCharacterEncoding("utf-8");
			httpServletResponse.setContentType("application/json; charset=utf-8");
			httpServletResponse.setStatus(HttpStatus.OK.value());
			PrintWriter writer = null;
			try {
				writer = httpServletResponse.getWriter();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code",ResponseCode.TOKEN_LOSE_EFFICACY.getCode());
			jsonObject.put("msg",ResponseCode.TOKEN_LOSE_EFFICACY.getMsg());
			writer.write(jsonObject.toJSONString());
			return false;
		}
	}
}
